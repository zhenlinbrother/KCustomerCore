package com.lit.kcustomercore

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.linc.download.jerry.JerryDownload
import com.linc.download.jerry.JerryDownloadConfig
//import com.linc.download.manager.DatabaseManager

/**
 * author       : linc
 * time         : 2020/9/25
 * desc         : 自定义application
 * version      : 1.0.0
 */
class LincApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        context = this

        JerryDownloadConfig.setThreadCount(3)
        JerryDownloadConfig.setDownloadFolder("linc/download")
        JerryDownloadConfig.init(this)

//        DatabaseManager.saveApplication(this)
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    companion object {
        lateinit var context: Context
    }
}