package com.tkjen.youtube.ui.home.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.tkjen.youtube.ui.categories.CategoryVideosFragment

class CategoryViewPagerAdapter(
    activity: FragmentActivity,
    private val categories: List<String>
) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = categories.size

    override fun createFragment(position: Int): Fragment {
        return CategoryVideosFragment.newInstance(categories[position])
    }
} 