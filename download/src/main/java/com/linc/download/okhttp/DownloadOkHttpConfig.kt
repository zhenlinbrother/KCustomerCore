package com.linc.download.okhttp
/**
 * author       : linc
 * time         : 2020/10/28
 * desc         : 下载 OKHTTP 配置
 * version      : 1.0.0
 */
interface DownloadOkHttpConfig {

    companion object {
        const val CONNECT_TIME_OUT = 30
        const val READ_TIME_OUT = 30
        const val WRITE_TIME_OUT = 30
    }
}