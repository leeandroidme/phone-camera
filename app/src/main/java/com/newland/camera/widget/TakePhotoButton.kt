package com.newland.camera.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Build
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.annotation.RequiresApi
import com.newland.camera.R
import com.newland.camera.utils.ResourceUtils
import kotlin.properties.Delegates

/**
 * @author: leellun
 * @data: 22/6/2021.
 *
 */
@RequiresApi(Build.VERSION_CODES.M)
class TakePhotoButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : androidx.appcompat.widget.AppCompatImageButton(context, attrs, defStyleAttr) {
    companion object {
        val TAKE_PHOTO: Int = 1

    }

    var type = TAKE_PHOTO
    private var prePress = false
    private var paint = Paint(Paint.ANTI_ALIAS_FLAG and Paint.DITHER_FLAG)
    var takeColor: Int
    private var innnerRadius = 0.0f
    private var maxInnerRadius = 0.0f
    private var strokeWidth = 0.0f

    init {
        takeColor = ResourceUtils.getColor(context, R.color.white)
        paint.setColor(takeColor)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        var strokeWidth = measuredWidth * 0.08f
        paint.strokeWidth = strokeWidth
        maxInnerRadius = (measuredWidth - strokeWidth * 1.5f)
        innnerRadius = maxInnerRadius * 0.8f
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        var result = super.onTouchEvent(event)
        if ((isPressed && !prePress) || (!isPressed && prePress)) {

        }
        return result
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.also { canvas ->
            when (type) {
                TAKE_PHOTO -> if (isPressed) {

                } else {
                    var left = (measuredWidth - innnerRadius) / 2
                    var top = (measuredHeight - innnerRadius) / 2
                    paint.style = Paint.Style.FILL
                    canvas.drawOval(
                        left,
                        top,
                        left + innnerRadius,
                        top + innnerRadius,
                        paint
                    )

                    paint.style = Paint.Style.STROKE
                    var strokeHalf = paint.strokeWidth / 2
                    canvas.drawRoundRect(
                        strokeHalf, strokeHalf,
                        measuredWidth.toFloat() - strokeHalf,
                        measuredHeight.toFloat() - strokeHalf,
                        measuredWidth / 2.0f,
                        measuredHeight / 2.0f,
                        paint
                    )
                }
            }
        }
    }
}