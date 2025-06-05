package com.tkjen.youtube.data.local

import com.tkjen.youtube.data.local.dao.RecentVideoDao
import com.tkjen.youtube.data.local.entity.RecentVideo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DatabaseHelperImpl @Inject constructor(

    private val appDatabase: AppDatabase,
    private val recentVideoDao: RecentVideoDao
) : DatabaseHelper  {
    override suspend fun insertRecentVideo(video: RecentVideo) {
        recentVideoDao.insert(video)
    }

    override fun getRecentVideos(): Flow<List<RecentVideo>> {
        return recentVideoDao.getRecentVideos()
    }
}