package com.tkjen.youtube.data.api

import com.tkjen.youtube.data.model.ChannelResponse
import com.tkjen.youtube.data.model.VideoListResponse
import retrofit2.http.GET
import retrofit2.http.Query
import com.tkjen.youtube.data.model.YoutubeResponse
import com.tkjen.youtube.data.model.SearchResponse
import com.tkjen.youtube.data.model.Statistics

//Giao tiếp với API YouTube
interface YoutubeApiService {

    @GET("videos")
    suspend fun getVideoDetails(
        @Query("part") part: String = "contentDetails,statistics,snippet",
        @Query("id") videoIds: String,  // danh sách id video, phân tách bằng dấu phẩy
        @Query("key") apiKey: String
    ): YoutubeResponse

    @GET("videos")
    suspend fun getPopularVideos(
        @Query("part") part: String = "snippet,contentDetails,statistics",
        @Query("chart") chart: String = "mostPopular",
        @Query("regionCode") regionCode: String = "VN",
        @Query("maxResults") maxResults: Int = 20,
        @Query("pageToken") pageToken: String? = null,
        @Query("videoCategoryId") videoCategoryId: String? = null,
        @Query("key") apiKey: String
    ): VideoListResponse

    @GET("search")
    suspend fun searchVideosByCategory(
        @Query("part") part: String = "snippet",
        @Query("type") type: String = "video",
        @Query("maxResults") maxResults: Int = 20,
        @Query("pageToken") pageToken: String? = null,
        @Query("key") apiKey: String
    ): SearchResponse

    @GET("channels")
    suspend fun getChannelStatistics(
        @Query("part") part: String = "statistics",
        @Query("id") channelId: String,
        @Query("key") apiKey: String
    ): ChannelResponse


}
