package com.tkjen.youtube.ui.shorts

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.WindowCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView
import com.tkjen.youtube.R
import com.tkjen.youtube.data.model.VideoItem
import com.tkjen.youtube.databinding.FragmentShortsBinding
import com.tkjen.youtube.ui.shorts.adapter.ShortsAdapter
import com.tkjen.youtube.utils.Result
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ShortsFragment : Fragment(R.layout.fragment_shorts) {

    private lateinit var binding: FragmentShortsBinding

    private val viewModel: ShortsViewModel by viewModels()
    private lateinit var shortsAdapter: ShortsAdapter
    private var currentPlayingPosition = -1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentShortsBinding.bind(view)

        setupSystemBars()
        setupRecyclerView()
        observeViewModel()
    }

    private fun setupSystemBars() {
        val window = requireActivity().window
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = android.graphics.Color.TRANSPARENT
        WindowCompat.getInsetsController(window, window.decorView).apply {
            isAppearanceLightStatusBars = false
        }
    }

    private fun setupRecyclerView() {
        shortsAdapter = ShortsAdapter { video ->
            // TODO: Navigate to video details
        }

        binding.rvShorts.apply {
            adapter = shortsAdapter
            layoutManager = ShortsLayoutManager(requireContext())
            setHasFixedSize(true)
            addOnScrollListener(createScrollListener())
        }
    }

    private fun createScrollListener() = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                handleScrollStateIdle(recyclerView)
            }
        }
    }

    private fun handleScrollStateIdle(recyclerView: RecyclerView) {
        val layoutManager = recyclerView.layoutManager as ShortsLayoutManager
        val centerView = layoutManager.findFirstCompletelyVisibleItemPosition()
        
        if (centerView != currentPlayingPosition) {
            pauseCurrentVideo(recyclerView)
            playNewVideo(recyclerView, centerView)
        }
    }

    private fun pauseCurrentVideo(recyclerView: RecyclerView) {
        if (currentPlayingPosition != -1) {
            val oldHolder = recyclerView.findViewHolderForAdapterPosition(currentPlayingPosition)
            if (oldHolder is ShortsAdapter.ShortsViewHolder) {
                oldHolder.pauseVideo()
            }
        }
    }

    private fun playNewVideo(recyclerView: RecyclerView, position: Int) {
        val newHolder = recyclerView.findViewHolderForAdapterPosition(position)
        if (newHolder is ShortsAdapter.ShortsViewHolder) {
            newHolder.playVideo()
            currentPlayingPosition = position
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.shorts.collect { result ->
                    handleShortsResult(result)
                }
            }
        }
    }

    private fun handleShortsResult(result: Result<List<VideoItem>>) {
        when (result) {
            is Result.Loading -> showLoading()
            is Result.Success -> handleSuccess(result.data)
            is Result.Error -> showError(result.message)
        }
    }

    private fun showLoading() {
        binding.apply {
            progressBar.isVisible = true
            errorView.isVisible = false
            emptyView.isVisible = false
        }
    }

    private fun handleSuccess(data: List<VideoItem>) {
        binding.apply {
            progressBar.isVisible = false
            errorView.isVisible = false
            emptyView.isVisible = data.isEmpty()
        }
        shortsAdapter.submitList(data)
        
        if (data.isNotEmpty()) {
            playFirstVideo()
        }
    }

    private fun playFirstVideo() {
        currentPlayingPosition = 0
        val holder = binding.rvShorts.findViewHolderForAdapterPosition(0)
        if (holder is ShortsAdapter.ShortsViewHolder) {
            holder.playVideo()
        }
    }

    private fun showError(message: String) {
        binding.apply {
            progressBar.isVisible = false
            errorView.isVisible = true
            emptyView.isVisible = false
            tvError.text = message
        }
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onPause() {
        super.onPause()
        pauseCurrentVideo(binding.rvShorts)
    }

    override fun onResume() {
        super.onResume()
        if (currentPlayingPosition != -1) {
            val holder = binding.rvShorts.findViewHolderForAdapterPosition(currentPlayingPosition)
            if (holder is ShortsAdapter.ShortsViewHolder) {
                holder.playVideo()
            }
        }
    }

}