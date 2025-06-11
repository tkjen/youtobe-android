package com.tkjen.youtube.ui.home.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tkjen.youtube.R
import com.tkjen.youtube.data.model.VideoItem
import com.tkjen.youtube.databinding.ItemVideoBinding
import com.tkjen.youtube.utils.formatDuration
import com.tkjen.youtube.utils.formatTimeAgo
import com.tkjen.youtube.utils.formatViewCount

class VideoAdapter(
    private val onVideoClicked: (VideoItem) -> Unit // 👈 Callback truyền từ Fragment
) : ListAdapter<VideoItem, VideoAdapter.VideoViewHolder>(VideoDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val binding = ItemVideoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return VideoViewHolder(binding, onVideoClicked)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val video = getItem(position)
        holder.bind(video)
    }

    class VideoViewHolder(
        private val binding: ItemVideoBinding,
        private val onVideoClicked: (VideoItem) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(video: VideoItem) {
            binding.apply {
                tvTitle.text = video.snippet.title
                tvChannelName.text = video.snippet.channelTitle

                val viewCountText = formatViewCount(video.statistics?.viewCount)
                val timeAgoText = formatTimeAgo(video.snippet.publishedAt)
                tvViewsTime.text = "$viewCountText lượt xem • $timeAgoText"

                val durationText = formatDuration(video.contentDetails?.duration)
                tvDuration.text = durationText

                Glide.with(imgThumbnail.context)
                    .load(video.snippet.thumbnails.high?.url
                        ?: video.snippet.thumbnails.medium?.url
                        ?: video.snippet.thumbnails.default?.url)
                    .placeholder(R.drawable.placeholder_thumbnail)
                    .error(R.drawable.placeholder_thumbnail)
                    .into(imgThumbnail)

                Glide.with(imgChannelAvatar.context)
                    .load(R.drawable.placeholder_avatar)
                    .circleCrop()
                    .into(imgChannelAvatar)

                imgMenu.setOnClickListener {
                    // Handle menu click if needed
                }

                itemView.setOnClickListener {
                    onVideoClicked(video)


                }
            }
        }
    }
}


