package com.newland.camera.utils

import android.content.Context
import android.os.Build
import com.newland.camera.di.CameraApplication
import java.io.BufferedReader
import java.io.InputStreamReader

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

    fun readResource(resourceId: Int): String =
        readResource(CameraApplication.application, resourceId)

    fun readResource(context: Context, resourceId: Int): String {
        val builder = StringBuilder()
        try {
            val inputStream = context.resources
                .openRawResource(resourceId)
            val streamReader = InputStreamReader(inputStream)

            val bufferedReader = BufferedReader(streamReader)
            var textLine: String?
            while (bufferedReader.readLine().also { textLine = it } != null) {
                builder.append(textLine)
                builder.append("\n")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return builder.toString()
    }
}