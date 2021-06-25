package com.newland.camera.extend

import android.app.Activity
import android.content.Intent
import android.widget.Toast

/**
 * @author: leellun
 * @data: 24/6/2021.
 *
 */
fun  Activity.startActivity(activityClazz: Class<out Activity>) {
    startActivity(Intent(this, activityClazz))
}
fun Activity.showToast(msg:String){
    Toast.makeText(this,msg,Toast.LENGTH_SHORT).show()
}