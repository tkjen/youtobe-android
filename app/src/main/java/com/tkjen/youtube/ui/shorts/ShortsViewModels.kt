package com.tkjen.youtube.ui.shorts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tkjen.youtube.data.model.VideoItem
import com.tkjen.youtube.repository.YoutubeRepository
import com.tkjen.youtube.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShortsViewModel @Inject constructor(
    private val repository: YoutubeRepository
) : ViewModel() {

    // State cho danh sách shorts
    private val _shorts = MutableStateFlow<Result<List<VideoItem>>>(Result.Loading)
    val shorts: StateFlow<Result<List<VideoItem>>> = _shorts.asStateFlow()

    // State cho token phân trang
    private var nextPageToken: String? = null
    private var isLoading = false

    init {
        loadShorts()
    }

    /**
     * Load danh sách shorts
     */
    fun loadShorts() {
        if (isLoading) return
        isLoading = true

        viewModelScope.launch {
            try {
                val response = repository.getPopularShortsPagination(nextPageToken)
                nextPageToken = response.nextPageToken
                _shorts.value = Result.Success(response.items)
            } catch (e: Exception) {
                _shorts.value = Result.Error(e.message ?: "Failed to load shorts")
            } finally {
                isLoading = false
            }
        }
    }

    /**
     * Load thêm shorts khi scroll
     */
    fun loadMoreShorts() {
        if (isLoading || nextPageToken == null) return
        loadShorts()
    }

    /**
     * Refresh danh sách shorts
     */
    fun refreshShorts() {
        nextPageToken = null
        _shorts.value = Result.Loading
        loadShorts()
    }
}