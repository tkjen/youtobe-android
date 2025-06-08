package com.tkjen.youtube.ui.like

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tkjen.youtube.data.local.DatabaseHelper
import com.tkjen.youtube.data.local.entity.LikeVideo
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
):ViewModel() {
    private val _video = MutableStateFlow<Result<List<LikeVideo>>>(Result.Loading)
    val video: StateFlow<Result<List<LikeVideo>>> = _video.asStateFlow()

    fun loadLikedVideos() {

        viewModelScope.launch {
        _video.value = Result.Loading
            databaseHelper.getLikedVideos().collect { videos ->
                if (videos.isEmpty()) {
                    _video.value = Result.Error("No liked videos found")
                } else {
                    _video.value = Result.Success(videos)
                }
            }
        }

    }
}