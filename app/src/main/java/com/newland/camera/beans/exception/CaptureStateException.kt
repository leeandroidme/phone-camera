package com.newland.camera.beans.exception

import android.hardware.camera2.CameraCaptureSession
import java.lang.RuntimeException

class CaptureStateException : RuntimeException {
    private var session: CameraCaptureSession? = null

    public constructor(session: CameraCaptureSession) : super() {
        this.session = session
    }
}