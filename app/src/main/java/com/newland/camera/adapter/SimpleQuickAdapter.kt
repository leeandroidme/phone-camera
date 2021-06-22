package com.newland.ui.adapter;

import android.util.SparseArray
import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 * @author: leellun
 */
abstract class SimpleQuickAdapter<T>(layoutResId: Int, data: MutableList<T>?) :
    BaseQuickAdapter<T, SimpleQuickAdapter.ViewHolder>(layoutResId, data) {

    class ViewHolder(view: View) : BaseViewHolder(view) {
        private val mViews = SparseArray<View>();

        fun setTextSize(id: Int, resId: Int) {
            var tv: TextView = getView(id);
            tv.setTextSize(tv.getContext().getResources().getDimension(resId));
        }


        fun setVisibility(id: Int, isVisiblity: Boolean) {
            var view: View = getView(id);
            view.visibility = if (isVisiblity) View.VISIBLE else View.GONE
        }

        fun setSelected(id: Int, isSelected: Boolean) {
            var view: View = getView(id);
            view.setSelected(isSelected);
        }

    }
}
