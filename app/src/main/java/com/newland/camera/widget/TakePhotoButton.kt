package com.newland.camera.widget

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent

/**
 * @author: leellun
 * @data: 22/6/2021.
 *
 */
class TakePhotoButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : androidx.appcompat.widget.AppCompatImageButton(context, attrs, defStyleAttr) {
    init {

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return super.onTouchEvent(event)
    }
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
    }
}