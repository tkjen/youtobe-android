package com.tkjen.youtube.ui.shorts.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.tkjen.youtube.R
import com.tkjen.youtube.data.model.VideoItem
import com.tkjen.youtube.databinding.ItemShortsBinding
import com.tkjen.youtube.utils.formatViewCount

/**
 * Adapter cho RecyclerView hiển thị danh sách Shorts
 * - Quản lý việc phát/dừng video
 * - Xử lý UI khi video đang phát/kết thúc
 */
class ShortsAdapter(
    private val onItemClick: (VideoItem) -> Unit
) : ListAdapter<VideoItem, ShortsAdapter.ShortsViewHolder>(ShortsDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShortsViewHolder {
        val binding = ItemShortsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ShortsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShortsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ShortsViewHolder(
        private val binding: ItemShortsBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private var isPlaying = false
        private var videoId: String? = null
        private var youTubePlayer: YouTubePlayer? = null

        init {
            setupClickListeners()
            setupYouTubePlayer()
        }

        private fun setupClickListeners() {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(getItem(position))
                }
            }
        }

        private fun setupYouTubePlayer() {
            binding.webView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    this@ShortsViewHolder.youTubePlayer = youTubePlayer
                    setupPlayerStateListener(youTubePlayer)
                }
            })
        }

        private fun setupPlayerStateListener(player: YouTubePlayer) {
            player.addListener(object : AbstractYouTubePlayerListener() {
                override fun onStateChange(
                    youTubePlayer: YouTubePlayer,
                    state: PlayerConstants.PlayerState
                ) {
                    when (state) {
                        PlayerConstants.PlayerState.PLAYING -> {
                            isPlaying = true
                            binding.ivPlay.isVisible = false
                        }
                        PlayerConstants.PlayerState.ENDED -> {
                            youTubePlayer.seekTo(0f)
                            youTubePlayer.play()
                        }
                        else -> {}
                    }
                }
            })
        }

        fun bind(video: VideoItem) {
            videoId = video.id
            binding.apply {
                loadThumbnail(video)
                setVideoInfo(video)
                updatePlayButtonVisibility()
            }
        }

        private fun ItemShortsBinding.loadThumbnail(video: VideoItem) {
            Glide.with(ivThumbnail)
                .load(video.snippet.thumbnails.high?.url)
                .centerCrop()
                .into(ivThumbnail)

            Glide.with(imgChannelAvatar.context)
                .load(video.snippet.thumbnails.high?.url)
                .placeholder(R.drawable.placeholder_avatar)
                .circleCrop()
                .into(imgChannelAvatar)
        }

        private fun ItemShortsBinding.setVideoInfo(video: VideoItem) {
            tvTitle.text = video.snippet.title
            tvChannelTitle.text = video.snippet.channelTitle
            tvViewCount.text = formatViewCount(video.statistics?.viewCount)
        }

        private fun ItemShortsBinding.updatePlayButtonVisibility() {
            ivPlay.isVisible = !isPlaying
        }

        fun playVideo() {
            if (!isPlaying && videoId != null) {
                showVideoPlayer()
                youTubePlayer?.loadVideo(videoId!!, 0f)
            }
        }

        fun pauseVideo() {
            if (isPlaying) {
                isPlaying = false
                showThumbnail()
                youTubePlayer?.pause()
            }
        }

        private fun showVideoPlayer() {
            binding.apply {
                ivThumbnail.isVisible = false
                gradientOverlay.isVisible = false
                webView.isVisible = true
            }
        }

        private fun showThumbnail() {
            binding.apply {
                ivPlay.isVisible = true
                ivThumbnail.isVisible = true
                gradientOverlay.isVisible = true
                webView.isVisible = false
            }
        }
    }

    private class ShortsDiffCallback : DiffUtil.ItemCallback<VideoItem>() {
        override fun areItemsTheSame(oldItem: VideoItem, newItem: VideoItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: VideoItem, newItem: VideoItem): Boolean {
            return oldItem == newItem
        }
    }
}