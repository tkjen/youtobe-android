package com.tkjen.youtube.ui.video_details

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.tkjen.youtube.R
import com.tkjen.youtube.data.model.VideoItem
import com.tkjen.youtube.databinding.FragmentVideoDetailsBinding
import com.tkjen.youtube.utils.formatTimeAgo
import com.tkjen.youtube.utils.formatViewCount
import com.tkjen.youtube.utils.Result
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class VideoDetailsFragment : Fragment(R.layout.fragment_video_details) {

    private lateinit var binding: FragmentVideoDetailsBinding
    private val viewModel: VideoDetailsViewModel by viewModels()
    private val args: VideoDetailsFragmentArgs by navArgs()

    private var youTubePlayer: YouTubePlayer? = null
    private var isPlayerReady = false
    private var currentVideoId: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentVideoDetailsBinding.bind(view)

        // Lấy videoId từ navArgs
        currentVideoId = args.videoId

        setupYouTubePlayer()
        observeVideoDetails()
        setupClickListeners()

        // Gọi ViewModel để lấy dữ liệu video
        viewModel.loadVideoDetails(currentVideoId!!)
    }

    // Cài đặt YouTube Player
    private fun setupYouTubePlayer() {
        lifecycle.addObserver(binding.youtubePlayerView)

        binding.youtubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(player: YouTubePlayer) {
                youTubePlayer = player
                isPlayerReady = true

                // Nếu đã có videoId -> load video
                currentVideoId?.let {
                    player.loadVideo(it, 0f)
                }
            }

            override fun onStateChange(player: YouTubePlayer, state: PlayerConstants.PlayerState) {
                // Có thể log hoặc xử lý thêm nếu cần
            }
        })
    }

    // Quan sát dữ liệu từ ViewModel
    private fun observeVideoDetails() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.videoDetails.collectLatest { result ->
                when (result) {
                    is Result.Loading -> {
                        // Hiển thị loading nếu muốn
                    }

                    is Result.Success -> {
                        val video = result.data
                        currentVideoId = video.id
                        updateUI(video)

                        // Nếu player đã sẵn sàng thì load lại video
                        if (isPlayerReady) {
                            youTubePlayer?.loadVideo(video.id, 0f)
                        }
                    }

                    is Result.Error -> {
                        Toast.makeText(requireContext(), result.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    // Cập nhật giao diện từ dữ liệu video
    private fun updateUI(video: VideoItem) {
        binding.apply {
            tvVideoTitle.text = video.snippet.title
            tvViewsCount.text = formatViewCount(video.statistics?.viewCount)
            tvUploadDate.text = formatTimeAgo(video.snippet.publishedAt)
            tvLikeCount.text = formatViewCount(video.statistics?.likeCount)
            tvChannelName.text = video.snippet.channelTitle
            tvSubscriberCount.text = "${formatViewCount(video.statistics?.subscriberCount)} subscriber"
            tvDescription.text = video.snippet.description

            Glide.with(requireContext())
                .load(video.snippet.thumbnails.high?.url)
                .placeholder(R.drawable.placeholder_avatar)
                .circleCrop()
                .into(ivChannelAvatar)
        }
    }

    // Bắt sự kiện các nút bấm
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

            btnShowMore.setOnClickListener {
                // Mở rộng/thu gọn phần mô tả
                val isCollapsed = tvDescription.maxLines == 3
                tvDescription.maxLines = if (isCollapsed) Int.MAX_VALUE else 3
                btnShowMore.text = if (isCollapsed) "Thu gọn" else "Hiển thị thêm"
            }
        }
    }

    // Giải phóng tài nguyên khi Fragment bị huỷ
    override fun onDestroyView() {
        super.onDestroyView()
        binding.youtubePlayerView.release()
        youTubePlayer = null
        isPlayerReady = false
    }
}
