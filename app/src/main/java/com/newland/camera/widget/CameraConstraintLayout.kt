package com.newland.camera.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewConfiguration
import androidx.constraintlayout.widget.ConstraintLayout

class CameraConstraintLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private var mLastTouchX = 0
    private var mLastTouchY = 0
    private var mDown = false
    private var mTouchSlop = 0
    private var mScrollPointerId = 0
    var mOnSwitchListener: OnSwitchListener? = null

    override fun onInterceptTouchEvent(e: MotionEvent?): Boolean {
        val action = e!!.actionMasked
        val actionIndex = e!!.actionIndex
        when (action) {
            MotionEvent.ACTION_DOWN -> {
                if (e.pointerCount == 1) {
                    mDown = true
                    mScrollPointerId = e.getPointerId(actionIndex)
                    mLastTouchX = (e!!.x + 0.5f).toInt()
                    mLastTouchY = (e!!.y + 0.5f).toInt()
                }
            }
            MotionEvent.ACTION_UP -> {
                mDown = false
            }
            MotionEvent.ACTION_CANCEL -> {
                mDown = false
            }
        }
        return false
    }

    /**
     * 重写touch 使其原有滑动不生效
     */
    override fun onTouchEvent(e: MotionEvent?): Boolean {
        //当itemView没有设置点击事件或者onTouchEvent没有消费event，会抛向parent处理
        if (e?.action == MotionEvent.ACTION_MOVE) {
            if (e.pointerCount == 1) {
                if (mDown) {
                    handleMoveEvent(e)
                }
            } else {
                mDown = false
            }
        }
        return true
    }

    /**
     * move事件处理
     */
    private fun handleMoveEvent(e: MotionEvent?) {
        val vc = ViewConfiguration.get(context)
        mTouchSlop = vc.scaledTouchSlop
        val index = e!!.findPointerIndex(mScrollPointerId)
        if (index < 0) {
            return
        }
        val x = (e!!.getX(index) + 0.5f).toInt()
        val y = (e!!.getY(index) + 0.5f).toInt()
        val dx = x - mLastTouchX
        val dy = y - mLastTouchY
        if (Math.abs(dx) > Math.abs(dy) && Math.abs(dx) > mTouchSlop) {
            if (dx > 0) {
                mOnSwitchListener?.onPrev()
            } else {
                mOnSwitchListener?.onNext()
            }
            mDown = false
        }
    }

    interface OnSwitchListener {
        fun onPrev()
        fun onNext()
    }
}