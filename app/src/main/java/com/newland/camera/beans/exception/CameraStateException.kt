package com.newland.camera.beans.exception

import android.hardware.camera2.CameraDevice
import java.lang.RuntimeException

class CameraStateException : RuntimeException {
    private var cameraDevice: CameraDevice?=null
    private var error = -1
    constructor(cameraDevice: CameraDevice, error: Int) : super() {
        this.cameraDevice = cameraDevice
        this.error = error
    }
}