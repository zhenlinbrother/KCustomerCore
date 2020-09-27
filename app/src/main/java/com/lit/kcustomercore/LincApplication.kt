package com.lit.kcustomercore

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex

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
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    companion object {
        lateinit var context: Context
    }
}