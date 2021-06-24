package com.newland.camera.utils

import android.content.Context
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide

object GlideUtils {
    /**
     * 顯示圖片
     */
    fun loadImage(context: Context, url: String, imageView: AppCompatImageView) {
        Glide.with(context).load(url).into(imageView)
    }
}