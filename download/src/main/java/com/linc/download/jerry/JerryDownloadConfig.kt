package com.linc.download.jerry

import android.content.Context
import com.linc.download.config.DownloadConfig
import com.linc.download.db.DownloadDB
import com.linc.download.okhttp.DownloadOkHttpHelper
import com.raizlabs.android.dbflow.config.DatabaseConfig
import com.raizlabs.android.dbflow.config.FlowConfig
import com.raizlabs.android.dbflow.config.FlowManager
import com.raizlabs.android.dbflow.config.JerryDownloadGeneratedDatabaseHolder
import okhttp3.OkHttpClient


object JerryDownloadConfig {

    fun init(context: Context) {
        FlowManager
            .init(
                FlowConfig.builder(context)
                    .addDatabaseConfig(
                        DatabaseConfig.builder(DownloadDB::class.java)
                            .databaseName(DownloadDB.NAME)
                            .build()
                    )
                    .build()
            )

        FlowManager.initModule(JerryDownloadGeneratedDatabaseHolder::class.java)
    }

    /**
     * 设置okHttp
     */
    fun setOkHttp(okHttpClient: OkHttpClient) {
        DownloadOkHttpHelper.setOkHttpClient(okHttpClient)
    }

    /**
     * 设置下载的线程数
     */
    fun setThreadCount(count: Int) {
        DownloadConfig.instance?.threadCount = count
    }

    /**
     * 设置下载路径
     */
    fun setDownloadFolder(path: String) {
        DownloadConfig.instance?.downloadFile = path
    }
}