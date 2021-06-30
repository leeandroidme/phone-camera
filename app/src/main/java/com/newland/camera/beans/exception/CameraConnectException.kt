package com.newland.camera.beans.exception

import android.hardware.camera2.CameraDevice

class CameraConnectException : RuntimeException {
    private var cameraDevice: CameraDevice? = null

    constructor(cameraDevice: CameraDevice) : super() {
        this.cameraDevice = cameraDevice
    }
}