package com.tkjen.youtube.ui.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.tkjen.youtube.databinding.FragmentCategoryVideosBinding
import com.tkjen.youtube.ui.home.adapter.VideoAdapter
import com.tkjen.youtube.utils.Result
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CategoryVideosFragment : Fragment() {
    private lateinit var binding: FragmentCategoryVideosBinding
    private val viewModel: CategoryVideosViewModel by viewModels()
    private lateinit var videoAdapter: VideoAdapter
    private var category: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Lấy dữ liệu category từ arguments của fragment khi nó được khởi tạo.
        //ARG_CATEGORY là key dùng để lấy giá trị chuỗi danh mục truyền vào fragment.
        arguments?.let {
            category = it.getString(ARG_CATEGORY)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCategoryVideosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeViewModel()
        loadVideos()
    }

    private fun setupRecyclerView() {
        videoAdapter = VideoAdapter()
        binding.recyclerViewVideos.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = videoAdapter
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.videos.collectLatest { result ->
                when (result) {
                    is Result.Loading -> {
                        if (videoAdapter.itemCount == 0) {
                            binding.shimmerViewContainer.visibility = View.VISIBLE
                            binding.shimmerViewContainer.startShimmer()
                            binding.recyclerViewVideos.visibility = View.GONE
                        }
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
                        Toast.makeText(requireContext(), result.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun loadVideos() {
        category?.let {
            viewModel.loadVideos(it)
        }
    }

    companion object {
        private const val ARG_CATEGORY = "category"

        fun newInstance(category: String) = CategoryVideosFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_CATEGORY, category)
            }
        }
    }
} 