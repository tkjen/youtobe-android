package com.tkjen.youtube.data.model

data class YoutubeResponse(
    val items: List<VideoItem>,
    val pageInfo: PageInfo
)

data class VideoItem(
    val id: String,
    val snippet: Snippet, // Thường snippet luôn có
    val contentDetails: ContentDetails?, // Cho phép null
    val statistics: Statistics? // Cho phép null
)


data class Snippet(
    val publishedAt: String,
    val channelId: String,
    val title: String,
    val description: String,
    val thumbnails: Thumbnails,
    val channelTitle: String
)
data class VideoListResponse(
    val items: List<VideoItem>
)


data class Thumbnails(
    val default: Thumbnail?, // Cho phép null nếu có khả năng thiếu
    val medium: Thumbnail?,
    val high: Thumbnail?
    // val standard: Thumbnail?,
    // val maxres: Thumbnail?
)

data class Thumbnail(
    val url: String,
    val width: Int,
    val height: Int
)

data class ContentDetails(
    val duration: String
)

data class Statistics(
    val viewCount: String?, // Cho phép null
    val likeCount: String?,
    val commentCount: String?
)

data class PageInfo(
    val totalResults: Int,
    val resultsPerPage: Int
)
