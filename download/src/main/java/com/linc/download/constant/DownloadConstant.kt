package com.linc.download.constant

import com.linc.download.model.Status

/**
 * author       : linc
 * time         : 2020/10/27
 * desc         : 下载的常量
 * version      : 1.0.0
 */
interface DownloadConstant {

    companion object{

        /**
         * 默认下载状态
         */
        const val DEFAULT_STATUS = Status.INIT

        /**
         * 默认的总长度
         */
        const val DEFALUT_TOTAL_SIZE = -1

        /**
         * 临时文件后缀
         */
        const val TMP = ".tmp"

        const val DEFAULT_USER_ID = -1

        const val DEFAULT_DOMAIN = "JerryDownload"
    }

}