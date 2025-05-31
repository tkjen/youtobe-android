package com.tkjen.youtube.utils

import android.util.Log
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit


fun formatViewCount(viewCount: String?): String { // Chấp nhận nullable String
    val count = viewCount?.toLongOrNull() ?: 0L // Xử lý null và parse an toàn
    return when {
        count >= 1_000_000_000 -> String.format(Locale.US, "%.1fB", count / 1_000_000_000.0).replace(".0B", "B")
        count >= 1_000_000 -> String.format(Locale.US, "%.1fM", count / 1_000_000.0).replace(".0M", "M")
        count >= 1_000 -> String.format(Locale.US, "%.1fK", count / 1_000.0).replace(".0K", "K")
        else -> count.toString()
    }
}

fun formatTimeAgo(publishedAt: String?): String {
    if (publishedAt.isNullOrBlank()) return "" // Xử lý null hoặc rỗng

    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
    inputFormat.timeZone = TimeZone.getTimeZone("UTC")

    try {
        val publishDate = inputFormat.parse(publishedAt) ?: return ""
        val now = Calendar.getInstance().time

        val diffInMillis = now.time - publishDate.time

        val seconds = TimeUnit.MILLISECONDS.toSeconds(diffInMillis)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillis)
        val hours = TimeUnit.MILLISECONDS.toHours(diffInMillis)
        val days = TimeUnit.MILLISECONDS.toDays(diffInMillis)

        return when {
            days > 365 -> "${days / 365} năm trước"
            days > 30 -> "${days / 30} tháng trước"
            days > 6 -> "${days / 7} tuần trước" // Thêm tuần
            days > 0 -> "$days ngày trước"
            hours > 0 -> "$hours giờ trước"
            minutes > 0 -> "$minutes phút trước"
            seconds > 0 -> "$seconds giây trước" // Thêm giây
            else -> "Vừa xong"
        }
    } catch (e: Exception) {
        Log.e("FormatUtils", "Error parsing date: $publishedAt", e) // Sửa tag log
        return "" // Trả về rỗng nếu có lỗi
    }
}

fun formatDuration(duration: String?): String {
    if (duration.isNullOrBlank() || !duration.startsWith("PT")) return "0:00"

    try {
        val cleanDuration = duration.substring(2) // Bỏ "PT"
        var hours: Long = 0
        var minutes: Long = 0
        var seconds: Long = 0
        var temp = cleanDuration

        if (temp.contains("H")) {
            val hIndex = temp.indexOf("H")
            hours = temp.substring(0, hIndex).toLongOrNull() ?: 0
            temp = temp.substring(hIndex + 1)
        }
        if (temp.contains("M")) {
            val mIndex = temp.indexOf("M")
            minutes = temp.substring(0, mIndex).toLongOrNull() ?: 0
            temp = temp.substring(mIndex + 1)
        }
        if (temp.contains("S")) {
            val sIndex = temp.indexOf("S")
            seconds = temp.substring(0, sIndex).toLongOrNull() ?: 0
        }

        return if (hours > 0) {
            String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, seconds)
        } else {
            String.format(Locale.getDefault(), "%d:%02d", minutes, seconds)
        }
    } catch (e: Exception) {
        Log.e("FormatUtils", "Error parsing ISO 8601 duration: $duration", e) // Sửa tag log
        return "0:00"
    }
}