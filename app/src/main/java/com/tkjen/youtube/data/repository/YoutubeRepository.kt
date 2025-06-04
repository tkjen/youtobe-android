package com.tkjen.youtube.data.repository

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
}