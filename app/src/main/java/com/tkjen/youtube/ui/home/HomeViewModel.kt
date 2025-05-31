package com.tkjen.youtube.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.facebook.shimmer.BuildConfig
import com.tkjen.youtube.data.model.VideoItem
import com.tkjen.youtube.data.repository.YoutubeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.tkjen.youtube.utils.Result
import kotlinx.coroutines.delay

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: YoutubeRepository
): ViewModel() {

    private val _videos = MutableStateFlow<Result<List<VideoItem>>>(Result.Loading)
    val videos: StateFlow<Result<List<VideoItem>>> = _videos.asStateFlow()

    val isDataLoaded: Boolean
        get() = _videos.value is Result.Success

    fun loadVideos(videoIds: List<String>) {
        if (isDataLoaded) return

        viewModelScope.launch {
            _videos.value = Result.Loading
            try {
                val response = repository.getVideoDetails(videoIds)
                _videos.value = Result.Success(response.items)
            } catch (e: Exception) {
                _videos.value = Result.Error("Failed to load videos: ${e.message}")
            }
        }
    }

    fun loadPopularVideos() {
        if (isDataLoaded) return

        viewModelScope.launch {
            _videos.value = Result.Loading
            delay(1000)
            try {
                val items = repository.getPopularVideos()
                _videos.value = Result.Success(items)
            } catch (e: Exception) {
                _videos.value = Result.Error("Failed to load videos: ${e.message}")
            }
        }
    }
}
