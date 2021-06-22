package com.newland.camera.utils

import android.content.Context
import android.os.Build

/**
 * @author: leellun
 * @data: 22/6/2021.
 *
 */
object ResourceUtils {
    fun getColor(context: Context, id: Int): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) context.getColor(id) else context.resources.getColor(
            id
        )
    }
}