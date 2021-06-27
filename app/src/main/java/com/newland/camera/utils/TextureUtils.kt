package com.newland.camera.utils

import com.newland.camera.beans.Texture
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer

object TextureUtils {
    fun isValidTexture(t: Texture?): Boolean {
        return t != null && !t.isDestroyed
    }

    fun d2r(degree: Double): Double {
        return degree * Math.PI.toFloat() / 180f
    }

    fun toFloatBuffer(v: FloatArray): FloatBuffer? {
        val buf = ByteBuffer.allocateDirect(v.size * 4)
        buf.order(ByteOrder.nativeOrder())
        val buffer = buf.asFloatBuffer()
        buffer.put(v)
        buffer.position(0)
        return buffer
    }

    fun toShortBuffer(v: ShortArray): ShortBuffer? {
        val buf = ByteBuffer.allocateDirect(v.size * 2)
        buf.order(ByteOrder.nativeOrder())
        val buffer = buf.asShortBuffer()
        buffer.put(v)
        buffer.position(0)
        return buffer
    }
}