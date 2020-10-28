package com.linc.download.listener
/**
 * author       : linc
 * time         : 2020/10/27
 * desc         : 下载监听
 * version      : 1.0.0
 */
interface DownloadListener {

    /**
     * 暂停
     */
    fun onPause()

    /**
     * 等待下载
     */
    fun onWaiting()

    /**
     * 初始化
     */
    fun onInit()

    /**
     * 开始下载
     */
    fun onDownloading()

    /**
     * 异常
     */
    fun onTip()

    /**
     * 错误
     */
    fun onError()

    /**'
     * 完成
     */
    fun onFinish()

    /**
     * 进度
     */
    fun onProgress()
}