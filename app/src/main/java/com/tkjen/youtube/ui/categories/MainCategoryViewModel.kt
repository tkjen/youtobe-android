package com.tkjen.youtube.ui.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tkjen.youtube.data.model.VideoItem
import com.tkjen.youtube.data.repository.YoutubeRepository
import com.tkjen.youtube.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainCategoryViewModel @Inject constructor(
    private val repository: YoutubeRepository
) : ViewModel() {

    private val _videos = MutableStateFlow<Result<List<VideoItem>>>(Result.Loading)
    val videos: StateFlow<Result<List<VideoItem>>> = _videos.asStateFlow()

    val isDataLoaded: Boolean
        get() = _videos.value is Result.Success

    private var nextPageToken: String? = null
    private val allVideos = mutableListOf<VideoItem>()
    private var isLoadingMore = false
    private var currentCategory: String? = null

    fun loadVideos(category: String) {
        if (category == currentCategory && isDataLoaded) return
        currentCategory = category

        viewModelScope.launch {
            _videos.value = Result.Loading
            try {
                // Reset pagination state for new category
                allVideos.clear()
                nextPageToken = null
                
                // Load initial videos for the category
                val items = repository.getVideosByCategory(category)
                allVideos.addAll(items)
                _videos.value = Result.Success(allVideos)
            } catch (e: Exception) {
                _videos.value = Result.Error("Failed to load videos: ${e.message}")
            }
        }
    }

    fun loadMoreVideos() {
        if (isLoadingMore || currentCategory == null) return
        isLoadingMore = true

        viewModelScope.launch {
            try {
                val response = repository.getVideosByCategoryPagination(currentCategory!!, nextPageToken)
                allVideos.addAll(response.items)
                nextPageToken = response.nextPageToken
                _videos.value = Result.Success(allVideos)
            } catch (e: Exception) {
                _videos.value = Result.Error("Load failed: ${e.message}")
            } finally {
                isLoadingMore = false
            }
        }
    }
} 