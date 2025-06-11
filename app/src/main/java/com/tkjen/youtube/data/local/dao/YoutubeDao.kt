package com.tkjen.youtube.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tkjen.youtube.data.local.entity.LikeVideo
import com.tkjen.youtube.data.local.entity.RecentVideo
import kotlinx.coroutines.flow.Flow

@Dao
interface YoutubeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(video: RecentVideo)

    @Query("SELECT * FROM recent_videos ORDER BY lastViewed DESC LIMIT 20")
    fun getRecentVideos(): Flow<List<RecentVideo>>

    // Like Videos
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLikeVideo(video: LikeVideo)

    @Query("SELECT * FROM like_videos ORDER BY lastViewed DESC")
    fun getLikedVideos(): Flow<List<LikeVideo>>

    @Query("SELECT EXISTS(SELECT 1 FROM like_videos WHERE videoId = :videoId)")
    suspend fun isVideoLiked(videoId: String): Boolean

    @Query("SELECT * FROM like_videos WHERE videoId = :videoId LIMIT 1")
    suspend fun getLikedVideoById(videoId: String): LikeVideo?

    @Query("SELECT * FROM recent_videos WHERE videoId = :videoId LIMIT 1")
    suspend fun getVideoById(videoId: String): RecentVideo?
    @Query("DELETE FROM like_videos WHERE videoId = :videoId")
    suspend fun deleteLikeVideo(videoId: String)

    @Delete
    suspend fun deleteLikeVideo(video: LikeVideo)

    @Query("SELECT COUNT(*) FROM like_videos")
    suspend fun getLikedVideosCount(): Int

    @Query("DELETE FROM like_videos")
    suspend fun clearAllLikedVideos()
    @Delete
    suspend fun deleteVideoRecent(video: RecentVideo)

}
