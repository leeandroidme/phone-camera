package com.newland.camera

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.newland.camera.common.TakeOptionConstant
import com.newland.camera.widget.center.CenterItemDecoration
import com.newland.camera.widget.center.CenterLayoutManager
import com.newland.camera.widget.center.CenterRecyclerView
import com.newland.ui.adapter.MenuAdapter

class MainActivity : AppCompatActivity() {
    private val indicatorTake: CenterRecyclerView by lazy { findViewById(R.id.indicator_take) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initTakeOperationView()
    }

    private fun initTakeOperationView() {
        val datas = TakeOptionConstant.getTakeOperations()
        var centerLayoutManager = CenterLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        var centerItemDecoration = CenterItemDecoration()
        var adapter = MenuAdapter(datas)
        adapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
                indicatorTake.smoothScrollToPosition(position)
            }
        })
        indicatorTake.mOnTargetItemListener = object : CenterRecyclerView.OnTargetItemListener {
            override fun onTargetItem(position: Int, prePosition: Int) {
                adapter.refreshTakeOperation(position, prePosition)
            }
        }
        indicatorTake.layoutManager = centerLayoutManager
        indicatorTake.addItemDecoration(centerItemDecoration)
        indicatorTake.adapter = adapter
        for (i in datas.indices){
            if(datas[i].flag==TakeOptionConstant.TAKE_PHOTO){
                indicatorTake.setInitPosition(i)
                break
            }
        }

    }
}