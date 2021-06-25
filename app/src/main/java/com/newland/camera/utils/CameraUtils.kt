package com.newland.camera.utils

import android.graphics.Point
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CameraMetadata
import android.hardware.camera2.params.StreamConfigurationMap
import android.util.Size
import android.view.Display
import dalvik.annotation.TestTargetClass
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

/**
 * @author: leellun
 * @data: 25/6/2021.
 *
 */
object CameraUtils {
    val SIZE_1080P = Size(1080, 1920)
    fun isSupportCamera(cameraManager: CameraManager): Boolean {
        var cameraIds = cameraManager.cameraIdList
        if (cameraIds.isEmpty()) return false
        cameraIds.forEach {
            val cameraCharacteristics = cameraManager.getCameraCharacteristics(it)
            if (cameraCharacteristics.get(CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES)
                    ?.contains(CameraMetadata.REQUEST_AVAILABLE_CAPABILITIES_BACKWARD_COMPATIBLE) == true
            ) {
                return true
            }
        }
        return false
    }

    fun getFirstCameraIdFacing(
        cameraManager: CameraManager,
        facing: Int = CameraMetadata.LENS_FACING_BACK
    ): String {
        var cameraIds = cameraManager.cameraIdList.filter {
            var cameraCharacteristics = cameraManager.getCameraCharacteristics(it)
            var cameraCharacteristic =
                cameraCharacteristics.get(CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES)
            cameraCharacteristic?.contains(CameraMetadata.REQUEST_AVAILABLE_CAPABILITIES_BACKWARD_COMPATIBLE)
                ?: false
        }
        cameraIds.forEach {
            val characteristics = cameraManager.getCameraCharacteristics(it)
            if (characteristics.get(CameraCharacteristics.LENS_FACING) == facing) {
                return it
            }
        }
        return cameraIds.first()
    }

    fun getCameraIdFacing(
        cameraManager: CameraManager,
        facing: Int = CameraMetadata.LENS_FACING_FRONT
    ): String? {
        var cameraIds = cameraManager.cameraIdList.filter {
            var cameraCharacteristics = cameraManager.getCameraCharacteristics(it)
            var cameraCharacteristic =
                cameraCharacteristics.get(CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES)
            cameraCharacteristic?.contains(CameraMetadata.REQUEST_AVAILABLE_CAPABILITIES_BACKWARD_COMPATIBLE)
                ?: false
        }
        cameraIds.forEach {
            val characteristics = cameraManager.getCameraCharacteristics(it)
            if (characteristics.get(CameraCharacteristics.LENS_FACING) == facing) {
                return it
            }
        }
        return null
    }

    fun getCameraFacing(cameraManager: CameraManager, cameraId: String): Int {
        val cameraCharacteristics = cameraManager.getCameraCharacteristics(cameraId)
        return cameraCharacteristics.get(CameraCharacteristics.LENS_FACING) as Int
    }

    fun <T> getOutputSize(
        display: Display,
        cameraManager: CameraManager,
        cameraId: String,
        width: Int,
        height: Int,
        targetClass: Class<T>,
        format: Int? = null
    ): Size {
        val outPoint = Point()
        display.getRealSize(outPoint)
        val maxWidth = min(min(outPoint.x, outPoint.y), SIZE_1080P.width)
        val maxHeight = min(max(outPoint.x, outPoint.y), SIZE_1080P.height)

        val configurationMap = cameraManager.getCameraCharacteristics(cameraId)
            .get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)!!
        if (format == null) {
            assert(StreamConfigurationMap.isOutputSupportedFor(targetClass))
        } else {
            assert(configurationMap.isOutputSupportedFor(format))
        }
        val allSizes =
            if (format == null)
                configurationMap.getOutputSizes(targetClass)
            else configurationMap.getOutputSizes(format)
        return allSizes.sortedWith(compareBy {
            it.width*it.height
        }).reversed().first {
            it.width <= maxWidth && it.height <= maxHeight
        }
    }

}