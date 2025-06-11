package com.tkjen.youtube.ui.like

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tkjen.youtube.data.local.DatabaseHelper
import com.tkjen.youtube.data.local.entity.LikeVideo
import com.tkjen.youtube.data.mapper.YoutubeMapper
import com.tkjen.youtube.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LikeVideosViewModels @Inject constructor(
    private val databaseHelper: DatabaseHelper
) : ViewModel() {

    private val _videos = MutableStateFlow<Result<List<LikeVideo>>>(Result.Loading)
    val videos: StateFlow<Result<List<LikeVideo>>> = _videos.asStateFlow()

    fun loadLikedVideos() {
        viewModelScope.launch {
            databaseHelper.getLikedVideos().collect { videos ->
                _videos.value = if (videos.isEmpty()) {
                    Result.Error("No liked videos found")
                } else {
                    Result.Success(videos)
                }
            }
        }
    }

    fun deleteLikeVideo(video: LikeVideo) {
        viewModelScope.launch {
            databaseHelper.deleteLikeVideo(video)
        }
    }

    fun insertRecentFromLike(video: LikeVideo) {
        viewModelScope.launch {
            val recent = YoutubeMapper.toRecentVideo(video)
            databaseHelper.insertRecentVideo(recent)
        }
    }
}
