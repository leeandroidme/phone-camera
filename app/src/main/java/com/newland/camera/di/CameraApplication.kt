package com.newland.camera.di

import android.app.Application

/**
 * @author: leellun
 * @data: 1/7/2021.
 *
 */
class CameraApplication : Application() {
    companion object {
        lateinit var application: CameraApplication
    }

    override fun onCreate() {
        super.onCreate()
        application = this
    }
}