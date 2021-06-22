package com.newland.camera.widget.center

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration


/**
 * @author: leellun
 * @data: 21/6/2021.
 * 主要头尾插入动态宽度使其头尾居中显示
 */
class CenterItemDecoration : ItemDecoration() {
    private val measurewidth: Int? = null
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        var position = parent.getChildAdapterPosition(view)
        if (position == 0) {
            var parentWidth = parent.measuredWidth
            var childWidth = view.measuredWidth
            if (childWidth == 0) {
                view.measure(0, 0)
                childWidth = view.measuredWidth
            }
//            outRect.set(((parentWidth - childWidth + 0.5) / 2).toInt(), 0, 0, 0)
            outRect.set(parentWidth/ 2, 0, 0, 0)
        } else if (position + 1 == parent.adapter?.itemCount) {
            var parentWidth = parent.measuredWidth
            var childWidth = view.measuredWidth
            if (childWidth == 0) {
                view.measure(0, 0)
                childWidth = view.measuredWidth
            }
//            outRect.set(0, 0, ((parentWidth - childWidth + 0.5) / 2).toInt(), 0)
            outRect.set(0, 0, parentWidth/ 2, 0)
        }
    }
}