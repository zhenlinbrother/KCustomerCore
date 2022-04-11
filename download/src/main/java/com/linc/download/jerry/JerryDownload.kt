package com.linc.download.jerry

import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import com.abc.lib_log.JLogUtils
import com.linc.download.config.DownloadConfig
import com.linc.download.constant.DownloadConstant
//import com.linc.download.manager.DatabaseManager
import com.linc.download.model.CurStatus
import com.linc.download.model.DownloadInfo
import com.linc.download.model.DownloadInfo_Table
//import com.linc.download.model.DownloadInfo_Table
import com.linc.download.model.Status
import com.linc.download.thread.DownloadThread
import com.linc.download.utils.DownloadFileUtils
import com.linc.download.utils.EncryptionUtils
import com.raizlabs.android.dbflow.sql.language.OrderBy
import com.raizlabs.android.dbflow.sql.language.SQLite
import com.raizlabs.android.dbflow.sql.language.Where
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
//import com.raizlabs.android.dbflow.sql.language.OrderBy
//import com.raizlabs.android.dbflow.sql.language.SQLite
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.collections.HashMap

class JerryDownload : IJerryDownload{

    private var mDownload: Download = Download()

    companion object {
        private const val TAG = "JerryDownload"
        private const val BOUND = 100000
        @Volatile
        var instance: JerryDownload? = null
            get() {
                if (field == null) {
                    synchronized(JerryDownload::class) {
                        field = JerryDownload()
                    }
                }
                return field
            }
    }
    override fun download(
        videoId: Long,
        url: String,
        fileName: String,
        userId: Int,
        domain: String,
        cover: String
    ): DownloadInfo {
        val downloadInfo = DownloadInfo()
        downloadInfo.url = url
        var filename = fileName
        if (filename.isNullOrEmpty()) {
            filename = obtainRandomName(url)
        }
        downloadInfo.fileName = filename
        downloadInfo.createTime = System.currentTimeMillis()
        downloadInfo.status = Status.INIT
        downloadInfo.cover = cover
        downloadInfo.videoId = videoId
        return download(downloadInfo)
    }

    override fun download(videoId: Long, url: String, fileName: String): DownloadInfo {
        return download(videoId, url, fileName,
            DownloadConstant.DEFAULT_USER_ID,
            DownloadConstant.DEFAULT_DOMAIN,
            "")
    }

    override fun download(downloadInfo: DownloadInfo): DownloadInfo {
        return mDownload.submitTask(downloadInfo)!!
    }

    override fun stopTask(downloadInfo: DownloadInfo) {
        mDownload.stopTask(downloadInfo)
    }

    override fun getDownloadInfo(userId: Int, domain: String): List<DownloadInfo> {
        synchronized(mDownload.LOCK) {

            //按照添加时间，由 近期 到 久远的时间获取
            val orderBy = OrderBy
                .fromProperty(DownloadInfo_Table.createTime)
                .descending()

            val downloadInfos = SQLite.select()
                .from(DownloadInfo::class.java)
                .orderBy(orderBy)
                .queryList()
//            val downloadInfos = DatabaseManager.db.downloadDao.getDownloadInfo()

            //按照 status 填充 运行时curStatus 的值
            for (i in 0 .. downloadInfos.size) {

                val downloadInfo = downloadInfos[i]
                //错误状态
                if (downloadInfo.isStatusContains(Status.ERROR)) {
                    downloadInfo.curState = CurStatus.ERROR
                    continue
                }

                //完成的状态
                if (downloadInfo.isStatusContains(Status.FINISH)) {
                    downloadInfo.curState = CurStatus.FINISH
                    continue
                }

                // 是否存在运行的线程中
                var isExist = false

                // 获取 本地临时文件的信息，并设置其进度
                var tmpFileName = downloadInfo.tmpFileName
                if (!tmpFileName.isNullOrEmpty()) {
                    val tempFile = DownloadFileUtils.getFile(tmpFileName)

                    //临时文件存在，则计算其进度
                    if (tempFile?.exists() == true) {
                        val percent = DownloadFileUtils.calculatePercent(tempFile.length(), downloadInfo.totalSize)
                        downloadInfo.percent = percent
                    }
                }

                // 将从数据库中查找的对象替换成线程池中的对象
                for ((key) in mDownload.mDownloadInfoMap) {
                    if (key.id == downloadInfo.id) {
                        downloadInfos.remove(downloadInfo)
                        downloadInfos.add(i, key)
                        isExist = true
                        break
                    }
                }

                //不存在，则添加为暂停
                if (!isExist) {
                    downloadInfo.curState = CurStatus.PAUSE
                }

                //如果存在异常，则将异常状态 "添加"
                if (downloadInfo.isStatusContains(Status.TIP)) {
                    downloadInfo.addCurStatus(CurStatus.TIP)
                }
            }

            return downloadInfos
        }
    }

    override fun getDownloadInfo(videoId: Long) : DownloadInfo?{
        synchronized(mDownload.LOCK) {
            val downloadInfo = SQLite.select()
                .from(DownloadInfo::class.java)
                .where(DownloadInfo_Table.videoId.eq(videoId))
                .querySingle()

            //错误状态
            if (downloadInfo?.isStatusContains(Status.ERROR) == true) {
                downloadInfo.curState = CurStatus.ERROR
            }

            //完成的状态
            if (downloadInfo?.isStatusContains(Status.FINISH) == true) {
                downloadInfo.curState = CurStatus.FINISH
            }

            // 获取 本地临时文件的信息，并设置其进度
            var tmpFileName = downloadInfo?.tmpFileName
            if (!tmpFileName.isNullOrEmpty()) {
                val tempFile = DownloadFileUtils.getFile(tmpFileName)

                //临时文件存在，则计算其进度
                if (tempFile?.exists() == true) {
                    val percent = DownloadFileUtils.calculatePercent(tempFile.length(), downloadInfo!!.totalSize)
                    downloadInfo.percent = percent
                }
            }

            return downloadInfo
        }
    }

