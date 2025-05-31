package com.tkjen.youtube.ui.home.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tkjen.youtube.data.model.VideoItem

class VideoDiffCallback : DiffUtil.ItemCallback<VideoItem>() {
    override fun areItemsTheSame(oldItem: VideoItem, newItem: VideoItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: VideoItem, newItem: VideoItem): Boolean {
        // Đối với data class, so sánh mặc định thường là đủ.
        // Nếu bạn có logic phức tạp, bạn có thể so sánh từng trường.
        return oldItem == newItem
    }
}