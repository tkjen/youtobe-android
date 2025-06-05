package com.tkjen.youtube.ui.libary

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tkjen.youtube.data.local.DatabaseHelper
import com.tkjen.youtube.data.local.entity.RecentVideo
import com.tkjen.youtube.data.model.VideoItem
import com.tkjen.youtube.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class LibaryViewModels @Inject constructor(
    private val databaseHelper: DatabaseHelper
):ViewModel() {
            private val _video = MutableStateFlow<Result<List<RecentVideo>>>(Result.Loading)
            val video: StateFlow<Result<List<RecentVideo>>> = _video.asStateFlow()

    fun loadRecentVideos() {
        viewModelScope.launch {
            _video.value = Result.Loading
            databaseHelper.getRecentVideos().collect { videos ->
                if (videos.isEmpty()) {
                    _video.value = Result.Error("No recent videos found")
                } else {
                    _video.value = Result.Success(videos)
                }
            }

        }

    }
}