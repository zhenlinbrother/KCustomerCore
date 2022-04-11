package com.linc.download.jerry

import com.linc.download.model.DownloadInfo

/**
 * author       : liaozhenlin
 * time         : 2022/1/11
 * desc         : 下载接口
 */
interface IJerryDownload {

    fun download(videoId: Long, url: String, fileName: String, userId: Int, domain: String, cover: String) : DownloadInfo

    fun download(videoId: Long, url: String, fileName: String) : DownloadInfo

    fun download(downloadInfo: DownloadInfo) : DownloadInfo

    fun stopTask(downloadInfo: DownloadInfo)

    fun getDownloadInfo(userId: Int, domain: String) : List<DownloadInfo>

    fun getDownloadInfo(videoId: Long) : DownloadInfo?

    fun removeDownloadInfo(downloadInfo: DownloadInfo)

    fun delete(downloadInfo: DownloadInfo)
}