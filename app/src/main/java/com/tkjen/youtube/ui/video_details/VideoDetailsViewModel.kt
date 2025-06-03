package com.tkjen.youtube.ui.video_details

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tkjen.youtube.data.model.VideoItem
import com.tkjen.youtube.data.repository.YoutubeRepository
import com.tkjen.youtube.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VideoDetailsViewModel @Inject constructor(
    private val repository: YoutubeRepository
) : ViewModel() {

    private val _videoDetails = MutableStateFlow<Result<VideoItem>>(Result.Loading)
    val videoDetails: StateFlow<Result<VideoItem>> = _videoDetails

    fun loadVideoDetails(videoId: String) {
        Log.d("VideoDetailsViewModel", "Loading video details for ID: $videoId")
        viewModelScope.launch {
            _videoDetails.value = Result.Loading
            try {
                val response = repository.getVideoDetails(listOf(videoId))
                if (response.items.isNotEmpty()) {
                    val video = response.items[0]
                    Log.d("VideoDetailsViewModel", "Video loaded successfully: ${video.id}")
                    _videoDetails.value = Result.Success(video)
                } else {
                    Log.e("VideoDetailsViewModel", "No video found with ID: $videoId")
                    _videoDetails.value = Result.Error("Video not found")
                }
            } catch (e: Exception) {
                Log.e("VideoDetailsViewModel", "Error loading video: ${e.message}")
                _videoDetails.value = Result.Error(e.message ?: "Failed to load video details")
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        // Reset state when ViewModel is cleared
        _videoDetails.value = Result.Loading
    }
} 