package com.linc.download.strategy

import com.abc.lib_log.JLogUtils
import com.linc.download.constant.DownloadConstant
import com.linc.download.constant.RespHead
import com.linc.download.model.DownloadInfo
import com.linc.download.model.Status
import com.linc.download.utils.CloseUtils
import com.linc.download.utils.DownloadFileUtils
import com.linc.download.utils.NetUtils
import java.io.File
import java.io.IOException
import java.net.UnknownHostException

/**
 * author       : linc
 * time         : 2020/10/29
 * desc         : 首次传输策略
 * version      : 1.0.0
 */
class FirstStrategy : BaseStrategy {


    constructor(mDownloadInfo: DownloadInfo, mStrategyName: String, mLog: JLogUtils) : super(
        mDownloadInfo,
        "FirstStrategy",
        mLog
    )

    override fun run(): Boolean {

        mLog.content("下载策略：$mStrategyName")

        try {
            mCall = NetUtils.request(mDownloadInfo, null)
            mResponse = mCall?.execute()
        } catch (e: UnknownHostException) {
            setTipStatus("网络异常")
            return false
        } catch (e: IOException) {
            mLog.content("EXCEPTION: ${e.message}")
        }

        if (mResponse == null || !mResponse!!.isSuccessful) {
            setTipStatus("服务器异常")
            return false
        }

        val body = mResponse?.body

        if (body == null) {
            setTipStatus("服务器异常")
            return false
        }

        val contentLength = body.contentLength()
        if (contentLength <= 0) {
            setTipStatus("服务器异常")
            return false
        }

        val mediaType = body.contentType()

        if (mediaType == null) {
            setTipStatus("服务器异常")
            return false
        }

        val etag = mResponse?.header(RespHead.ETAG)

        mDownloadInfo.totalSize = contentLength
        mDownloadInfo.type = mediaType.subtype
        mDownloadInfo.mimeType = mediaType.toString()
        mDownloadInfo.etag = etag

        val fileName = calculateFileName()

        if (fileName == null) {
            setTipStatus("文件夹创建失败")
            return false
        }

        mDownloadInfo.status = Status.DOWNLOAD
        mDownloadInfo.update()

        CloseUtils.close(mResponse)
        return true
    }

    /**
     * 获取文件名称
     * @return String?
     */
    private fun calculateFileName() : String? {
        var fileName = mDownloadInfo.fileName

        var i = 1

        val dotIndex = fileName!!.lastIndexOf(".")
        if (dotIndex > 0) {
            fileName = fileName.substring(0, dotIndex)
        }

        var tmp = fileName
        var tmpFileName = tmp + DownloadConstant.TMP
        var realFileName = tmp + "." + mDownloadInfo.type

        var result: File? = null
        var fileExit = true
        while (fileExit) {
            synchronized(LOCK){
                val tmpFileExist = DownloadFileUtils.isExist(tmpFileName)
                val realFileExist = DownloadFileUtils.isExist(realFileName)
                if (!tmpFileExist && !realFileExist) {
                    result = DownloadFileUtils.createFile(tmpFileName)

                    mDownloadInfo.tmpFileName = tmpFileName
                    mDownloadInfo.realFileName = realFileName
                    fileExit = false
                    return@synchronized
                }

                tmp = "$fileName($i)"
                tmpFileName = tmp + DownloadConstant.TMP
                realFileName = "$tmp.${mDownloadInfo.type}"

                ++i
            }
        }

        if (result == null) {
            return null
        }

        return tmpFileName

    }

    companion object {
        private val LOCK = Any()
    }
}



