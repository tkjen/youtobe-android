package com.tkjen.youtube.ui.like

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.tkjen.youtube.R
import com.tkjen.youtube.data.local.DatabaseHelper
import com.tkjen.youtube.data.local.entity.LikeVideo
import com.tkjen.youtube.data.mapper.YoutubeMapper
import com.tkjen.youtube.databinding.FragmentLikeVideosBinding
import com.tkjen.youtube.ui.like.adapter.LikeVideoAdapter
import com.tkjen.youtube.utils.Result
import com.tkjen.youtube.utils.SwipeToDeleteCallback
import com.tkjen.youtube.utils.formatDuration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LikeVideosFragment : Fragment(R.layout.fragment_like_videos) {

    private lateinit var binding: FragmentLikeVideosBinding
    private val viewModel: LikeVideosViewModel by viewModels()
    private lateinit var likeVideoAdapter: LikeVideoAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLikeVideosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeVideos()
        viewModel.loadLikedVideos()
    }

    private fun setupRecyclerView() {
        likeVideoAdapter = LikeVideoAdapter { video ->
            viewModel.insertRecentFromLike(video)
        }

        binding.rvLikeVideos.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = likeVideoAdapter
        }

        val itemTouchHelper = ItemTouchHelper(
            SwipeToDeleteCallback(requireContext()) { position ->
                likeVideoAdapter.currentList.getOrNull(position)?.let { video ->
                    viewModel.deleteLikeVideo(video)
                    Toast.makeText(context, "Removed ${video.videoTitle}", Toast.LENGTH_SHORT).show()
                }
            }
        )
        itemTouchHelper.attachToRecyclerView(binding.rvLikeVideos)
    }

    private fun observeVideos() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.videos.collectLatest { result ->
                when (result) {
                    is Result.Loading -> { /* show loading UI if needed */ }
                    is Result.Error -> { /* show error UI */ }
                    is Result.Success -> {
                        val videos = result.data
                        likeVideoAdapter.submitList(videos)
                        updateUi(videos)
                    }
                }
            }
        }
    }

    private fun updateUi(videos: List<LikeVideo>) {
        binding.apply {
            if (videos.isNotEmpty()) {
                val first = videos.first()
                Glide.with(requireContext()).load(first.thumbnailUrl).into(imgThumbnail)
                tvDuration.text = formatDuration(first.duration)
                tvVideoCount.text = "${videos.size} videos"
                cvItemFirst.setOnClickListener {
                    val action = LikeVideosFragmentDirections
                        .actionLikeVideosFragmentToVideoDetailsLikeFragment(first.videoId)
                    view?.findNavController()?.navigate(action)
                }
            } else {
                imgThumbnail.setImageResource(R.drawable.placeholder_thumbnail)
                tvDuration.text = ""
                tvVideoCount.text = "No videos"
                cvItemFirst.setOnClickListener(null)
            }
        }
    }
}
