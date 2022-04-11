package com.linc.download.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
//import androidx.room.PrimaryKey
import com.abc.lib_log.JLogUtils
import com.linc.download.constant.DownloadConstant
import com.linc.download.db.DownloadDB
import com.linc.download.listener.DownloadListener
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.structure.BaseModel

//import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
//import com.raizlabs.android.dbflow.annotation.Table
//import com.raizlabs.android.dbflow.structure.BaseModel

/**
 * author       : linc
 * time         : 2020/10/27
 * desc         : 下载的信息
 * version      : 1.0.0
 */
//@Entity
//data class DownloadInfo(
//    @PrimaryKey(autoGenerate = true)
//    var id: Long = 0,
//    @ColumnInfo
//    var url: String = "",
//    @ColumnInfo
//    var fileName: String = "",
//    @ColumnInfo
//    var createTime: Long = 0,
//    @ColumnInfo
//    var status: Int = DownloadConstant.DEFAULT_STATUS,
//    @ColumnInfo
//    var totalSize: Long = DownloadConstant.DEFALUT_TOTAL_SIZE,
//    @ColumnInfo
//    var type: String = "",
//    @ColumnInfo
//    var mimeType: String = "",
//    @ColumnInfo
//    var etag: String = "",
//    @ColumnInfo
//    var tmpFileName: String ="",
//    @ColumnInfo
//    var realFileName: String = "",
//    @ColumnInfo
//    var cover: String = "",
//    @ColumnInfo
//    var lastModified: String = "",
//    @ColumnInfo
//    var errorMsg: String ="",
//    @ColumnInfo
//    var tip: String = ""
//) {
//    //----------------------------------------- 未持久化信息 ----------------------------------
//    /**
//     * 监听器
//     */
//    @Ignore
//    var listener: DownloadListener? = null
//
//    /**
//     * 运行时状态【展示使用】
//     */
//    @Ignore
//    var curState: Int = 0
//
//    /**
//     * 进度 【0-100】
//     */
//    @Ignore
//    var percent: Int = 0
//
//    /**
//     * 判断 status 是否含有 type
//     * @param type Int
//     * @return Boolean
//     */
//    fun isStatusContains(type: Int) : Boolean = Status.contains(status, type)
//
//    /**
//     * 判断 curStatus 是否含有 type
//     * @param type Int
//     * @return Boolean
//     */
//    fun isCurStatusContains(type: Int) : Boolean = CurStatus.contains(curState, type)
//
//    /**
//     * 往 status 添加 type
//     * @param type Int
//     */
//    fun addStatus(type: Int) {
//        status = status or type
//    }
//
//    /**
//     * 从 status 移除 type
//     * @param type Int
//     */
//    fun removeStatus(type: Int) {
//        status = status and type.inv()
//    }
//
//    /**
//     * 往 curStatus 添加 type
//     * @param type Int
//     */
//    fun addCurStatus(type: Int) {
//        curState = curState or type
//    }
//
//    /**
//     * 往 curStatus 移除 type
//     * @param type Int
//     */
//    fun removeCurStatus(type: Int) {
//        curState = curState and  type.inv()
//    }
//
//    fun log(log: JLogUtils) {
//        log.title("DownloadInfo")
//            .param("id = $id")
//            .param("url = $url")
//            .param("fileName = $fileName")
//            .param("createTime = $createTime")
//            .param("status = ${Status.getStatus(status)} 【$status】")
//            .param("totalSize = $totalSize")
//            .param("type = $type")
//            .param("mimeType = $mimeType")
//            .param("etag = $etag")
//            .param("tmpFileName = $tmpFileName")
//            .param("realFileName = $realFileName")
//            .param("errorMsg = $errorMsg")
//            .param("tip = $tip")
//            .param("curStatus = $curState")
//            .param("listener = $listener")
//            .param("percent = $percent")
//    }
//}
//
@Table(database = DownloadDB::class)
class DownloadInfo : BaseModel() {
    // ============================ 持久化信息 ==================================
    @PrimaryKey(autoincrement = true)
    var id : Long = 0

    @Column
    var videoId: Long = 0

    /**
     * 下载URL 【必填】
     */
    @Column
    var url : String? = null

    /**
     * 文件名称，用户传入。状态未 init 时，会使用【必填】
     */
    @Column
    var fileName : String? = null

    /**
     * 创建时间【必填】
     */
    @Column
    var createTime: Long = 0

    /**
     * 资源当前状态【必填】
     */
    @Column(defaultValue = "${DownloadConstant.DEFAULT_STATUS}")
    var status: Int = 0

    //------------------------------------ 初始化 -------------------------------------
    /**
     * 总长度【初始化完填写】
     */
    @Column(defaultValue = "${DownloadConstant.DEFALUT_TOTAL_SIZE}")
    var totalSize: Long = 0

    /**
     * 类型【初始化完填写】
     */
    @Column
    var type: String? = null

    @Column
    var mimeType: String? = null

    /**
     * 资源标记【初始化完填写】
     */
    @Column
    var etag: String? = null

    /**
     * 临时文件名称【初始化完填写】
     * xxx.tmp
     */
    @Column
    var tmpFileName: String? = null

    /**
     * 真实文件名称【初始化完填写】
     * xxx.mp4
     */
    @Column
    var realFileName: String? = null

    /**
     * 封面
     */
    @Column
    var cover: String? = null

    /**
     * 最后修改时间
     */
    @Column
    var lastModified: String? = null

    // ---------------------------------------- 非正常信息 ---------------------------------
    /**
     * 错误信息
     */
    @Column
    var errorMsg: String? = null

    /**
     * 异常信息
     */
    @Column
    var tip: String? = null

    //----------------------------------------- 未持久化信息 ----------------------------------
    /**
     * 监听器
     */
    var listener: DownloadListener? = null

    /**
     * 运行时状态【展示使用】
     */
    var curState: Int = 0

    /**
     * 进度 【0-100】
     */
    var percent: Int = 0

    /**
     * 判断 status 是否含有 type
     * @param type Int
     * @return Boolean
     */
    fun isStatusContains(type: Int) : Boolean = Status.contains(status, type)

    /**
     * 判断 curStatus 是否含有 type
     * @param type Int
     * @return Boolean
     */
    fun isCurStatusContains(type: Int) : Boolean = CurStatus.contains(curState, type)

    /**
     * 往 status 添加 type
     * @param type Int
     */
    fun addStatus(type: Int) {
        status = status or type
    }

    /**
     * 从 status 移除 type
     * @param type Int
     */
    fun removeStatus(type: Int) {
        status = status and type.inv()
    }

    /**
     * 往 curStatus 添加 type
     * @param type Int
     */
    fun addCurStatus(type: Int) {
        curState = curState or type
    }

    /**
     * 往 curStatus 移除 type
     * @param type Int
     */
    fun removeCurStatus(type: Int) {
        curState = curState and  type.inv()
    }

    fun log(log: JLogUtils) {
        log.title("DownloadInfo")
            .param("id = $id")
            .param("url = $url")
            .param("fileName = $fileName")
            .param("createTime = $createTime")
            .param("status = ${Status.getStatus(status)} 【$status】")
            .param("totalSize = $totalSize")
            .param("type = $type")
            .param("mimeType = $mimeType")
            .param("etag = $etag")
            .param("tmpFileName = $tmpFileName")
            .param("realFileName = $realFileName")
            .param("errorMsg = $errorMsg")
            .param("tip = $tip")
            .param("curStatus = $curState")
            .param("listener = $listener")
            .param("percent = $percent")
    }
}




