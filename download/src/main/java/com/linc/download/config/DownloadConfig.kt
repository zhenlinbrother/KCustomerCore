package com.linc.download.config
/**
 * author       : linc
 * time         : 2020/10/27
 * desc         : 下载设置
 * version      : 1.0.0
 */
class DownloadConfig private constructor() {
    var downloadFile = "jerry/download"
    var threadCount = 3

    companion object{
        @Volatile
        var instance: DownloadConfig? = null
            get() {
                if (field == null) {
                    synchronized(DownloadConfig::class.java){
                        if (field == null){
                            field = DownloadConfig()
                        }
                    }
                }
                return field
            }
    }
}

