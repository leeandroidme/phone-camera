package com.newland.camera

import android.graphics.SurfaceTexture
import android.opengl.GLSurfaceView
import android.os.Bundle
import android.util.SparseIntArray
import android.view.Surface
import com.newland.camera.common.CameraRenderCallback
import com.newland.camera.widget.CameraGLSurfaceView

class TakeActivity : BaseCameraActivity() {
    companion object{
        val ORIENTATIONS: SparseIntArray = SparseIntArray()

        init {
            ORIENTATIONS.append(Surface.ROTATION_0, 90)
            ORIENTATIONS.append(Surface.ROTATION_90, 0)
            ORIENTATIONS.append(Surface.ROTATION_180, 270)
            ORIENTATIONS.append(Surface.ROTATION_270, 180)
        }
    }
    private val surfaceView: CameraGLSurfaceView by lazy { findViewById(R.id.surfaceView) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_take)
        surfaceView.cameraRenderCallback=object :CameraRenderCallback{
            override fun onSurfaceCreated(surfaceTexture: SurfaceTexture) {
                openCamera(surfaceTexture)
            }
        }
    }
}