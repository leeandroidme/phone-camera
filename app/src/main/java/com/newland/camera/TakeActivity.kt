package com.newland.camera

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.ImageFormat
import android.hardware.camera2.*
import android.media.ImageReader
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.SparseIntArray
import android.view.Surface
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.newland.camera.widget.AutoFitSurfaceView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import java.lang.RuntimeException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class TakeActivity : AppCompatActivity() {
    companion object{
        val ORIENTATIONS: SparseIntArray = SparseIntArray()

        init {
            ORIENTATIONS.append(Surface.ROTATION_0, 90)
            ORIENTATIONS.append(Surface.ROTATION_90, 0)
            ORIENTATIONS.append(Surface.ROTATION_180, 270)
            ORIENTATIONS.append(Surface.ROTATION_270, 180)
        }
    }
    private val surfaceView: AutoFitSurfaceView by lazy { findViewById(R.id.surfaceView) }
    private val cameraManager: CameraManager by lazy { getSystemService(Context.CAMERA_SERVICE) as CameraManager }
    private val cameraId: String by lazy { cameraManager.cameraIdList[0] }
    private val characteristic: CameraCharacteristics by lazy {
        cameraManager.getCameraCharacteristics(cameraId)
    }
    private val cameraThread = HandlerThread("cameraThread").apply { start() }
    private val cameraHandler = Handler(cameraThread.looper)

    private lateinit var camera: CameraDevice
    private lateinit var session: CameraCaptureSession
    private lateinit var imageReader:ImageReader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_take)
        surfaceView.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder?) {
//                surfaceView.holder.setFixedSize(1920, 1080)
                surfaceView.setAspectRatio(1920,1080)
                surfaceView.post { initializeCamera() }
            }

            override fun surfaceChanged(
                holder: SurfaceHolder?,
                format: Int,
                width: Int,
                height: Int
            ) = Unit

            override fun surfaceDestroyed(holder: SurfaceHolder?) = Unit

        })
    }

    private fun initializeCamera() = lifecycleScope.launch(Dispatchers.Main) {
        camera = openCamera(cameraManager, cameraId, cameraHandler)
        val size = characteristic.get(
            CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)!!
            .getOutputSizes(ImageFormat.JPEG).maxByOrNull { it.height * it.width }!!
        imageReader = ImageReader.newInstance(
            size.width, size.height, ImageFormat.JPEG, 3
        )

        // Creates list of Surfaces where the camera will output frames
        val targets = listOf(surfaceView.holder.surface,imageReader.surface)
        session = createCaptureSession(camera,targets,cameraHandler)
        val captureRequest=camera.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW).apply {
            val rotation:Int=windowManager.defaultDisplay.rotation
            set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS[rotation])
            addTarget(surfaceView.holder.surface)
        }
        session.setRepeatingRequest(captureRequest.build(),null,cameraHandler)

    }

    @SuppressLint("MissingPermission")
    private suspend fun openCamera(
        manager: CameraManager,
        cameraId: String,
        handler: Handler?
    ): CameraDevice =
        suspendCancellableCoroutine { cont ->
            manager.openCamera(
                cameraId,
                object : CameraDevice.StateCallback() {
                    override fun onOpened(camera: CameraDevice) = cont.resume(camera)

                    override fun onDisconnected(camera: CameraDevice) {
                        finish()
                    }

                    override fun onError(camera: CameraDevice, error: Int) {
                        if (cont.isActive) {
                            val exe = RuntimeException("Camera $cameraId error:($error) ")
                            cont.resumeWithException(exe)
                        }
                    }

                }, handler
            )
        }

    private suspend fun createCaptureSession(
        device: CameraDevice,
        targets: List<Surface>,
        handler: Handler?
    ): CameraCaptureSession =
        suspendCancellableCoroutine { cont ->
            device.createCaptureSession(targets, object : CameraCaptureSession.StateCallback() {
                override fun onConfigured(session: CameraCaptureSession) = cont.resume(session)

                override fun onConfigureFailed(session: CameraCaptureSession) {
                    val exe = RuntimeException("Camera ${device.id} session configuration failed")
                    cont.resumeWithException(exe)
                }

            }, handler)

        }
}