package com.tkjen.youtube.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tkjen.youtube.data.local.dao.YoutubeDao
import com.tkjen.youtube.data.local.entity.RecentVideo

@Database([RecentVideo::class], version = 2, exportSchema = false)
abstract class AppDatabase:RoomDatabase() {

    abstract fun recentVideoDao(): YoutubeDao
}