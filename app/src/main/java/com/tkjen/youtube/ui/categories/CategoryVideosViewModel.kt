package com.tkjen.youtube.ui.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tkjen.youtube.data.local.DatabaseHelper
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
class CategoryVideosViewModel @Inject constructor(
    private val repository: YoutubeRepository,
) : ViewModel() {

    // State để lưu trữ và quan sát danh sách video
    private val _videos = MutableStateFlow<Result<List<VideoItem>>>(Result.Loading)
    val videos: StateFlow<Result<List<VideoItem>>> = _videos.asStateFlow()

    // Lưu category hiện tại để tránh load lại dữ liệu không cần thiết
    private var currentCategory: String? = null

    /**
     * Load danh sách video theo category
     * @param category Category cần load videos
     */
    fun loadVideos(category: String) {
        // Nếu category không đổi và đã có dữ liệu thì không load lại
        if (category == currentCategory && _videos.value is Result.Success) return
        
        currentCategory = category

        viewModelScope.launch {
            _videos.value = Result.Loading
            try {
                val items = repository.getVideosByCategory(category)
                _videos.value = Result.Success(items)
            } catch (e: Exception) {
                _videos.value = Result.Error("Failed to load videos: ${e.message}")
            }
        }
    }
} 