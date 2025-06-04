package com.tkjen.youtube.ui.shorts

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView

class ShortsLayoutManager : LinearLayoutManager {
    private val pagerSnapHelper = PagerSnapHelper()

    constructor(context: Context) : super(context)

    override fun onAttachedToWindow(view: RecyclerView) {
        super.onAttachedToWindow(view)
        pagerSnapHelper.attachToRecyclerView(view)
    }

    override fun onDetachedFromWindow(view: RecyclerView, recycler: RecyclerView.Recycler) {
        super.onDetachedFromWindow(view, recycler)
        pagerSnapHelper.attachToRecyclerView(null)
    }
}