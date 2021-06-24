package com.newland.camera.extend

import android.app.Activity
import android.content.Intent

/**
 * @author: leellun
 * @data: 24/6/2021.
 *
 */
fun  Activity.startActivity(activityClazz: Class<out Activity>) {
    startActivity(Intent(this, activityClazz))
}