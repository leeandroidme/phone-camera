package com.newland.camera.widget

import android.content.Context
import android.graphics.RectF
import android.graphics.SurfaceTexture
import android.opengl.GLES20
import android.opengl.GLES30
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import com.newland.camera.camera.CameraDrawer
import com.newland.opengl.utils.TextureUtils
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class CameraGLSurfaceView : GLSurfaceView, GLSurfaceView.Renderer,
    SurfaceTexture.OnFrameAvailableListener {
    private var mTextureId = -1
    private lateinit var mSurfaceTexture: SurfaceTexture
    private lateinit var mCameraDraw: CameraDrawer

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    init {
        setEGLContextClientVersion(3)
        setRenderer(this)
        renderMode = RENDERMODE_WHEN_DIRTY
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        mTextureId = TextureUtils.getExternalOESTextureId()
        mSurfaceTexture = SurfaceTexture(mTextureId)
        mSurfaceTexture.setOnFrameAvailableListener(this)
        mCameraDraw = CameraDrawer()
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES30.glViewport(0, 0, width, height)
        mCameraDraw?.surfaceChanged(gl, width, height)
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES20.glClearColor(0f, 0f, 0f, 0f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)
        mSurfaceTexture.updateTexImage()
        mCameraDraw.draw(mTextureId, false)
    }

    override fun onFrameAvailable(surfaceTexture: SurfaceTexture?) {
        requestRender()
    }
}