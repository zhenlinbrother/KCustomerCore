package com.linc.download.model
/**
 * author       : linc
 * time         : 2020/10/25
 * desc         : 当前状态值
 * version      : 1.0.0
 */
interface CurStatus {
    companion object {

        /**
         * 暂停
         */
        const val PAUSE = 1

        /**
         * 等待下载
         */
        const val WAITING = 1 shl 1

        /**
         * 初始化下载
         */
        const val INIT = 1 shl 2

        /**
         * 正在下载
         */
        const val DOWNLOADING = 1 shl 3

        /**
         * 下载错误
         */
        const val ERROR = 1 shl 4

        /**
         * 下载异常
         */
        const val TIP = 1 shl 5

        /**
         * 下载完成
         */
        const val FINISH = 1 shl 6

        /**
         * status 是否含有 type
         * @param status Int
         * @param type Int
         * @return Boolean
         */
        fun contains(status: Int, type: Int) : Boolean{
            return status and type == type
        }

        /**
         * 获取当前状态值的解释
         * @param type Int
         * @return String
         */
        fun getStatus(type: Int) : String {
            return when(type) {
                PAUSE -> "PAUSE"
                WAITING -> "WAITING"
                INIT -> "INIT"
                DOWNLOADING -> "DOWNLOADING"
                ERROR -> "ERROR"
                TIP -> "TIP"
                FINISH -> "FINISH"
                else -> "UNKNOWN"
            }
        }
    }
}