    override fun removeDownloadInfo(downloadInfo: DownloadInfo) {
        mDownload.removeThread(downloadInfo)
    }

    override fun delete(downloadInfo: DownloadInfo) {
        mDownload.delete(downloadInfo)
    }

    private fun obtainRandomName(url: String) : String =
        EncryptionUtils.md5(url) + "-" + System.currentTimeMillis()

    inner class Download {
        val mThreadPool: ExecutorService = Executors.newFixedThreadPool(DownloadConfig.instance!!.threadCount)
        val mDownloadInfoMap: HashMap<DownloadInfo, DownloadThread> = HashMap()
        val LOCK: Any = Any()
        val mRandom: Random = Random()
        val mHandler: Handler = Handler(Looper.getMainLooper())

        fun submitTask(downloadInfo: DownloadInfo): DownloadInfo? {
            synchronized(LOCK) {
                val log = JLogUtils.getDefault()
                log.title("添加下载任务")

                val downloadInfoKeySet = mDownloadInfoMap.keys
                if (downloadInfo.id > 0) {
                    downloadInfoKeySet.forEach {
                        if (it.id == downloadInfo.id) {
                            log.content("线程池中已经存在有相同的任务：")

                            val thread = mDownloadInfoMap[it]

                            if (thread!!.isRunning) {
                                downloadInfo.log(log)
                                log.showError()
                                return@forEach
                            } else {
                                mDownloadInfoMap.remove(it)
                                log.content("移除对应的线程")
                                return@forEach
                            }
                        }
                    }

                    log.content("线程池中没有相同的任务：${downloadInfo.id}")
                } else {
                    log.content("新增任务")
                }

                //如果添加了错误任务的状态，则终止
                if (downloadInfo.isStatusContains(Status.ERROR)) {
                    log.content("错误状态").showError()
                    return null
                }

                //如果添加了完成状态的任务，则终止
                if (downloadInfo.status == Status.FINISH) {
                    log.content("成功状态").showError()
                    return null
                }

                if (downloadInfo.id <= 0) {
                    val save = downloadInfo.save()
//                    val save = DatabaseManager.db.downloadDao.save(downloadInfo)

                    log.content("插入数据库结果：$save")

                    if (!save) {
                        log.showError()
                        return null
                    }
//                    if (save == 0L) {
//                        log.showError()
//                        return null
//                    }

                    downloadInfo.log(log)
                }

                if (downloadInfo.isCurStatusContains(CurStatus.TIP)) {
                    downloadInfo.removeCurStatus(CurStatus.TIP)
                }

                if (downloadInfo.isStatusContains(Status.TIP)) {
                    downloadInfo.removeStatus(Status.TIP)
                    downloadInfo.update()
//                    DatabaseManager.getDownloadDao().update(downloadInfo)
                }

                val taskId = mRandom.nextInt(BOUND)
                downloadInfo.curState = CurStatus.WAITING

                mHandler.post {
                    val listener = downloadInfo.listener ?: return@post
                    listener.onWaiting()
                }

                val downloadThread = DownloadThread(taskId, downloadInfo)
                mDownloadInfoMap[downloadInfo] = downloadThread

                mThreadPool.submit(downloadThread)

                log.content("线程编号：$taskId 提交进线程池")
                downloadInfo.log(log)
                log.showInfo()

                return downloadInfo
            }
//            val downloadInfoFlow = flow<DownloadInfo?> {
//                emit(downloadInfo)
//            }

        }

        fun stopTask(downloadInfo: DownloadInfo) {
            synchronized(LOCK) {
                var removeModel: DownloadInfo? = null

                mDownloadInfoMap.keys.forEach {
                    if (it.id == downloadInfo.id) {
                        removeModel = it
                        return@forEach
                    }
                }

                if (removeModel == null) {
                    return
                }

                val downloadThread = mDownloadInfoMap.remove(removeModel!!)
                downloadThread?.stop()

                Log.e(TAG, "线程池中没有找到对应的线程 stopTask: $downloadInfo")
                //置为暂停 通知出去
                removeModel!!.curState = CurStatus.PAUSE
                val listener = removeModel!!.listener
                listener?.onPause()
            }
        }

        fun removeThread(downloadInfo: DownloadInfo) {
            synchronized(LOCK) {
                val thread = mDownloadInfoMap.remove(downloadInfo)
                if (thread != null) {
                    return
                }

                var removeModel: DownloadInfo? = null
                mDownloadInfoMap.keys.forEach {
                    if (it.id == downloadInfo.id) {
                        removeModel = it
                        return@forEach
                    }
                }

                if (removeModel == null) {
                    return
                }

                mDownloadInfoMap.remove(removeModel!!)
            }
        }

        fun delete(downloadInfo: DownloadInfo) = synchronized(LOCK) {
            //停止下载
            stopTask(downloadInfo)

            //删除临时文件
            if (!downloadInfo.tmpFileName.isNullOrEmpty()) {
                val realFile = DownloadFileUtils.getFile(downloadInfo.tmpFileName!!)
                if (realFile!!.exists()) {
                    realFile.delete()
                }
            }

            //删除数据库
            downloadInfo.delete()
//            DatabaseManager.getDownloadDao().delete(downloadInfo)
        }

    }
}