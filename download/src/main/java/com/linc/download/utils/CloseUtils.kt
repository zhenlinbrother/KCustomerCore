package com.linc.download.utils

import java.io.Closeable
import java.lang.Exception
import java.net.Socket
/**
 * author       : linc
 * time         : 2020/10/27
 * desc         : 流工具
 * version      : 1.0.0
 */
object CloseUtils {

    /**
     * 关闭流
     * @param closeable Closeable?
     */
    fun close(closeable: Closeable?) {

        try {
            closeable?.close()
        } catch (e: Exception){
            e.printStackTrace()
        }
    }

    /**
     * 关闭 socket
     * @param socket Socket?
     */
    fun close(socket: Socket?) {
        try {
            socket?.close()
        } catch (e: Exception){
            e.printStackTrace()
        }
    }

    fun Closeable.closeable() {
        try {
            this.close()
        } catch (e : Exception) {
            e.printStackTrace()
        }
    }
}