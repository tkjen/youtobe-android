package com.tkjen.youtube.ui.like

import android.view.View
import androidx.fragment.app.Fragment
import com.tkjen.youtube.R
import com.tkjen.youtube.databinding.FragmentLikeVideosBinding

class LikeVideosFragment:Fragment(R.layout.fragment_like_videos) {

     private lateinit var binding:FragmentLikeVideosBinding

        override fun onCreateView(
             inflater: android.view.LayoutInflater,
                 container: android.view.ViewGroup?,
                 savedInstanceState: android.os.Bundle?):View
        {
            binding = FragmentLikeVideosBinding.inflate(inflater, container, false)
            return binding.root
        }

}