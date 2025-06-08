package com.tkjen.youtube.ui.libary

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import com.tkjen.youtube.utils.Result
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.tkjen.youtube.R
import com.tkjen.youtube.data.local.DatabaseHelper
import com.tkjen.youtube.data.mapper.YoutubeMapper
import com.tkjen.youtube.databinding.FragmentLibaryBinding
import com.tkjen.youtube.ui.libary.adapter.RecentVideoAdapter

import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LibaryFragment: Fragment(R.layout.fragment_libary) {

        private lateinit var binding : FragmentLibaryBinding
        private lateinit var recentVideoAdapter: RecentVideoAdapter
        private val viewModel: LibaryViewModels by viewModels()
    @Inject
    lateinit var  databaseHelper: DatabaseHelper
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLibaryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeVideos()
        updateUI()
        // Load dữ liệu recent videos
        viewModel.loadRecentVideos()

        binding.lnLikedVideos.setOnClickListener {
            findNavController().navigate(R.id.action_libaryFragment_to_likeVideosFragment)
        }
    }

    private fun setupRecyclerView() {
        recentVideoAdapter = RecentVideoAdapter{ onItemClick->

        }
        binding.rvRecentVideos.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = recentVideoAdapter
        }
    }

    private fun observeVideos() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.video.collectLatest { result ->
                when(result) {
                    is Result.Loading -> {

                    }
                    is Result.Success -> {
                        recentVideoAdapter.submitList(result.data)
                    }
                    is Result.Error -> {
                        Toast.makeText(requireContext(), result.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun updateUI() {
        binding.apply {
            viewLifecycleOwner.lifecycleScope.launch {
//                val count = databaseHelper.getLikedVideosCount()
//                tvLikeCount.text = "${count} videos"

                val videos = databaseHelper.getLikedVideos().firstOrNull() ?: emptyList()
                binding.tvLikeCount.text = "${videos.size} videos"
                if (videos.isNotEmpty()) {
                    val thumbnailUrl = videos.first().thumbnailUrl

                    Glide.with(requireContext())
                        .load(thumbnailUrl)
                        .into(imgThumbnailLikedVideos)
                }


            }

        }
    }


}