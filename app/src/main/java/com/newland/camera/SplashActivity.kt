package com.newland.camera

import android.Manifest
import android.content.Context
import android.hardware.camera2.CameraManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.newland.camera.extend.showToast
import com.newland.camera.extend.startActivity
import com.newland.camera.utils.CameraUtils

/**
 * @author: leellun
 * @data: 24/6/2021.
 *
 */
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        if (!CameraUtils.isSupportCamera(getSystemService(Context.CAMERA_SERVICE) as CameraManager)) {
            showToast("您的手机不支持相机功能")
            finish()
            return;
        }
        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO
            ), 1
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        startActivity(MainActivity::class.java)
        finish()
    }
}