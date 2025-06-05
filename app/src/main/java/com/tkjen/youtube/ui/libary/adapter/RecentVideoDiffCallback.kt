package com.tkjen.youtube.ui.libary.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tkjen.youtube.data.local.entity.RecentVideo
import com.tkjen.youtube.data.model.VideoItem

class RecentVideoDiffCallback : DiffUtil.ItemCallback<RecentVideo>() {
    override fun areItemsTheSame(oldItem: RecentVideo, newItem: RecentVideo): Boolean {
        return oldItem.videoId == newItem.videoId
    }

    override fun areContentsTheSame(oldItem: RecentVideo, newItem: RecentVideo): Boolean {
        return oldItem == newItem
    }


}