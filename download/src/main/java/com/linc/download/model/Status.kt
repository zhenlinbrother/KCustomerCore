package com.linc.download.model
/**
 * author       : linc
 * time         : 2020/10/25
 * desc         : 状态
 * version      : 1.0.0
 */
interface Status {
    companion object {
        /**
         * 初始化
         */
        const val INIT = 1

        /**
         * 下载
         */
        const val DOWNLOAD = 1 shl 1

        /**
         * 完成
         */
        const val FINISH = 1 shl 2

        /**
         * 错误
         */
        const val ERROR = 1 shl 3

        /**
         * 异常
         */
        const val TIP = 1 shl 4

        /**
         * status 是否含有 type
         * @param status Int 状态
         * @param type Int   类型
         * @return Boolean   布尔值
         */
        fun contains(status: Int, type: Int) : Boolean {
            return status and type == type
        }

        /**
         * 获取状态值的解释
         * @param type Int  状态值
         * @return String
         */
        fun getStatus(type: Int) : String {
            return when(type) {
                INIT -> "INIT"
                DOWNLOAD -> "DOWNLOAD"
                FINISH -> "FINISH"
                ERROR -> "ERROR"
                TIP -> "TIP"
                else -> "UNKNOWN"
            }
        }


    }
}
