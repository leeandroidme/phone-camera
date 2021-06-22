package com.newland.camera.common

import com.newland.camera.beans.TakeOperation

class TakeOptionConstant {
    companion object {
        val DELAY_PLAY = 1
        val SLOW_PLAY = 2
        val VEDIO = 3
        val TAKE_PHOTO = 4
        val SEQUARE = 5
        val FULL = 6
        public fun getTakeOperations(): MutableList<TakeOperation> {
            var list = mutableListOf<TakeOperation>()
            list.add(TakeOperation(DELAY_PLAY, "延迟摄影"))
            list.add(TakeOperation(SLOW_PLAY, "慢动作"))
            list.add(TakeOperation(VEDIO, "视频"))
            list.add(TakeOperation(TAKE_PHOTO, "照片"))
            list.add(TakeOperation(SEQUARE, "正方形"))
            list.add(TakeOperation(FULL, "全景"))
            return list
        }
    }
}