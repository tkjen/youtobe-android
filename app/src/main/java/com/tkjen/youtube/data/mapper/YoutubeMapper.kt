package com.tkjen.youtube.data.mapper

import com.tkjen.youtube.data.local.entity.RecentVideo
import com.tkjen.youtube.data.model.VideoItem

object YoutubeMapper {

    fun toRecentVideo(videoItem: VideoItem): RecentVideo {
        return RecentVideo(
            videoId = videoItem.id,
            videoTitle = videoItem.snippet.title,
            channelName = videoItem.snippet.channelTitle,
            thumbnailUrl = videoItem.snippet.thumbnails.high?.url
                ?: videoItem.snippet.thumbnails.medium?.url
                ?: videoItem.snippet.thumbnails.default?.url,
            duration = videoItem.contentDetails?.duration ?: "PT0S",
            publishedAt = videoItem.snippet.publishedAt,
            lastViewed = System.currentTimeMillis() // nếu có trường này
        )
    }

}