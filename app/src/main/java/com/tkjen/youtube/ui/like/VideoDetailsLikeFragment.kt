package com.tkjen.youtube.ui.like

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.tkjen.youtube.R
import com.tkjen.youtube.data.local.DatabaseHelper
import com.tkjen.youtube.data.mapper.YoutubeMapper
import com.tkjen.youtube.data.model.VideoItem
import com.tkjen.youtube.databinding.FragmentLikeVideosBinding
import com.tkjen.youtube.databinding.FragmentVideoDetailsBinding
import com.tkjen.youtube.databinding.FragmentVideoDetailsLikeBinding
import com.tkjen.youtube.ui.home.adapter.VideoAdapter
import com.tkjen.youtube.ui.like.adapter.LikeVideoAdapter
import com.tkjen.youtube.ui.video_details.VideoDetailsFragmentArgs
import com.tkjen.youtube.ui.video_details.VideoDetailsViewModel
import com.tkjen.youtube.utils.Result
import com.tkjen.youtube.utils.formatTimeAgo
import com.tkjen.youtube.utils.formatViewCount
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class VideoDetailsLikeFragment: Fragment(R.layout.fragment_video_details_like) {

    private lateinit var binding: FragmentVideoDetailsLikeBinding

    private val viewModel: VideoDetailsLikeViewModels by viewModels()
    private val args: VideoDetailsFragmentArgs by navArgs()
    private lateinit var videoAdapter: VideoAdapter
    private var youTubePlayer: YouTubePlayer? = null
    private var isPlayerReady = false
    private var currentVideoId: String? = null
    @Inject
    lateinit var  databaseHelper: DatabaseHelper
    private lateinit var likeVideoAdapter: LikeVideoAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        binding = FragmentVideoDetailsLikeBinding.bind(view)

        currentVideoId = args.videoId
        setupYouTubePlayer()
        setupRecyclerView()
        setupClickListeners()
        observeViewModel()


        // Load dữ liệu video
        currentVideoId?.let { viewModel.loadVideoDetails(it) }



            viewModel.loadLikedVideos()


    }
    private fun setupYouTubePlayer() {
        lifecycle.addObserver(binding.youtubePlayerView)

        binding.youtubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(player: YouTubePlayer) {
                youTubePlayer = player
                isPlayerReady = true
                currentVideoId?.let { player.loadVideo(it, 0f) }
            }
        })
    }

    private fun setupRecyclerView() {

        videoAdapter = VideoAdapter{clickVideo->
            lifecycleScope.launch {
                val recentVideo = YoutubeMapper.toRecentVideo(clickVideo)
                databaseHelper.insertRecentVideo(recentVideo)

                val action = VideoDetailsLikeFragmentDirections.actionVideoDetailsLikeFragmentToVideoDetailsFragment(clickVideo.id)

                findNavController().navigate(action)
                Log.d("CategoryVideosFragment", "Lưu video thành công: ${recentVideo.videoTitle}")
            }

        }
        binding.recyclerViewVideos.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = videoAdapter
        }
        likeVideoAdapter = LikeVideoAdapter(
            onItemClick = { video ->
                // Xử lý click item ở đây nếu cần
            }
        )

        binding.rvLikeVideos.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = likeVideoAdapter
        }
    }


    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            // Quan sát thông tin chi tiết video
            viewModel.videoDetails.collectLatest { result ->
                when (result) {
                    is Result.Success -> {
                        val video = result.data
                        currentVideoId = video.id
                        updateUI(video)
                        if (isPlayerReady) {
                            youTubePlayer?.loadVideo(video.id, 0f)
                        }


                    }
                    is Result.Error -> {
                        Toast.makeText(requireContext(), result.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> {} // Loading state
                }
            }

            // Quan sát số lượng subscriber
            viewModel.subscriberCount.collectLatest { count ->
                binding.tvSubscriberCount.text = "$count Subscribers"
            }


            // Quan sát danh sách video liên quan


        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.videos.collectLatest { result ->
                when (result) {
                    is Result.Success -> {
                        Log.d("VideoDetailsFragment", "Related videos loaded: ${result.data.size}")
                        if (result.data.isNotEmpty()) {
                            binding.recyclerViewVideos.visibility = View.VISIBLE
                            videoAdapter.submitList(result.data)

                        } else {
                            binding.recyclerViewVideos.visibility = View.GONE
                        }
                    }
                    is Result.Error -> {
                        Log.e("VideoDetailsFragment", "Error loading related videos: ${result.message}")
                        binding.recyclerViewVideos.visibility = View.GONE
                        Toast.makeText(requireContext(), result.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        binding.recyclerViewVideos.visibility = View.GONE
                    }
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.videoLike.collectLatest { result ->
                when (result) {
                    is Result.Loading -> {

                    }
                    is Result.Success -> {
                        likeVideoAdapter.submitList(result.data)
                        binding.tvTitle.text = viewModel.getNextVideoTitle() ?: "Không có video tiếp theo"

                    }
                    is Result.Error -> {
                        Toast.makeText(requireContext(), result.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun updateUI(video: VideoItem) {
        binding.apply {
            tvVideoTitle.text = video.snippet.title
            tvViewsCount.text = formatViewCount(video.statistics?.viewCount)
            tvUploadDate.text = formatTimeAgo(video.snippet.publishedAt)
            tvLikeCount.text = formatViewCount(video.statistics?.likeCount)
            tvChannelName.text = video.snippet.channelTitle
            tvDescription.text = video.snippet.description

            Glide.with(requireContext())
                .load(video.snippet.thumbnails.high?.url)
                .placeholder(R.drawable.placeholder_avatar)
                .circleCrop()
                .into(ivChannelAvatar)

        }

        viewLifecycleOwner.lifecycleScope.launch {
            val isLiked = databaseHelper.isVideoLiked(args.videoId)
            updateLikeIcon(isLiked)
            val videos = databaseHelper.getLikedVideos().firstOrNull() ?: emptyList()
            val videoNow = viewModel.getCurrentVideoIndex()
            binding.tvDuration.text = "${videoNow}/${videos.size}"
            binding.tvPlaylistCount.text = binding.tvDuration.text
        }

    }

    private fun setupClickListeners() {
        binding.apply {
            btnLike.setOnClickListener {
                Toast.makeText(requireContext(), "Đã thích video", Toast.LENGTH_SHORT).show()
            }

            btnDislike.setOnClickListener {
                Toast.makeText(requireContext(), "Không thích video", Toast.LENGTH_SHORT).show()
            }

            btnShareAction.setOnClickListener {
                Toast.makeText(requireContext(), "Chia sẻ video", Toast.LENGTH_SHORT).show()
            }

            btnSave.setOnClickListener {
                Toast.makeText(requireContext(), "Đã lưu video", Toast.LENGTH_SHORT).show()
            }

            btnSubscribe.setOnClickListener {
                Toast.makeText(requireContext(), "Đăng ký kênh", Toast.LENGTH_SHORT).show()
            }
            icLike.setOnClickListener {
                val currentVideo = viewModel.videoDetails.value
                if (currentVideo is Result.Success) {
                    val likeVideo = YoutubeMapper.toLikeVideo(currentVideo.data)
                    viewLifecycleOwner.lifecycleScope.launch {
                        val isLiked = databaseHelper.toggleLikeVideo(likeVideo)
                        if (isLiked) {
                            Toast.makeText(requireContext(), "Đã thích video", Toast.LENGTH_SHORT).show()
                            Log.d("LikeVideo", "Liked: ${likeVideo.videoTitle}")
                            // Optional: đổi icon sang trạng thái đã thích
                            icLike.setImageResource(R.drawable.ic_liked)
                        } else {
                            Toast.makeText(requireContext(), "Đã bỏ thích video", Toast.LENGTH_SHORT).show()
                            Log.d("LikeVideo", "Unliked: ${likeVideo.videoTitle}")
                            icLike.setImageResource(R.drawable.ic_thumb_up)
                            // Optional: đổi icon sang trạng thái chưa thích
                        }
                    }
                } else {
                    Toast.makeText(requireContext(), "Chưa có video để thao tác", Toast.LENGTH_SHORT).show()
                }
            }


            btnShowMore.setOnClickListener {
                val isCollapsed = tvDescription.maxLines == 3
                tvDescription.maxLines = if (isCollapsed) Int.MAX_VALUE else 3
                btnShowMore.text = if (isCollapsed) "Thu gọn" else "Hiển thị thêm"
            }
            btnCollapse.setOnClickListener {
                playlistContainer.visibility = View.VISIBLE
                lnListLikeVideos.visibility = View.VISIBLE
                viewModel.loadLikedVideos()
                btnCollapse.setImageResource(R.drawable.ic_keyboard_arrow_down)
            }

            btnClosePlaylist.setOnClickListener {
                // Hide playlist
                playlistContainer.visibility = View.GONE
                lnListLikeVideos.visibility = View.VISIBLE
                binding.lnActionButtons.visibility = View.VISIBLE
                // Reset arrow direction
                btnCollapse.setImageResource(R.drawable.ic_keyboard_arrow_up)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.youtubePlayerView.release()
        youTubePlayer = null
        isPlayerReady = false
    }
    private fun updateLikeIcon(isLiked: Boolean) {
        binding.icLike.setImageResource(
            if (isLiked) R.drawable.ic_liked else R.drawable.ic_thumb_up
        )
    }

}