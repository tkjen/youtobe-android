package com.tkjen.youtube.ui.like

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tkjen.youtube.data.local.DatabaseHelper
import com.tkjen.youtube.data.local.entity.LikeVideo
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
class VideoDetailsLikeViewModels @Inject constructor(
    private val repository: YoutubeRepository,
    private val databaseHelper: DatabaseHelper
) : ViewModel() {

    // State cho thông tin chi tiết video
    private val _videoDetails = MutableStateFlow<Result<VideoItem>>(Result.Loading)
    val videoDetails: StateFlow<Result<VideoItem>> = _videoDetails.asStateFlow()

    // State cho danh sách video liên quan
    private val _videos = MutableStateFlow<Result<List<VideoItem>>>(Result.Loading)
    val videos: StateFlow<Result<List<VideoItem>>> = _videos

    // State cho số lượng subscriber của kênh
    private val _subscriberCount = MutableStateFlow("0")
    val subscriberCount: StateFlow<String> = _subscriberCount.asStateFlow()
    // State cho danh sách video đã thích
    private val _videoLike = MutableStateFlow<Result<List<LikeVideo>>>(Result.Loading)
    val videoLike: StateFlow<Result<List<LikeVideo>>> = _videoLike.asStateFlow()
    // Lưu channel ID và video ID hiện tại
    private var currentChannelId: String? = null
    private var currentVideoId: String? = null



    /**
     * Load thông tin chi tiết của video và số lượng subscriber của kênh
     */
    fun loadVideoDetails(videoId: String) {
        currentVideoId = videoId
        viewModelScope.launch {
            _videoDetails.value = Result.Loading
            try {
                val response = repository.getVideoDetails(listOf(videoId))

                if (response.items.isNotEmpty()) {
                    val video = response.items[0]
                    _videoDetails.value = Result.Success(video)

                    // Lưu channel ID và load số lượng subscriber
                    currentChannelId = video.snippet.channelId
                    val subs = repository.getChannelSubscriberCount(currentChannelId!!)
                    _subscriberCount.value = subs

                    // Load video liên quan theo category
                    val categoryId = video.snippet.categoryId
                    if (categoryId != null) {
                        loadRelatedVideosByCategory(categoryId)
                    } else {
                        // Nếu không có category, load video phổ biến
                        loadPopularVideos()
                    }
                } else {
                    _videoDetails.value = Result.Error("Video not found")
                }
            } catch (e: Exception) {
                _videoDetails.value = Result.Error(e.message ?: "Failed to load video details")
            }
        }
    }

    /**
     * Load danh sách video liên quan dựa trên category
     */
    private fun loadRelatedVideosByCategory(categoryId: String) {
        viewModelScope.launch {
            _videos.value = Result.Loading
            try {
                Log.d("VideoDetailsViewModel", "Loading related videos by category: $categoryId")
                // Lấy danh sách video theo category
                val category = when (categoryId) {
                    "10" -> "Music"
                    "20" -> "Gaming"
                    "22" -> "People & Blogs"
                    "24" -> "Entertainment"
                    "25" -> "News & Politics"
                    "27" -> "Education"
                    "28" -> "Science & Technology"
                    "17" -> "Sports"
                    else -> null
                }


                if (category != null) {
                    // Lấy video theo category và lọc bỏ video hiện tại
                    val relatedVideos = repository.getVideosByCategory(category)
                        .filter { it.id != currentVideoId }
                        .take(10)

                    Log.d("VideoDetailsViewModel", "Found ${relatedVideos.size} related videos in category: $category")
                    _videos.value = Result.Success(relatedVideos)
                } else {
                    Log.d("VideoDetailsViewModel", "No category found for ID: $categoryId, loading popular videos")
                    loadPopularVideos()
                }
            } catch (e: Exception) {
                Log.e("VideoDetailsViewModel", "Error loading related videos: ${e.message}")
                loadPopularVideos()
            }
        }
    }
    fun getNextVideoTitle(): String? {
        val currentList = (_videoLike.value as? Result.Success)?.data ?: return null
        val currentIndex = currentList.indexOfFirst { it.videoId == currentVideoId }

        return if (currentIndex != -1 && currentIndex + 1 < currentList.size) {
            currentList[currentIndex + 1].videoTitle
        } else {
            null // Không có video tiếp theo
        }
    }
    fun getCurrentVideoIndex(): Int {
        // Bước 1: Lấy danh sách các video đã thích, nếu không có thì trả -1
        val likedVideos = (_videoLike.value as? Result.Success)?.data ?: return -1

        // Bước 2: Tìm vị trí (đếm từ 0) của video hiện tại trong danh sách
        val zeroBasedIndex = likedVideos.indexOfFirst { it.videoId == currentVideoId }

        // Bước 3: Nếu tìm được (>= 0), chuyển sang vị trí đếm từ 1 để người dùng dễ hiểu
        return if (zeroBasedIndex >= 0) {
            zeroBasedIndex + 1
        } else {
            // Nếu không tìm thấy, trả về -1
            -1
        }
    }


    private fun loadPopularVideos() {
        viewModelScope.launch {
            try {
                val popularVideos = repository.getPopularVideos()
                    .filter { it.id != currentVideoId }
                    .take(10)
                Log.d("VideoDetailsViewModel", "Loaded ${popularVideos.size} popular videos")
                _videos.value = Result.Success(popularVideos)
            } catch (e: Exception) {
                Log.e("VideoDetailsViewModel", "Error loading popular videos: ${e.message}")
                _videos.value = Result.Error("Failed to load popular videos: ${e.message}")
            }
        }
    }
    fun loadLikedVideos() {
        viewModelScope.launch {
            _videoLike.value = Result.Loading
            databaseHelper.getLikedVideos().collect { videos ->
                if (videos.isEmpty()) {
                    _videoLike.value = Result.Error("No liked videos found")
                } else {
                    _videoLike.value = Result.Success(videos)
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        _videoDetails.value = Result.Loading
        _videos.value = Result.Loading
        _subscriberCount.value = "0"
        currentChannelId = null
        currentVideoId = null
    }
}