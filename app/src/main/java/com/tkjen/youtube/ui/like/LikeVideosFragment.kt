package com.tkjen.youtube.ui.like

import android.os.Bundle
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
    private val viewModel: LikeVideosViewModels by viewModels()
    private lateinit var likeVideoAdapter: LikeVideoAdapter

    @Inject
    lateinit var databaseHelper: DatabaseHelper

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
        updateUi()
        viewModel.loadLikedVideos()
    }

    private fun setupRecyclerView() {
        likeVideoAdapter = LikeVideoAdapter(
            onItemClick = { video ->
                // Xử lý click item ở đây nếu cần
            }
        )

        binding.rvLikeVideos.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = likeVideoAdapter
        }

        val itemTouchHelper = ItemTouchHelper(
            SwipeToDeleteCallback(requireContext()) { position ->
                val video = likeVideoAdapter.currentList.getOrNull(position)
                if (video != null) {
                    viewLifecycleOwner.lifecycleScope.launch {
                        databaseHelper.deleteLikeVideo(video)
                        val updatedList = likeVideoAdapter.currentList.toMutableList()
                        updatedList.removeAt(position)
                        likeVideoAdapter.submitList(updatedList)
                        Toast.makeText(context,"Remove video ${video.videoTitle}", Toast.LENGTH_SHORT).show()
                        updateUi()
                    }
                }
            }
        )
        itemTouchHelper.attachToRecyclerView(binding.rvLikeVideos)
    }

    private fun observeVideos() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.video.collectLatest { result ->
                when (result) {
                    is Result.Loading -> {
                        // TODO: show loading if needed
                    }
                    is Result.Success -> {
                        likeVideoAdapter.submitList(result.data)
                    }
                    is Result.Error -> {
                        Toast.makeText(requireContext(), result.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun updateUi() {
        binding.apply {
            viewLifecycleOwner.lifecycleScope.launch {
                val videos = databaseHelper.getLikedVideos().firstOrNull() ?: emptyList()
                if (videos.isNotEmpty()) {
                    val firstVideo = videos.first()
                    Glide.with(requireContext())
                        .load(firstVideo.thumbnailUrl)
                        .into(imgThumbnail)
                    tvDuration.text = formatDuration(firstVideo.duration)

                    cvItemFirst.setOnClickListener {
                        val videoId = firstVideo.videoId
                        val action = LikeVideosFragmentDirections.actionLikeVideosFragmentToVideoDetailsLikeFragment(videoId)
                        view?.findNavController()?.navigate(action)
                    }

                    tvVideoCount.text = "${videos.size} videos"
                } else {
                    imgThumbnail.setImageResource(R.drawable.placeholder_thumbnail)
                    tvDuration.text = ""
                    tvVideoCount.text = "No videos"
                    cvItemFirst.setOnClickListener(null)
                }
            }
        }
    }
}
