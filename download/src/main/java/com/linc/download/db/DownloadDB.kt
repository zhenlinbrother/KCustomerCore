package com.linc.download.db

import com.raizlabs.android.dbflow.annotation.Database


/**
 * author       : linc
 * time         : 2020/10/25
 * desc         : 数据库基本配置
 * version      : 1.0.0
 */
@Database(version = DownloadDB.VERSION)
class DownloadDB {
    companion object {
        const val VERSION = 1

        const val NAME = "JerryDownload"
    }
}