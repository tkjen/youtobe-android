package com.tkjen.youtube.data.local

import com.tkjen.youtube.data.local.entity.RecentVideo
import kotlinx.coroutines.flow.Flow

interface DatabaseHelper {
    suspend fun insertRecentVideo(video: RecentVideo)

    fun getRecentVideos(): Flow<List<RecentVideo>>

    // Thêm các phương thức khác nếu cần
}