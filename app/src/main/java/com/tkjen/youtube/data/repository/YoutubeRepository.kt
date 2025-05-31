package com.tkjen.youtube.data.repository

import android.util.Log
import com.tkjen.youtube.data.api.YoutubeApiService
import com.tkjen.youtube.data.model.VideoItem
import com.tkjen.youtube.data.model.YoutubeResponse
import javax.inject.Inject
import javax.inject.Named

class YoutubeRepository @Inject constructor(
    private val apiService: YoutubeApiService,
    @Named("youtube_api_key") private val apiKey: String
) {
    suspend fun getVideoDetails(videoIds: List<String>): YoutubeResponse {
        Log.d("YoutubeRepository", "Getting video details for IDs: $videoIds")
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

}