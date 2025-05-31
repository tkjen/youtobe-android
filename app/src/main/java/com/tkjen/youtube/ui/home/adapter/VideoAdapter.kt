// VideoAdapter.kt
package com.tkjen.youtube.ui.home.adapter // Hoặc package hiện tại của bạn

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter // DiffUtil không cần import trực tiếp ở đây nữa
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tkjen.youtube.R
import com.tkjen.youtube.data.model.VideoItem
import com.tkjen.youtube.databinding.ItemVideoBinding
import com.tkjen.youtube.utils.formatDuration
import com.tkjen.youtube.utils.formatTimeAgo
import com.tkjen.youtube.utils.formatViewCount


class VideoAdapter(private val onItemClicked: ((VideoItem) -> Unit)? = null) :
    ListAdapter<VideoItem, VideoAdapter.VideoViewHolder>(VideoDiffCallback()) { // Sử dụng VideoDiffCallback đã tách

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val binding = ItemVideoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return VideoViewHolder(binding, onItemClicked)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val video = getItem(position)
        holder.bind(video)
    }

    // VideoViewHolder vẫn có thể là inner class hoặc nested class nếu bạn muốn,
    // hoặc cũng có thể tách ra file riêng nếu nó quá lớn.
    // Hiện tại để nó ở đây vẫn ổn.
    class VideoViewHolder(
        private val binding: ItemVideoBinding,
        private val onItemClicked: ((VideoItem) -> Unit)?
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
                    .load(video.snippet.thumbnails.high?.url ?: video.snippet.thumbnails.medium?.url ?: video.snippet.thumbnails.default?.url)
                    .placeholder(R.drawable.placeholder_thumbnail)
                    .error(R.drawable.placeholder_thumbnail)
                    .into(imgThumbnail)

                Glide.with(imgChannelAvatar.context)
                    .load(R.drawable.placeholder_avatar)
                    .circleCrop()
                    .into(imgChannelAvatar)
                imgMenu.setOnClickListener {

                }
                itemView.setOnClickListener {
                    onItemClicked?.invoke(video)
                }

            }
        }
    }


}