package com.newland.camera.camera

import android.opengl.GLES11Ext
import android.opengl.GLES20
import android.opengl.GLES30
import android.opengl.Matrix
import com.newland.camera.R
import com.newland.camera.utils.ResourceUtils
import com.newland.opengl.utils.ShaderUtils
import java.nio.ByteBuffer
import java.nio.ByteOrder
import javax.microedition.khronos.opengles.GL10

/**
 * @author: leellun
 * @data: 1/7/2021.
 *
 */
class CameraDrawer {
    companion object {
        private const val VERTEX_SIZE = 2
        private const val VERTEX_STRIDE = VERTEX_SIZE * 4
        private val VERTEXES = floatArrayOf(
            -1.0f, 1.0f,
            -1.0f, -1.0f,
            1.0f, -1.0f,
            1.0f, 1.0f
        )

        // 后置摄像头使用的纹理坐标
        private val TEXTURE_BACK = floatArrayOf(
            0.0f, 1.0f,
            1.0f, 1.0f,
            1.0f, 0.0f,
            0.0f, 0.0f
        )

        // 前置摄像头使用的纹理坐标
        private val TEXTURE_FRONT = floatArrayOf(
            1.0f, 1.0f,
            0.0f, 1.0f,
            0.0f, 0.0f,
            1.0f, 0.0f
        )
        private val VERTEX_ORDER = shortArrayOf(0, 1, 2, 3)

    }

    private var mVertexBuffer =
        ByteBuffer.allocateDirect(VERTEXES.size * 4).order(ByteOrder.nativeOrder()).asFloatBuffer()
            .put(
                VERTEXES
            ).position(0)
    private var mBackTextureBuffer =
        ByteBuffer.allocateDirect(TEXTURE_BACK.size * 4).order(ByteOrder.nativeOrder())
            .asFloatBuffer()
            .put(
                TEXTURE_BACK
            ).position(0)
    private var mFrontTextureBuffer =
        ByteBuffer.allocateDirect(TEXTURE_FRONT.size * 4).order(ByteOrder.nativeOrder())
            .asFloatBuffer()
            .put(
                TEXTURE_FRONT
            ).position(0)
    private var mDrawListBuffer =
        ByteBuffer.allocateDirect(VERTEX_ORDER.size * 2).order(ByteOrder.nativeOrder())
            .asShortBuffer()
            .put(
                VERTEX_ORDER
            ).position(0)
    private var mProgram = 0
    private var mPositionIndex = -1
    private var mTextureIndex = -1
    private var uMatrixLocation = -1
    private var mMatrix = FloatArray(16)
    init {
        var vertexShareId =
            ShaderUtils.compileVertexShader(ResourceUtils.readResource(R.raw.vertex_camera))
        var fragmentShareId =
            ShaderUtils.compileFragmentShader(ResourceUtils.readResource(R.raw.fragment_camera))
        mProgram = ShaderUtils.linkProgram(vertexShareId, fragmentShareId)
        mPositionIndex = GLES20.glGetAttribLocation(mProgram, "vPosition")
        mTextureIndex = GLES20.glGetAttribLocation(mProgram, "inputTextureCoordinate")
        uMatrixLocation = GLES30.glGetUniformLocation(mProgram, "u_Matrix")
    }
    fun surfaceChanged(gl: GL10?, width: Int, height: Int) {
        val aspectRatio =
            if (width > height) width.toFloat() / height.toFloat() else height.toFloat() / width.toFloat()
        if (width > height) {
            Matrix.orthoM(mMatrix, 0, -aspectRatio, aspectRatio, -1f, 1f, -1f, 1f)
        } else {
            Matrix.orthoM(mMatrix, 0, -1f, 1f, -aspectRatio, aspectRatio, -1f, 1f)
        }
    }
    fun draw(texture: Int, isFrontCamera: Boolean) {
        GLES20.glUseProgram(mProgram) // 指定使用的program
        GLES20.glEnable(GLES20.GL_CULL_FACE) // 启动剔除
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, texture) // 绑定纹理
        GLES20.glEnableVertexAttribArray(mPositionIndex)
        GLES20.glVertexAttribPointer(
            mPositionIndex,
            VERTEX_SIZE,
            GLES20.GL_FLOAT,
            false,
            VERTEX_STRIDE,
            mVertexBuffer
        )
        GLES20.glEnableVertexAttribArray(mTextureIndex)
        if (isFrontCamera) {
            GLES20.glVertexAttribPointer(
                mTextureIndex,
                VERTEX_SIZE,
                GLES20.GL_FLOAT,
                false,
                VERTEX_STRIDE,
                mFrontTextureBuffer
            )
        } else {
            GLES20.glVertexAttribPointer(
                mTextureIndex,
                VERTEX_SIZE,
                GLES20.GL_FLOAT,
                false,
                VERTEX_STRIDE,
                mBackTextureBuffer
            )
        }
        // 真正绘制的操作
        GLES20.glDrawElements(
            GLES20.GL_TRIANGLE_FAN,
            VERTEX_ORDER.size,
            GLES20.GL_UNSIGNED_BYTE,
            mDrawListBuffer
        )
        GLES20.glDisableVertexAttribArray(mPositionIndex)
        GLES20.glDisableVertexAttribArray(mTextureIndex)
    }
}