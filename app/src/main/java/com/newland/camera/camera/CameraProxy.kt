package com.newland.camera.camera

import android.annotation.SuppressLint
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.media.ImageReader
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper.getMainLooper
import android.util.Log
import android.view.Surface
import com.newland.camera.beans.exception.CameraConnectException
import com.newland.camera.beans.exception.CameraStateException
import com.newland.camera.beans.exception.CaptureStateException
import kotlinx.coroutines.suspendCancellableCoroutine
import java.lang.RuntimeException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class CameraProxy {
    companion object {
        private const val TAG = "CameraProxy"
    }

    private lateinit var mCameraManager: CameraManager
    private var mCameraDevice: CameraDevice? = null
    lateinit var mCameraId: String
    private var mCaptureSession: CameraCaptureSession? = null
    private var mImageReader: ImageReader? = null

    private val mMainHandler by lazy { Handler(getMainLooper()) }
    private val mBackgroundHandlerThread = HandlerThread("background").apply {
        start()
    }
    private val mBackgroundHandler: Handler = Handler(mBackgroundHandlerThread.looper)
    private val mStateCallback = object : CameraDevice.StateCallback() {
        override fun onOpened(cameraDevice: CameraDevice) {
            mCameraDevice = cameraDevice
//            takePreview()
        }

        override fun onDisconnected(cameraDevice: CameraDevice) {
            releaseCamera()
        }

        override fun onError(cameraDevice: CameraDevice, error: Int) {
        }
    }

    private fun releaseCamera() {
        Log.e(TAG, "releaseCamera")
        mCaptureSession?.close()
        mCaptureSession = null
        mCameraDevice?.close()
        mCameraDevice = null
        mImageReader?.close()
        mImageReader = null
        stopBackgroundThread()
    }

    private fun stopBackgroundThread() {
        try {
            mBackgroundHandlerThread.quitSafely()
            mBackgroundHandlerThread.join()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    @SuppressLint("MissingPermission")
    private suspend fun openCamera(
        cameraManager: CameraManager,
        cameraId: String,
        handler: Handler?
    ): CameraDevice =
        suspendCancellableCoroutine { cont ->
            cameraManager.openCamera(
                cameraId,
                object : CameraDevice.StateCallback() {
                    override fun onOpened(camera: CameraDevice) {
                        cont.resume(camera)
                    }

                    override fun onDisconnected(camera: CameraDevice) {
                        cont.resumeWithException(CameraConnectException(camera))
                    }

                    override fun onError(camera: CameraDevice, error: Int) {
                        cont.resumeWithException(CameraStateException(camera, error))
                    }

                }, handler
            )
        }

    /**
     * 創建capture session
     */
    private suspend fun createCaptureSession(
        cameraDevice: CameraDevice,
        targets: List<Surface>,
        handler: Handler? = null
    ): CameraCaptureSession = suspendCancellableCoroutine { cont ->
        cameraDevice.createCaptureSession(targets, object : CameraCaptureSession.StateCallback() {
            override fun onConfigured(session: CameraCaptureSession) {
                cont.resume(session)
            }

            override fun onConfigureFailed(session: CameraCaptureSession) {
                cont.resumeWithException(CaptureStateException(session))
            }

        }, handler)
    }
}