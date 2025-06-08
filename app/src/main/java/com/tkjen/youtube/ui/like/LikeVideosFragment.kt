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
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.tkjen.youtube.R
import com.tkjen.youtube.data.local.DatabaseHelper
import com.tkjen.youtube.databinding.FragmentLikeVideosBinding
import com.tkjen.youtube.ui.like.adapter.LikeVideoAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.tkjen.youtube.utils.Result
import com.tkjen.youtube.utils.formatDuration
import kotlinx.coroutines.flow.firstOrNull

@AndroidEntryPoint
class LikeVideosFragment:Fragment(R.layout.fragment_like_videos) {

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
        likeVideoAdapter = LikeVideoAdapter { onItemClick ->

        }
        binding.rvLikeVideos.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = likeVideoAdapter
        }

    }

  private fun observeVideos(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.video.collectLatest { result ->
                when (result) {
                    is Result.Loading -> {
                        // Show loading state if needed
                    }
                    is Result.Success -> {
                        likeVideoAdapter.submitList(result.data)
                    }
                    is Result.Error -> {
                        // Handle error state, e.g., show a Toast
                        Toast.makeText(requireContext(), result.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }

    private fun updateUi(){
            binding.apply {
                viewLifecycleOwner.lifecycleScope.launch {

                    // Lấy danh sách video đã thích dau tien từ DatabaseHelper
                    val videos = databaseHelper.getLikedVideos().firstOrNull() ?: emptyList()
                    if (videos.isNotEmpty()) {
                        val thumbnailUrl = videos.first().thumbnailUrl

                        Glide.with(requireContext())
                            .load(thumbnailUrl)
                            .into(imgThumbnail)
                    }
                    //Duration
                    val durationVideo = formatDuration(videos.first().duration)
                    tvDuration.text = durationVideo
                    // Phat video dau tien
                    cvItemFirst.setOnClickListener {
                        if (videos.isNotEmpty()) {
                            val videoId = videos.first().videoId
                            val action = LikeVideosFragmentDirections
                                .actionLikeVideosFragmentToVideoDetailsFragment(videoId)
                            view?.findNavController()?.navigate(action)
                        } else {
                            Toast.makeText(requireContext(), "Không có video nào", Toast.LENGTH_SHORT).show()
                        }
                    }
                    // Hiển thị số lượng video đã thích
                    val likedVideosCount = videos.size.toString()
                    tvVideoCount.text = "${likedVideosCount} videos"
                }


            }
    }
}