package com.tkjen.youtube.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.time.Duration

@Entity(tableName = "recent_videos")
data class RecentVideo(
    @PrimaryKey val videoId: String,
    val videoTitle: String,
    val thumbnailUrl: String?,
    val publishedAt: String?,
    val duration: String,
    val channelName: String,
    val lastViewed: Long =  System.currentTimeMillis()// timestamp để sort theo mới nhất
)
