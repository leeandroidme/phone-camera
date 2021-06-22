package com.newland.camera.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet

/**
 * @author: leellun
 * @data: 22/6/2021.
 *
 */
class RoundImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : androidx.appcompat.widget.AppCompatImageView(context, attrs, defStyleAttr) {
    private var paint = Paint(Paint.ANTI_ALIAS_FLAG and Paint.DITHER_FLAG)
    private var roundRect: RectF = RectF()
    private var xfermode: Xfermode

    init {
        xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        paint.setColor(Color.WHITE)
        paint.setFilterBitmap(true)
        paint.style = Paint.Style.FILL
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        roundRect.set(0f, 0f, w.toFloat(), h.toFloat())
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.also { canvas ->
            canvas.saveLayer(roundRect, paint)
            canvas.drawRoundRect(roundRect, 10f, 10f, paint)
            paint.setXfermode(xfermode)
            canvas.saveLayer(roundRect, paint)
            super.onDraw(canvas)
            canvas.restore()
            canvas.restore()
            paint.setXfermode(null)
        }

    }
}