package com.newland.camera.manager

import android.os.Environment

class FileManager {
    companion object {
        val instance: FileManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { FileManager() }
    }

    private constructor()

    fun getPicture(file: String): String {
        return "${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).absolutePath}/Camera/$file"
    }
}