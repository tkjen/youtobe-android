package com.tkjen.youtube.data.local

import com.tkjen.youtube.data.local.entity.LikeVideo
import com.tkjen.youtube.data.local.entity.RecentVideo
import kotlinx.coroutines.flow.Flow

interface DatabaseHelper {
    suspend fun insertRecentVideo(video: RecentVideo)

    fun getRecentVideos(): Flow<List<RecentVideo>>

    // Thêm các phương thức khác nếu cần

    // Like Videos
    suspend fun insertLikeVideo(video: LikeVideo)
    fun getLikedVideos(): Flow<List<LikeVideo>>
    suspend fun isVideoLiked(videoId: String): Boolean
    suspend fun deleteLikeVideo(videoId: String)
    suspend fun deleteLikeVideo(video: LikeVideo)
    suspend fun getLikedVideosCount(): Int
    suspend fun clearAllLikedVideos()

    // Toggle like (convenience method)
    suspend fun toggleLikeVideo(video: LikeVideo): Boolean // returns true if liked, false if unliked
}