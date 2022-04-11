package com.linc.download.db

import androidx.room.Database
//import androidx.room.RoomDatabase
//import com.linc.download.model.DownloadInfo


/**
 * author       : linc
 * time         : 2020/10/25
 * desc         : 数据库基本配置
 * version      : 1.0.0
 */
@com.raizlabs.android.dbflow.annotation.Database(version = DownloadDB.VERSION)
class DownloadDB {
    companion object {
        const val VERSION = 1

        const val NAME = "JerryDownload"
    }
}
//@Database(version = 1, exportSchema = false, entities = [DownloadInfo::class])
//abstract class DownloadDB : RoomDatabase() {
//
//    val downloadDao: DownloadDao by lazy { createDownloadDao() }
//
//    abstract fun createDownloadDao(): DownloadDao
//}