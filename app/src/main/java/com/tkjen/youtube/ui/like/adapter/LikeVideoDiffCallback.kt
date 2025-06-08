package com.tkjen.youtube.ui.like.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tkjen.youtube.data.local.entity.LikeVideo

class LikeVideoDiffCallback:DiffUtil.ItemCallback<LikeVideo>() {
    override fun areItemsTheSame(oldItem: LikeVideo, newItem: LikeVideo): Boolean {
        return oldItem.videoId == newItem.videoId
    }

    override fun areContentsTheSame(oldItem: LikeVideo, newItem: LikeVideo): Boolean {
        return oldItem == newItem
    }


}