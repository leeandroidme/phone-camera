package com.newland.camera.common

import android.graphics.SurfaceTexture

/**
 * @author: leellun
 * @data: 2/7/2021.
 *
 */
interface CameraRenderCallback {
    fun onSurfaceCreated(surfaceTexture: SurfaceTexture)
}