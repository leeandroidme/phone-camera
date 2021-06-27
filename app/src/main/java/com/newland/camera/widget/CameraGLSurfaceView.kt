package com.newland.camera.widget

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet

class CameraGLSurfaceView:GLSurfaceView {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    init {
        setEGLContextClientVersion(2)
        val mCameraV2Renderer = CameraRenderer()
        setRenderer(mCameraV2Renderer)
        renderMode = RENDERMODE_WHEN_DIRTY
    }
}