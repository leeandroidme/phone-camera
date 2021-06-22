package com.newland.camera.widget.center

import android.content.Context
import android.os.Parcelable
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView

/**
 * @author: leellun
 * @data: 21/6/2021.
 *
 */
class CenterLayoutManager : LinearLayoutManager {
    var mPendingScrollPosition = RecyclerView.NO_POSITION
    var mPrePendingScrollPosition = RecyclerView.NO_POSITION
    private var recyclerView: RecyclerView? = null

    constructor(context: Context?) : super(context)
    constructor(context: Context?, orientation: Int, reverseLayout: Boolean) : super(
        context,
        orientation,
        reverseLayout
    )

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun onAttachedToWindow(view: RecyclerView?) {
        super.onAttachedToWindow(view)
        recyclerView = view
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        mPendingScrollPosition = -1;
        super.onRestoreInstanceState(state)
    }

    /**
     * 当设置初始位置时，layout添加childview完成后 第一次调用scrollToPosition移动制定position 第二次指定位置position居中显示
     */
    override fun onLayoutCompleted(state: RecyclerView.State?) {
        super.onLayoutCompleted(state)
        if (mPrePendingScrollPosition != RecyclerView.NO_POSITION) {
            for (i in childCount - 1 downTo 0) {
                if (mPrePendingScrollPosition == RecyclerView.NO_POSITION) break
                recyclerView?.apply {
                    var view: View = getChildAt(i)
                    var adapterPosition = getChildAdapterPosition(view)
                    if (adapterPosition == mPrePendingScrollPosition) {
                        scrollBy(
                            (view.left - scrollX) - (measuredWidth - view.measuredWidth) / 2,
                            0
                        )
                        mPrePendingScrollPosition = RecyclerView.NO_POSITION
                    }
                }
            }
        } else if (mPendingScrollPosition != RecyclerView.NO_POSITION) {
            super.scrollToPosition(mPendingScrollPosition)
            mPrePendingScrollPosition = mPendingScrollPosition
            mPendingScrollPosition = RecyclerView.NO_POSITION
        }
    }

    override fun scrollToPosition(position: Int) {
        mPendingScrollPosition = position
        requestLayout()
    }

    override fun smoothScrollToPosition(
        recyclerView: RecyclerView?,
        state: RecyclerView.State?,
        position: Int
    ) {
        var smoothScroller = CenterSmoothScroller(recyclerView?.context)
        smoothScroller.targetPosition = position
        startSmoothScroll(smoothScroller)
    }

    class CenterSmoothScroller(context: Context?) : LinearSmoothScroller(context) {
        override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics?): Float {
            return 150f / displayMetrics?.densityDpi!!
        }

        override fun calculateDtToFit(
            viewStart: Int,
            viewEnd: Int,
            boxStart: Int,
            boxEnd: Int,
            snapPreference: Int
        ): Int {
            if (targetPosition == 0) {
                var newViewStart = viewStart + (boxEnd - boxStart) / 2
                return (boxStart + (boxEnd - boxStart) / 2) - (newViewStart + (viewEnd - newViewStart) / 2)
            } else if (targetPosition == childCount) {
                var newViewEnd = viewEnd - (boxEnd - boxStart) / 2
                return (boxStart + (boxEnd - boxStart) / 2) - (viewStart + (newViewEnd - viewStart) / 2)
            } else {
                return (boxStart + (boxEnd - boxStart) / 2) - (viewStart + (viewEnd - viewStart) / 2)
            }
        }
    }
}