package com.tkjen.youtube.repository

import android.util.Log
import com.tkjen.youtube.data.api.YoutubeApiService
import com.tkjen.youtube.data.model.VideoItem
import com.tkjen.youtube.data.model.VideoListResponse
import com.tkjen.youtube.data.model.YoutubeResponse
import javax.inject.Inject
import javax.inject.Named

class YoutubeRepository @Inject constructor(
    private val apiService: YoutubeApiService,
    @Named("youtube_api_key") private val apiKey: String
) {
    suspend fun getVideoDetails(videoIds: List<String>): YoutubeResponse {
        Log.d("YoutubeRepository", "Using API key: ${apiKey.take(5)}...")
        
        val response = apiService.getVideoDetails(
            videoIds = videoIds.joinToString(","),
            apiKey = apiKey
        )
        
        Log.d("YoutubeRepository", "Received response with ${response.items.size} items")
        return response
    }

    suspend fun getPopularVideos(): List<VideoItem> {
        val response = apiService.getPopularVideos(apiKey = apiKey)
        Log.d("YoutubeRepository", "Fetched ${response.items.size} popular videos")
        return response.items
    }

    suspend fun getPopularVideosPagination(pageToken: String?): VideoListResponse {
        return apiService.getPopularVideos(
            pageToken = pageToken,
            apiKey = apiKey
        )
    }

    suspend fun getVideosByCategory(category: String): List<VideoItem> {
        val categoryId = when (category) {
            "Music" -> "10" // Music category ID
            "Gaming" -> "20"  // Gaming category ID
            "Sports" -> "17" // Sports category ID
            "News" -> "25"  // News category ID
            else -> return getPopularVideos() // Default to popular videos
        }

        // Use the videos endpoint with category ID for better results
        val response = apiService.getPopularVideos(
            videoCategoryId = categoryId,
            apiKey = apiKey
        )
        
        Log.d("YoutubeRepository", "Fetched ${response.items.size} videos for category: $category")
        return response.items
    }

    suspend fun getChannelSubscriberCount(channelId: String): String {
        val response = apiService.getChannelStatistics(
            channelId = channelId,
            apiKey = apiKey
        )

        val count = response.items.firstOrNull()?.statistics?.subscriberCount ?: "0"
        return count
    }

    /**
     * Lấy danh sách shorts videos phổ biến
     * @return Danh sách VideoItem chứa thông tin shorts
     */
    // short videos are typically fetched using the search endpoint with a specific query
    suspend fun getPopularShorts(): List<VideoItem> {
        try {
            Log.d("YoutubeRepository", "Fetching popular shorts videos...")
            
            // Lấy danh sách shorts từ API
            val searchResponse = apiService.getShortsVideos(apiKey = apiKey)
            
            if (searchResponse.items.isEmpty()) {
                Log.d("YoutubeRepository", "No shorts videos found")
                return emptyList()
            }

            // Lấy danh sách video IDs từ kết quả tìm kiếm
            val videoIds = searchResponse.items.map { it.id.videoId }
            
            // Lấy thông tin chi tiết của các video
            val videoDetails = getVideoDetails(videoIds)
            
            Log.d("YoutubeRepository", "Fetched ${videoDetails.items.size} shorts videos")
            return videoDetails.items
        } catch (e: Exception) {
            Log.e("YoutubeRepository", "Error fetching shorts videos: ${e.message}")
            return emptyList()
        }
    }

    /**
     * Lấy danh sách shorts videos phổ biến với phân trang
     * @param pageToken Token phân trang
     * @return VideoListResponse chứa danh sách video và token phân trang tiếp theo
     */
    suspend fun getPopularShortsPagination(pageToken: String?): VideoListResponse {
        try {
            Log.d("YoutubeRepository", "Fetching popular shorts videos with page token: $pageToken")
            
            // Lấy danh sách shorts từ API
            val searchResponse = apiService.getShortsVideos(
                pageToken = pageToken,
                apiKey = apiKey
            )
            
            if (searchResponse.items.isEmpty()) {
                Log.d("YoutubeRepository", "No shorts videos found")
                return VideoListResponse(emptyList(), null)
            }

            // Lấy danh sách video IDs từ kết quả tìm kiếm
            val videoIds = searchResponse.items.map { it.id.videoId }
            
            // Lấy thông tin chi tiết của các video
            val videoDetails = getVideoDetails(videoIds)
            
            Log.d("YoutubeRepository", "Fetched ${videoDetails.items.size} shorts videos")
            return VideoListResponse(videoDetails.items, searchResponse.nextPageToken)
        } catch (e: Exception) {
            Log.e("YoutubeRepository", "Error fetching shorts videos: ${e.message}")
            return VideoListResponse(emptyList(), null)
        }
    }
}