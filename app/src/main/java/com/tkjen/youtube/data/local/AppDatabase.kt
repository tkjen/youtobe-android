package com.tkjen.youtube.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tkjen.youtube.data.local.dao.YoutubeDao
import com.tkjen.youtube.data.local.entity.LikeVideo
import com.tkjen.youtube.data.local.entity.RecentVideo

@Database([RecentVideo::class, LikeVideo::class], version = 3, exportSchema = false)
abstract class AppDatabase:RoomDatabase() {

    abstract fun youtubeDao(): YoutubeDao
}