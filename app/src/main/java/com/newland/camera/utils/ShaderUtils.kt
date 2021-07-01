package com.newland.opengl.utils

import android.opengl.GLES30
import android.util.Log

object ShaderUtils {
    fun compileVertexShader(shaderCode: String) = compileShader(GLES30.GL_VERTEX_SHADER, shaderCode)
    fun compileFragmentShader(shaderCode: String) =
        compileShader(GLES30.GL_FRAGMENT_SHADER, shaderCode)

    private fun compileShader(type: Int, shaderCode: String): Int {
        //創建着色器
        val shaderId = GLES30.glCreateShader(type)
        if (shaderId != 0) {
            //
            GLES30.glShaderSource(shaderId, shaderCode)
            GLES30.glCompileShader(shaderId)
            //检测状态
            val compileStatus = IntArray(1)
            GLES30.glGetShaderiv(shaderId, GLES30.GL_COMPILE_STATUS, compileStatus, 0)
            if (compileStatus[0] == 0) {
                val infoLen = IntArray(1)
                GLES30.glGetShaderiv(shaderId, GLES30.GL_INFO_LOG_LENGTH, infoLen, 0)
                if (infoLen[0] > 0) {
                    var loginfo = GLES30.glGetShaderInfoLog(shaderId)
                    Log.e("logoinfo", loginfo)
                }
                //检测失败
                GLES30.glDeleteShader(shaderId)
                return 0
            }
            return shaderId
        } else {
            return 0
        }
    }

    fun linkProgram(vertexShaderId: Int, fragmentShaderId: Int): Int =
        likeProgram(vertexShaderId, fragmentShaderId)

    fun likeProgram(vertexShaderId: Int, fragmentShaderId: Int): Int {
        //创建一个空的OpenGLES程序
        val programId = GLES30.glCreateProgram()
        if (programId != 0) {
            //将顶点着色器加入到程序
            GLES30.glAttachShader(programId, vertexShaderId)
            //将片元着色器加入到程序中
            GLES30.glAttachShader(programId, fragmentShaderId)
            //链接着色器程序
            GLES30.glLinkProgram(programId)
            //检测状态
            val linkStatus = IntArray(1)
            GLES30.glGetProgramiv(programId, GLES30.GL_LINK_STATUS, linkStatus, 0)
            if (linkStatus[0] == 0) {
                val infoLen = IntArray(1)
                GLES30.glGetProgramiv(programId, GLES30.GL_INFO_LOG_LENGTH, infoLen, 0)
                if (infoLen[0] > 0) {
                    val logInfo = GLES30.glGetProgramInfoLog(programId)
                    Log.e("logoinfo", logInfo)
                }
                GLES30.glDeleteProgram(programId)
                return 0
            }
            return programId
        } else {
            return 0
        }
    }

    fun validProgram(programId: Int): Boolean {
        GLES30.glValidateProgram(programId)
        val programStatus = IntArray(1)
        GLES30.glGetProgramiv(programId, GLES30.GL_VALIDATE_STATUS, programStatus, 0)
        return programStatus[0] != 0
    }
}