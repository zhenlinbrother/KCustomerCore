package com.linc.download.utils

import com.linc.download.model.DownloadInfo
import com.linc.download.okhttp.DownloadOkHttpHelper
import okhttp3.Call
import okhttp3.Request
import java.io.IOException

object NetUtils {

    @Throws(IOException::class)
    fun request(downloadInfo: DownloadInfo, headers: HashMap<String, String>?) : Call?{
        //进行网络请求
        val builder = Request.Builder()
        builder.url(downloadInfo.url!!)

        if (headers != null) {
            for ((key, value) in headers) {
                builder.addHeader(key, value!!)
            }
        }

        return DownloadOkHttpHelper.okHttpInstance!!.newCall(builder.build())
    }
}

