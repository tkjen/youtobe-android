package com.tkjen.youtube.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "like_videos")
data class LikeVideo(
    @PrimaryKey val videoId: String,
    val videoTitle: String,
    val thumbnailUrl: String?,
    val duration: String,
    val channelName: String,
    val lastViewed: Long =  System.currentTimeMillis()
)
