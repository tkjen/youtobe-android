package com.tkjen.youtube.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.tkjen.youtube.R
import com.tkjen.youtube.databinding.FragmentHomeBinding
import com.tkjen.youtube.ui.home.adapter.VideoAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import com.tkjen.youtube.utils.Result
@AndroidEntryPoint
class HomeFragment: Fragment(R.layout.fragment_home) {

    private lateinit var binding: FragmentHomeBinding
    private val videoAdapter = VideoAdapter()
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        eventClick()
        loadPopularVideos()

    }

    private fun eventClick() {
        binding.icProfile.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_settingsFragment)
        }
    }

    fun loadPopularVideos() {
        binding.recyclerViewVideos.adapter = videoAdapter
        binding.recyclerViewVideos.layoutManager = LinearLayoutManager(requireContext())
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.videos.collectLatest { result ->
                when (result) {
                    is Result.Loading -> {
                        binding.shimmerViewContainer.visibility = View.VISIBLE
                        binding.shimmerViewContainer.startShimmer()
                        binding.recyclerViewVideos.visibility = View.GONE
                    }

                    is Result.Success -> {
                        binding.shimmerViewContainer.stopShimmer()
                        binding.shimmerViewContainer.visibility = View.GONE
                        binding.recyclerViewVideos.visibility = View.VISIBLE
                        videoAdapter.submitList(result.data)
                    }

                    is Result.Error -> {
                        binding.shimmerViewContainer.stopShimmer()
                        binding.shimmerViewContainer.visibility = View.GONE
                        binding.recyclerViewVideos.visibility = View.VISIBLE
                    }
                }
            }
        }
        viewModel.loadPopularVideos()
    }

    fun loadSample()
    {
        val sampleVideoIds = listOf(
            "dQw4w9WgXcQ",  // Sử dụng ID video hợp lệ để test
            "jNQXAC9IVRw",
            "kJQP7kiw5Fk"
        )
         viewModel.loadVideos(sampleVideoIds)
    }

}


