//package com.linc.download.manager
//
//import android.app.Application
//import androidx.room.Room
//import androidx.room.migration.Migration
//import androidx.sqlite.db.SupportSQLiteDatabase
//import com.linc.download.db.DownloadDB
//import com.linc.download.db.DownloadDao
//
//object DatabaseManager {
//    private const val DB_NAME = "download.db"
//    private lateinit var application: Application
//    private val MIGRATIONS = arrayOf(Migration1)
//    val db: DownloadDB by lazy {
//        Room.databaseBuilder(application.applicationContext, DownloadDB::class.java, DB_NAME)
//            .build()
//    }
//
//    fun saveApplication(application: Application) {
//        DatabaseManager.application = application
//    }
//
//
//    private object Migration1 : Migration(1,2) {
//        override fun migrate(database: SupportSQLiteDatabase) {
//
//        }
//
//    }
//
//    fun getDownloadDao() : DownloadDao = db.downloadDao
//
//}