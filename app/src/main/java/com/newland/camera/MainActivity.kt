package com.newland.camera

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.hardware.camera2.CameraManager
import android.opengl.Visibility
import android.os.Bundle
import android.view.SurfaceView
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.newland.camera.common.TakeOptionConstant
import com.newland.camera.widget.CameraConstraintLayout
import com.newland.camera.widget.CameraConstraintLayout.OnSwitchListener
import com.newland.camera.widget.TakePhotoButton
import com.newland.camera.widget.center.CenterItemDecoration
import com.newland.camera.widget.center.CenterLayoutManager
import com.newland.camera.widget.center.CenterRecyclerView
import com.newland.ui.adapter.MenuAdapter

class MainActivity : AppCompatActivity() {
    private val indicatorTake: CenterRecyclerView by lazy { findViewById(R.id.indicator_take) }
    private val takePhotoBtn: TakePhotoButton by lazy { findViewById(R.id.btn_takephoto) }
    private val bottomLayout: View by lazy { findViewById(R.id.layout_bottom) }
    private val topLayout: View by lazy { findViewById(R.id.layout_top) }
    private val surfaceView: SurfaceView by lazy { findViewById(R.id.surfaceView) }
    private val cameraConstraintLayout: CameraConstraintLayout by lazy { findViewById(R.id.camera_constraintlayout) }
    private val flashIb: ImageButton by lazy { findViewById(R.id.ib_flash) }
    private val timerIb: ImageButton by lazy { findViewById(R.id.ib_timer) }
    private val filterIb: ImageButton by lazy { findViewById(R.id.ib_filter) }
    private val flashAutoTv: AppCompatTextView by lazy { findViewById(R.id.tv_flash_auto) }
    private val flashOpenTv: AppCompatTextView by lazy { findViewById(R.id.tv_flash_open) }
    private val flashCloseIb: AppCompatTextView by lazy { findViewById(R.id.tv_flash_close) }
    private val closeTimerTv: AppCompatTextView by lazy { findViewById(R.id.tv_timer_close) }
    private val timer3sTv: AppCompatTextView by lazy { findViewById(R.id.tv_timer_3s) }
    private val timer10sTv: AppCompatTextView by lazy { findViewById(R.id.tv_timer_10s) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initTakeOperationView()
        initSurfaceView()
        flashIb.setOnClickListener {
            if (flashAutoTv.visibility == View.VISIBLE) {
                timerIb.visibility = View.VISIBLE
                filterIb.visibility = View.VISIBLE
                flashAutoTv.visibility = View.GONE
                flashOpenTv.visibility = View.GONE
                flashCloseIb.visibility = View.GONE
            } else {
                timerIb.visibility = View.GONE
                filterIb.visibility = View.GONE
                flashAutoTv.visibility = View.VISIBLE
                flashOpenTv.visibility = View.VISIBLE
                flashCloseIb.visibility = View.VISIBLE
            }
        }
        timerIb.setOnClickListener {
            if (closeTimerTv.visibility == View.VISIBLE) {
                closeTimerTv.visibility = View.GONE
                timer3sTv.visibility = View.GONE
                timer10sTv.visibility = View.GONE
                var targetX: Float = (((timerIb.parent as ViewGroup).width - timerIb.width) / 2).toFloat()
                var animator = ObjectAnimator.ofFloat(timerIb, "x", timerIb.x, targetX)
                animator.duration = 800
                animator.addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        flashIb.visibility = View.VISIBLE
                        filterIb.visibility = View.VISIBLE
                    }
                })
                animator.start()
            } else {
                flashIb.visibility = View.GONE
                filterIb.visibility = View.GONE
                var animator = ObjectAnimator.ofFloat(timerIb, "x", timerIb.x, flashIb.x)
                animator.duration = 800
                animator.addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        closeTimerTv.visibility = View.VISIBLE
                        timer3sTv.visibility = View.VISIBLE
                        timer10sTv.visibility = View.VISIBLE
                    }
                })
                animator.start()
            }
        }
    }

    private fun initSurfaceView() {
        surfaceView.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                surfaceView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                var parent = surfaceView.parent as ViewGroup
                surfaceView.layoutParams.height =
                        parent.height - topLayout.height - bottomLayout.height
            }
        })
    }

    private fun initTakeOperationView() {
        val datas = TakeOptionConstant.getTakeOperations()
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
        indicatorTake.layoutManager =
                CenterLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        indicatorTake.addItemDecoration(CenterItemDecoration())
        indicatorTake.adapter = adapter
        for (i in datas.indices) {
            if (datas[i].flag == TakeOptionConstant.TAKE_PHOTO) {
                indicatorTake.setInitPosition(i)
                takePhotoBtn.type = datas[i].flag
                break
            }
        }
        cameraConstraintLayout.mOnSwitchListener = object : OnSwitchListener {
            override fun onPrev() {
                indicatorTake.smoothScrollToPosition(indicatorTake.mPosition - 1)
            }

            override fun onNext() {
                indicatorTake.smoothScrollToPosition(indicatorTake.mPosition + 1)
            }

        }
    }
}