package com.tkjen.youtube.ui.like.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tkjen.youtube.R
import com.tkjen.youtube.data.local.entity.LikeVideo
import com.tkjen.youtube.databinding.ItemLikeVideoBinding
import com.tkjen.youtube.ui.like.VideoDetailsLikeFragment
import com.tkjen.youtube.ui.like.VideoDetailsLikeFragmentArgs
import com.tkjen.youtube.utils.formatDuration
import com.tkjen.youtube.utils.formatTimeAgo
import com.tkjen.youtube.utils.formatViewCount

class LikeVideoAdapter(private val onItemClick:(LikeVideo) ->Unit):
    ListAdapter<LikeVideo,LikeVideoAdapter.LikeVideoViewHolder>(LikeVideoDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LikeVideoViewHolder {
        val binding = ItemLikeVideoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return LikeVideoViewHolder(binding, onItemClick) // TRUYỀN VÔ
    }


    override fun onBindViewHolder(holder: LikeVideoViewHolder, position: Int) {
        val video = getItem(position)
        holder.bind(video)
    }

    class LikeVideoViewHolder(
        private val binding: ItemLikeVideoBinding,
        private val onItemClick: (LikeVideo) -> Unit
    ):
        RecyclerView.ViewHolder(binding.root) {

        fun bind(likeVideo: LikeVideo) {
            binding.apply {
                tvChannelName.text = likeVideo.channelName
                tvVideoTitle.text = likeVideo.videoTitle
                tvVideoDuration.text = formatDuration(likeVideo.duration)
                tvViewCount.text = formatViewCount(likeVideo.viewCount)
                tvTimeAgo.text = formatTimeAgo(likeVideo.publishedAt)
                Glide.with(ivVideoThumbnail.context)
                    .load(likeVideo.thumbnailUrl)
                    .placeholder(R.drawable.placeholder_thumbnail)
                    .error(R.drawable.placeholder_thumbnail)
                    .into(ivVideoThumbnail)

                itemView.setOnClickListener {
                    onItemClick(likeVideo)
                    val navController = itemView.findNavController()
                    val videoId = likeVideo.videoId

                    val currentDestinationId = navController.currentDestination?.id
                    val action = com.tkjen.youtube.ui.like.LikeVideosFragmentDirections
                        .actionLikeVideosFragmentToVideoDetailsLikeFragment(videoId)

                    if (currentDestinationId == R.id.videoDetailsLikeFragment) {
                        navController.navigate(
                            R.id.action_videoDetailsLikeFragment_self,
                           VideoDetailsLikeFragmentArgs(videoId).toBundle()
                        )
                    } else {
                        navController.navigate(action)
                    }
                }


            }
        }

    }

}