package com.tkjen.youtube.ui.libary.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tkjen.youtube.data.local.entity.RecentVideo
import com.tkjen.youtube.data.model.VideoItem
import com.tkjen.youtube.databinding.ItemRecentVideoBinding
import com.bumptech.glide.Glide
import com.tkjen.youtube.R
import com.tkjen.youtube.ui.libary.adapter.RecentVideoDiffCallback
import com.tkjen.youtube.utils.formatDuration

class RecentVideoAdapter(
    private val onItemClick: (RecentVideo) -> Unit
) : ListAdapter<RecentVideo, RecentVideoAdapter.RecentVideoViewHolder>(RecentVideoDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentVideoViewHolder {
        val binding = ItemRecentVideoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RecentVideoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecentVideoViewHolder, position: Int) {
        val video = getItem(position)
        holder.bind(video)
    }

    class RecentVideoViewHolder(
        private val binding: ItemRecentVideoBinding

    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(recentVideo: RecentVideo) {
            binding.apply {
                tvChannelName.text = recentVideo.channelName
                tvVideoTitle.text = recentVideo.videoTitle
                tvVideoDuration.text = formatDuration(recentVideo.duration)

                Glide.with(ivVideoThumbnail.context)
                    .load(recentVideo.thumbnailUrl)
                    .placeholder(R.drawable.placeholder_thumbnail)
                    .error(R.drawable.placeholder_thumbnail)
                    .into(ivVideoThumbnail)

                itemView.setOnClickListener {
                    val navController = itemView.findNavController()
                    val videoId = recentVideo.videoId

                    Log.d("RecentVideoAdapter", "Navigating to video with ID: $videoId")

                    // Navigate to video details
                    val action = com.tkjen.youtube.ui.libary.LibaryFragmentDirections
                        .actionLibaryFragmentToVideoDetailsFragment(videoId)
                    navController.navigate(action)
                }

            }


        }
    }
}
