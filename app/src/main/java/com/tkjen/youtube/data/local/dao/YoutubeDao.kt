package com.tkjen.youtube.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tkjen.youtube.data.local.entity.RecentVideo
import kotlinx.coroutines.flow.Flow

@Dao
interface YoutubeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(video: RecentVideo)

    @Query("SELECT * FROM recent_videos ORDER BY lastViewed DESC LIMIT 20")
    fun getRecentVideos(): Flow<List<RecentVideo>>


}
