package com.lit.kcustomercore.utils

import android.util.Log
import com.google.gson.JsonSyntaxException
import com.lit.kcustomercore.net.exception.ResponseCodeException
import java.net.ConnectException
import java.net.NoRouteToHostException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * author       : linc
 * time         : 2020/9/28
 * desc         : 获取网络请求返回的异常信息
 * version      : 1.0.0
 */
object ResponseHandler {
    private const val TAG = "ResponseHandler"

    fun getFailureTips(e: Throwable?): String {
        Log.w(TAG, "getFailureTips: is ${e?.message}")
        return when(e){
            is ConnectException -> "网络连接异常"
            is SocketTimeoutException -> "网络连接超时"
            is ResponseCodeException -> "服务器状态码异常: " + e.responseCode
            is NoRouteToHostException -> "无法连接到服务器"
            is UnknownHostException -> "网络错误"
            is JsonSyntaxException -> "数据解析异常"
            else -> "发生未知错误"
        }
    }
}