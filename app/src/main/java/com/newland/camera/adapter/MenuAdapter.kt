package com.newland.ui.adapter

import com.newland.camera.R
import com.newland.camera.beans.TakeOperation


class MenuAdapter(data: MutableList<TakeOperation>?) :
    SimpleQuickAdapter<TakeOperation>(R.layout.adapter_menu, data) {
    private var selectTakeOperation: TakeOperation? = null
    override fun convert(holder: ViewHolder, item: TakeOperation) {
        holder.setText(R.id.tv_text, item.operation)
        holder.setTextColorRes(
            R.id.tv_text,
            if (item.flag == selectTakeOperation?.flag) R.color.color_take_operation_select
            else R.color.white
        )
    }

    fun refreshTakeOperation(position: Int, prePosition: Int) {
        selectTakeOperation = data[position]
        notifyItemChanged(position)
        notifyItemChanged(prePosition)
    }
}