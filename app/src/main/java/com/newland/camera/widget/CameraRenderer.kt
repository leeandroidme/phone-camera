package com.newland.camera.widget

import android.opengl.GLSurfaceView
import com.newland.camera.utils.TextureUtils
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class CameraRenderer : GLSurfaceView.Renderer {

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        gl?.also { gl ->
            //设置背景色
            gl.glClearColor(0f, 0f, 0f, 0f)
            //阴影平滑
            gl.glShadeModel(GL10.GL_SMOOTH)
            //设置深度值
            gl.glClearDepthf(1.0f)
            //深度测试 在涉及到消隐等情况（可能遮挡）都要开启深度测试 硬件上打开了深度缓存区，当有新的同样XY 坐标的片
            //断到来时，比较两者的深度
            gl.glEnable(GL10.GL_DEPTH_TEST)
            //一般设为1，这将背景设为最深，这是默认的
            gl.glClearDepthx(1)
            //深度检测设置
            gl.glDepthFunc(GL10.GL_LEQUAL)
            gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST)
        }

    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        gl?.also { gl ->
            //负责把视景体截取的图像按照怎样的高和宽显示到屏幕上
            gl.glViewport(0, 0, width, height)
            /*
             *GL_MODELVIEW,对模型视景矩阵堆栈应用随后的矩阵操作。 GL_PROJECTION,对投影矩阵应用随后的矩阵操作. GL_TEXTURE,对纹理矩阵堆栈应用随后的矩阵操作.
             */
            gl.glMatrixMode(GL10.GL_PROJECTION)
            //恢复初始坐标系 重置当前指定的矩阵为单位矩阵
            gl.glLoadIdentity()

            val fovy = 20.0
            val eyeZ = height / 2f / Math.tan(TextureUtils.d2r(fovy / 2)).toFloat()


        }
    }

    override fun onDrawFrame(gl: GL10?) {
    }
}