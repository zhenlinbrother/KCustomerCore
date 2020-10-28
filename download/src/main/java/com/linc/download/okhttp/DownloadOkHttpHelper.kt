package com.linc.download.okhttp

import com.linc.download.config.DownloadConfig
import okhttp3.OkHttpClient
import java.sql.Time
import java.util.concurrent.TimeUnit

object DownloadOkHttpHelper  {
    @Volatile
    private var OK_HTTP_CLIENT: OkHttpClient? = null

    private val LOCK = Any()

    fun setOkHttpClient(okHttpClient: OkHttpClient?) {
        synchronized(LOCK) {
            OK_HTTP_CLIENT = okHttpClient
        }
    }

    val okHttpInstance: OkHttpClient?
        get() {
            if (OK_HTTP_CLIENT == null) {
                synchronized(LOCK) {
                    if (OK_HTTP_CLIENT == null) {
                        val builder = OkHttpClient.Builder()
                        OK_HTTP_CLIENT = builder
                            .connectTimeout(DownloadOkHttpConfig.CONNECT_TIME_OUT.toLong(), TimeUnit.SECONDS)
                            .readTimeout(DownloadOkHttpConfig.READ_TIME_OUT.toLong(), TimeUnit.SECONDS)
                            .writeTimeout(DownloadOkHttpConfig.WRITE_TIME_OUT.toLong(), TimeUnit.SECONDS)
                            .build()
                    }
                }
            }
            return OK_HTTP_CLIENT
        }
}

