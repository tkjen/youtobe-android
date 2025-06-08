package com.tkjen.youtube.ui.like

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.tkjen.youtube.R
import com.tkjen.youtube.data.local.DatabaseHelper
import com.tkjen.youtube.databinding.FragmentLikeVideosBinding
import com.tkjen.youtube.ui.like.adapter.LikeVideoAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.tkjen.youtube.utils.Result

@AndroidEntryPoint
class LikeVideosFragment:Fragment(R.layout.fragment_like_videos) {

    private lateinit var binding: FragmentLikeVideosBinding
    private val viewModel: LikeVideosViewModels by viewModels()
    private lateinit var likeVideoAdapter: LikeVideoAdapter

    @Inject
    lateinit var databaseHelper: DatabaseHelper
    override fun onCreateView(
        inflater: android.view.LayoutInflater,
        container: android.view.ViewGroup?,
        savedInstanceState: android.os.Bundle?
    ): View {
        binding = FragmentLikeVideosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeVideos()

        // Load dữ liệu liked videos
        viewModel.loadLikedVideos()
    }

 private fun setupRecyclerView() {
        likeVideoAdapter = LikeVideoAdapter { onItemClick ->
            // Handle item click if needed
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
}