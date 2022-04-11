package com.linc.download.db

import androidx.room.*
import com.linc.download.model.DownloadInfo

@Dao
interface DownloadDao {

    @Insert
    suspend fun save(downloadInfo: DownloadInfo) : Long

    @Query("select * from downloadinfo order by createTime DESC")
    suspend fun getDownloadInfo(): MutableList<DownloadInfo>

    @Delete
    suspend fun delete(downloadInfo: DownloadInfo)

    @Update
    suspend fun update(downloadInfo: DownloadInfo)
}