package com.newland.camera

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.ImageFormat
import android.graphics.SurfaceTexture
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CaptureRequest
import android.media.ImageReader
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.util.Log
import android.util.Size
import android.view.Surface
import androidx.appcompat.app.AppCompatActivity
import com.newland.camera.beans.exception.CameraConnectException
import com.newland.camera.beans.exception.CameraStateException
import com.newland.camera.beans.exception.CaptureStateException
import com.newland.camera.utils.CameraUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * @author: leellun
 * @data: 1/7/2021.
 *
 */
open class BaseCameraActivity : AppCompatActivity() {
    private lateinit var mCameraManager: CameraManager
    private var mCameraDevice: CameraDevice? = null
    lateinit var mCameraId: String
    private var mCameraCaptureSession: CameraCaptureSession? = null
    private var mImageReader: ImageReader? = null

    private val mMainHandler by lazy { Handler(Looper.getMainLooper()) }
    private val mBackgroundHandlerThread = HandlerThread("background").apply {
        start()
    }
    private val mBackgroundHandler: Handler = Handler(mBackgroundHandlerThread.looper)
    private var mPreviewSurface: Surface? = null

    override fun onDestroy() {
        super.onDestroy()
        releaseCamera()
    }

    fun openCamera(surfaceTexture: SurfaceTexture) = GlobalScope.launch(Dispatchers.Main) {
        mCameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        mCameraId = CameraUtils.getFirstCameraIdFacing(mCameraManager)
        mCameraDevice = openCamera(mCameraManager, mCameraId, mBackgroundHandler)
        val size = Size(1920, 1080)
        mImageReader = ImageReader.newInstance(
            size.width, size.height, ImageFormat.JPEG, 3
        )
        if (surfaceTexture != null && mPreviewSurface == null) { // use texture view
            surfaceTexture!!.setDefaultBufferSize(
                size.width,
                size.height
            )
            mPreviewSurface = Surface(surfaceTexture)
        }
        val targets = listOf(mPreviewSurface!!, mImageReader!!.surface)
        mCameraCaptureSession = createCaptureSession(mCameraDevice!!, targets, mBackgroundHandler)
        val captureRequest =
            mCameraDevice?.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)?.apply {
                val rotation: Int = 0
                set(CaptureRequest.JPEG_ORIENTATION, TakeActivity.ORIENTATIONS[rotation])
                addTarget(mPreviewSurface!!)
            }!!
        mCameraCaptureSession?.setRepeatingRequest(captureRequest.build(), null, mBackgroundHandler)
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

    private fun releaseCamera() {
        try {
            mCameraCaptureSession?.close()
            mCameraCaptureSession = null
            mCameraDevice?.close()
            mCameraDevice = null
            mImageReader?.close()
            mImageReader = null
            stopBackgroundThread()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun stopBackgroundThread() {
        try {
            mBackgroundHandlerThread.quitSafely()
            mBackgroundHandlerThread.join()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}