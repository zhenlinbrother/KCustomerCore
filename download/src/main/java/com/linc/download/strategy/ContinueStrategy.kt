package com.linc.download.strategy

import com.abc.lib_log.JLogUtils
import com.linc.download.constant.ReqHead
import com.linc.download.model.DownloadInfo
import com.linc.download.utils.DownloadFileUtils
import com.linc.download.utils.NetUtils
import java.io.IOException
import java.net.UnknownHostException

class ContinueStrategy : BaseStrategy{

    constructor(mDownloadInfo: DownloadInfo, mStrategyName: String, mLog: JLogUtils) : super(
        mDownloadInfo,
        "ContinueStrategy",
        mLog
    )

    override fun run(): Boolean {

        mLog.content("下载策略：$mStrategyName")

        val headMap: HashMap<String, String> = HashMap()
        headMap[ReqHead.If_None_Match] = headMap.toString()

        try {
            val request = NetUtils.request(mDownloadInfo, headMap)
            mResponse = request!!.execute()
        } catch (e : UnknownHostException) {
            setTipStatus("网络异常")
            return false
        } catch (e: IOException) {
            mLog.content("EXCEPTION: ${e.message}")
        }

        if (mResponse == null) {
            setTipStatus("服务器异常")
            return false
        }

        //说明之前的资源没变动
        if (mResponse?.code == 304) {
            return true
        }

        if (!mResponse!!.isSuccessful){
            setTipStatus("服务器异常")
            return false
        }

        val isDel = mDownloadInfo.realFileName?.let { DownloadFileUtils.deleteFile(it) }
        if (!isDel!!) {
            setErrorStatus("文件异常")
            return false
        }

        val file = mDownloadInfo.realFileName?.let { DownloadFileUtils.createFile(it) }
        if (file == null) {
            setErrorStatus("文件异常")
            return false
        }

        return true
    }
}