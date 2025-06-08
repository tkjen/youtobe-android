package com.tkjen.youtube.data.local

import com.tkjen.youtube.data.local.dao.YoutubeDao
import com.tkjen.youtube.data.local.entity.LikeVideo
import com.tkjen.youtube.data.local.entity.RecentVideo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DatabaseHelperImpl @Inject constructor(

    private val appDatabase: AppDatabase,
    private val videoDao: YoutubeDao
) : DatabaseHelper  {
    override suspend fun insertRecentVideo(video: RecentVideo) {
        videoDao.insert(video)
    }

    override fun getRecentVideos(): Flow<List<RecentVideo>> {
        return videoDao.getRecentVideos()
    }

    override suspend fun insertLikeVideo(video: LikeVideo) {
        videoDao.insertLikeVideo(video)
    }

    override fun getLikedVideos(): Flow<List<LikeVideo>> {
       return videoDao.getLikedVideos()
    }

    override suspend fun isVideoLiked(videoId: String): Boolean {
        return videoDao.getLikedVideoById(videoId) != null
    }

    override suspend fun deleteLikeVideo(videoId: String) {
        videoDao.deleteLikeVideo(videoId)
    }

    override suspend fun deleteLikeVideo(video: LikeVideo) {
        videoDao.deleteLikeVideo(video)
    }

    override suspend fun getLikedVideosCount(): Int {
        return videoDao.getLikedVideosCount()
    }

    override suspend fun clearAllLikedVideos() {
        videoDao.clearAllLikedVideos()
    }

    override suspend fun toggleLikeVideo(video: LikeVideo): Boolean {
        return if (videoDao.isVideoLiked(video.videoId)) {
            videoDao.deleteLikeVideo(video.videoId)
            false // đã bỏ thích
        } else {
            videoDao.insertLikeVideo(video)
            true // đã thích
        }
    }
}