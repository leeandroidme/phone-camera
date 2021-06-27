package com.newland.camera.beans

import android.graphics.Bitmap
import android.opengl.GLUtils
import javax.microedition.khronos.opengles.GL10

class Texture {
    val id = intArrayOf(0)
    var width = 0
        private set
    var height = 0
        private set
    var contentWidth = 0
        private set
    var contentHeight = 0
        private set
    val isDestroyed = false
    fun destroy(gl: GL10) {
        if (id[0] != 0) gl.glDeleteTextures(1, id, 0)
        id[0] = 0
    }

    companion object {
        fun createTexture(bitmap: Bitmap, gl: GL10): Texture {
            val t = Texture()
            val w = Integer.highestOneBit(bitmap.width - 1) shl 1
            val h = Integer.highestOneBit(bitmap.height - 1) shl 1
            t.contentWidth = bitmap.width
            t.contentHeight = bitmap.height
            t.width = w
            t.height = h
            gl.glGenTextures(1, t.id, 0)
            gl.glBindTexture(GL10.GL_TEXTURE_2D, t.id[0])
            gl.glTexParameterf(
                GL10.GL_TEXTURE_2D,
                GL10.GL_TEXTURE_MIN_FILTER,
                GL10.GL_LINEAR.toFloat()
            )
            gl.glTexParameterf(
                GL10.GL_TEXTURE_2D,
                GL10.GL_TEXTURE_MAG_FILTER,
                GL10.GL_LINEAR.toFloat()
            )
            /*
		 gl.glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		 gl.glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
		 */gl.glTexImage2D(
                GL10.GL_TEXTURE_2D,
                0,
                GL10.GL_RGBA,
                w,
                h,
                0,
                GL10.GL_RGBA,
                GL10.GL_UNSIGNED_BYTE,
                null
            )
            GLUtils.texSubImage2D(GL10.GL_TEXTURE_2D, 0, 0, 0, bitmap)
            return t
        }
    }
}