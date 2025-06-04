// VideoAdapter.kt
package com.tkjen.youtube.ui.home.adapter // Hoặc package hiện tại của bạn

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tkjen.youtube.R
import com.tkjen.youtube.data.model.VideoItem
import com.tkjen.youtube.databinding.ItemVideoBinding
import com.tkjen.youtube.utils.formatDuration
import com.tkjen.youtube.utils.formatTimeAgo
import com.tkjen.youtube.utils.formatViewCount

class VideoAdapter : ListAdapter<VideoItem, VideoAdapter.VideoViewHolder>(VideoDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val binding = ItemVideoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return VideoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val video = getItem(position)
        holder.bind(video)
    }

    // VideoViewHolder vẫn có thể là inner class hoặc nested class nếu bạn muốn,
    // hoặc cũng có thể tách ra file riêng nếu nó quá lớn.
    // Hiện tại để nó ở đây vẫn ổn.
    class VideoViewHolder(
        private val binding: ItemVideoBinding
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
                    // Handle menu click
                }

                itemView.setOnClickListener {

                    val navController = itemView.findNavController()
                    val currentDestination = navController.currentDestination

                    // Đảm bảo video.id không null
                    val videoId = video.id
                    Log.d("VideoAdapter", "Navigating to video with ID: $videoId")

                    when (currentDestination?.id) {
                        R.id.homeFragment -> {
                            val action = com.tkjen.youtube.ui.home.HomeFragmentDirections
                                .actionHomeFragmentToVideoDetailsFragment(videoId)
                            navController.navigate(action)
                        }
                        R.id.libaryFragment -> {
                            val action = com.tkjen.youtube.ui.libary.LibaryFragmentDirections
                                .actionLibaryFragmentToVideoDetailsFragment(videoId)
                            navController.navigate(action)
                        }
                        R.id.videoDetailsFragment->
                        {
                            val action = com.tkjen.youtube.ui.video_details.VideoDetailsFragmentDirections
                                .actionVideoDetailsFragmentSelf(videoId)
                            navController.navigate(action)
                        }
                        else -> {
                            // Fallback navigation if the current fragment doesn't have a specific action
                            val action = com.tkjen.youtube.ui.home.HomeFragmentDirections
                                .actionHomeFragmentToVideoDetailsFragment(videoId)
                            navController.navigate(action)
                        }
                    }
                }
            }
        }
    }
}





