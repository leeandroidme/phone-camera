package com.newland.camera.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Build
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.annotation.RequiresApi
import com.newland.camera.R
import com.newland.camera.common.TakeOptionConstant
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

    var type: Int = TakeOptionConstant.TAKE_PHOTO
    private var prePress = false
    private var paint = Paint(Paint.ANTI_ALIAS_FLAG and Paint.DITHER_FLAG)
    var takeColor: Int
    private var innerRadius = 0.0f
    private var minInnerRadius = 0.0f
    private var radius = 0.0f

    init {
        takeColor = ResourceUtils.getColor(context, R.color.white)
        paint.setColor(takeColor)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        var strokeWidth = measuredWidth * 0.08f
        paint.strokeWidth = strokeWidth
        var maxInnerRadius = (measuredWidth - strokeWidth * 1.5f)
        innerRadius = maxInnerRadius * 0.85f
        minInnerRadius = maxInnerRadius * 0.75f
        radius = innerRadius
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        var result = super.onTouchEvent(event)
        when (type) {
            TakeOptionConstant.TAKE_PHOTO -> onTouchTakePhoto(event)
            TakeOptionConstant.SEQUARE -> onTouchTakePhoto(event)
            TakeOptionConstant.FULL -> onTouchTakePhoto(event)
        }

        if ((isPressed && !prePress) || (!isPressed && prePress)) {

        }
        return true
    }

    private fun onTouchTakePhoto(event: MotionEvent?) {
        when (event?.action) {
            MotionEvent.ACTION_DOWN ->{
                radius = minInnerRadius
                postInvalidate()
            }
            MotionEvent.ACTION_CANCEL->{
                radius = innerRadius
                postInvalidate()
            }
            MotionEvent.ACTION_UP -> {
                radius = innerRadius
                postInvalidate()
            }
        }

    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.also { canvas ->
            when (type) {
                TakeOptionConstant.TAKE_PHOTO -> if (isPressed) {

                } else {
                    var left = (measuredWidth - radius) / 2
                    var top = (measuredHeight - radius) / 2
                    paint.style = Paint.Style.FILL
                    canvas.drawOval(
                        left,
                        top,
                        left + radius,
                        top + radius,
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