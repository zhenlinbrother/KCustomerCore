package com.linc.download.thread

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.abc.lib_log.JLogUtils
import com.linc.download.constant.ReqHead
import com.linc.download.jerry.JerryDownload
import com.linc.download.listener.DownloadListener
//import com.linc.download.manager.DatabaseManager
import com.linc.download.model.CurStatus
import com.linc.download.model.DownloadInfo
import com.linc.download.model.Status
import com.linc.download.strategy.ContinueStrategy
import com.linc.download.strategy.FirstStrategy
import com.linc.download.strategy.IStrategy
import com.linc.download.utils.CloseUtils
import com.linc.download.utils.CloseUtils.close
import com.linc.download.utils.CloseUtils.closeable
import com.linc.download.utils.DownloadFileUtils
import com.linc.download.utils.DownloadFileUtils.calculatePercent
import com.linc.download.utils.DownloadFileUtils.getFile
import com.linc.download.utils.DownloadFileUtils.getFolder
import com.linc.download.utils.DownloadFileUtils.isExist
import com.linc.download.utils.FileUtils
import com.linc.download.utils.FileUtils.renameFileAtTheSameFolder
import com.linc.download.utils.NetUtils
import com.linc.download.utils.NetUtils.request
import okhttp3.Call
import okhttp3.Response
import okio.IOException
import java.io.*
import java.net.UnknownHostException
import java.util.*
import kotlin.collections.HashMap
/**
 * author       : linc
 * time         : 2020/11/3
 * desc         : 下载线程
 * version      : 1.0.0
 */
class DownloadThread(
    private val mTaskId: Int,
    private val mDownloadInfo: DownloadInfo
) : IDownloadThread, Runnable{

//    private val mDownloadInfo: DownloadInfo = mDownloadInfo
    private val mLog: JLogUtils = JLogUtils.getDefault()
    private val mHandler = Handler(Looper.getMainLooper())
    private var mCall: Call? = null
    private var mResponse: Response? = null

    //是否运行
    @Volatile
    var isRunning = true
        private set

    //最后更新时间
    private var lastUpdateTime: Long = -1

    //策略
    private var mStrategy: IStrategy? = null

    @Synchronized
    override fun stop() {
        if (!isRunning) return
        isRunning = false
        mStrategy?.stop()
        mCall?.cancel()
        mResponse?.closeable()

    }

    override fun run() {
        if (!isRunning) {
            return
        }

        //设置为开始状态
        mDownloadInfo.curState = CurStatus.INIT
        onCallback()

        mLog.title("TASK NO: 【$mTaskId】 线程开启")
        mDownloadInfo.log(mLog)

        if (!isRunning) {
            stopSelf("线程停止【1】")
            return
        }

        // 存储文件夹没创建成功
        val createFile = DownloadFileUtils.getFolder()
        if (createFile == null) {
            tip("文件异常", "文件创建失败...")
            return
        }

        mLog.content("文件夹: ${createFile.absolutePath}" )

        mStrategy = if (mDownloadInfo.isStatusContains(Status.INIT)) {
            FirstStrategy(mDownloadInfo, mLog)
        } else {
            ContinueStrategy(mDownloadInfo, mLog)
        }

        mDownloadInfo.curState = CurStatus.DOWNLOADING
        onCallback()

        val isSuc = mStrategy?.run()!!
        mLog.content("策略运行结果: $isSuc")

        //是否被停止
        if (!isRunning) {
            stopSelf("线程停止【2】")
            return
        }

        //策略结果
        if (!isSuc) {
            onCallback()
            mResponse?.closeable()
            //移除
            JerryDownload.instance?.removeDownloadInfo(mDownloadInfo)
            mDownloadInfo.log(mLog)
            mLog.showError()
            return
        }

        //装载
        val sourceFile = getFile(mDownloadInfo.tmpFileName!!)

        //文件是否存在
        val fileExist = isExist(sourceFile!!)
        mLog.content("文件是否存在: $fileExist")

        if (!fileExist) {
            error("文件异常", "本地文件异常")
            return
        }

        val length = sourceFile.length()
        mLog.param("当前本地文件长度【已存储】: $length")
        mLog.param("range: $length")

        val headerMap = HashMap<String, String>()
        headerMap[ReqHead.RANGE] = "bytes = $length-"

        try {
            mCall = request(mDownloadInfo, headerMap)
            mResponse = mCall?.execute()
        } catch (e : UnknownHostException) {
            tip("网络异常", "EXCEPTION: ${e.message}")
            return
        } catch (e : IOException) {
            mLog.content("EXCEPTION: ${e.message}")
        }

        if (mResponse == null) {
            tip("服务器异常", "response 为空")
            return
        }

        if (mResponse!!.code == 416) {
          error("资源异常", "response 响应码为416")
          return
        }

        //500 - 599 服务器异常，可以下次继续
        if (mResponse!!.code in 500..599) {
            tip("服务器异常", "mResponse响应失败: ${mResponse!!.code}")
            return
        }

        if (!mResponse!!.isSuccessful) {
            tip("资源异常", "mResponse响应失败: ${mResponse!!.code}")
            return
        }

        val body = mResponse?.body
        if (body == null) {
            tip("服务器异常", "body 为空")
            return
        }

        val outputStream: OutputStream

        try {
            outputStream = BufferedOutputStream(FileOutputStream(sourceFile, true))
        } catch (e: FileNotFoundException) {
            tip("文件异常", "文件流打开失败 ${e.message}")
            return
        }

        onCallback()

        val isSucTransmit = transmitStream(body.byteStream(), outputStream, sourceFile)

        if (!isRunning) {
            stopSelf("线程停止【4】")
            return
        }

        if (!isSucTransmit) {
            tip("服务器异常", "isSuctransmit失败")
            return
        }

        val realFileName = mDownloadInfo.realFileName

        val same = renameFileAtTheSameFolder(sourceFile, realFileName)

        mLog.content("重命名结果: $same")

        if (same) {
            finish()
        } else {
            error("文件异常", "")
        }

        mLog.content("最终: ")
        mDownloadInfo.log(mLog)
        mLog.showInfo()
    }

    private fun stopSelf(log: String) {
        mDownloadInfo.curState = CurStatus.PAUSE
        JerryDownload.instance?.removeDownloadInfo(mDownloadInfo)
        onCallback()
        mLog.content(log).showWarn()
    }

    /**
     * 进行回调
     */
    private fun onCallback() {
        mHandler.post {
            val listener = mDownloadInfo.listener
            Log.i("downloadThread", "${mTaskId.toString()} " +
                    "onCallback: 【 curStatus: ${mDownloadInfo.curState}; " +
                    "listener: ${mDownloadInfo.listener} 】")

            if (mDownloadInfo.isCurStatusContains(CurStatus.WAITING)){
                Log.d("onCallback", "$mTaskId 异常! ! ! 含有WAITING")
            }

            if (mDownloadInfo.isCurStatusContains(CurStatus.ERROR)){
                listener?.onError()
                return@post
            }

            if (mDownloadInfo.isCurStatusContains(CurStatus.FINISH)) {
                listener?.onFinish()
                return@post
            }

            if (mDownloadInfo.isCurStatusContains(CurStatus.TIP)) {
                listener?.onTip()
                return@post
            }

            if (mDownloadInfo.isCurStatusContains(CurStatus.PAUSE)) {
                listener?.onPause()
                return@post
            }

            if (mDownloadInfo.isCurStatusContains(CurStatus.INIT)) {
                listener?.onInit()
                return@post
            }

            if (mDownloadInfo.isCurStatusContains(CurStatus.DOWNLOADING)) {
                listener?.onDownloading()
                return@post
            }
        }
    }

    private fun tip(msg: String, log: String) {
        mResponse?.closeable()

        JerryDownload.instance?.removeDownloadInfo(mDownloadInfo)

        //如果是被暂停的，则不记录错误信息
        if (!isRunning) {
            mDownloadInfo.curState = CurStatus.PAUSE
            onCallback()
            mLog.content("线程暂停【6】").showWarn()
            return
        }

        mLog.content(log).showWarn()

        mDownloadInfo.tip = msg
        mDownloadInfo.addCurStatus(CurStatus.TIP)
        mDownloadInfo.addStatus(Status.TIP)
        mDownloadInfo.update()
//        DatabaseManager.getDownloadDao().update(mDownloadInfo)
        onCallback()
    }

    private fun error(msg: String, log: String) {
        mResponse?.closeable()

        JerryDownload.instance?.removeDownloadInfo(mDownloadInfo)

        //如果被暂停的，则不记录错误信息
        if (!isRunning) {
            mDownloadInfo.curState = CurStatus.PAUSE
            onCallback()
            mLog.content("线程暂停【5】").showWarn()
            return
        }

        mLog.content(log).showError()

        mDownloadInfo.errorMsg = msg
        mDownloadInfo.curState = CurStatus.ERROR
        mDownloadInfo.status = CurStatus.ERROR
        mDownloadInfo.update()
//        DatabaseManager.getDownloadDao().update(mDownloadInfo)

        onCallback()
    }

    private fun finish() {
        mResponse?.closeable()

        JerryDownload.instance?.removeDownloadInfo(mDownloadInfo)

        mDownloadInfo.curState = CurStatus.FINISH
        mDownloadInfo.status = Status.FINISH
        mDownloadInfo.update()
//        DatabaseManager.getDownloadDao().update(mDownloadInfo)

        onCallback()
    }

    /**
     * 转接流，将 输入流-->转给-->输出流
     * @param inputStream 输入流
     * @param outputStream 输出流
     * @param sourceFile 文件
     * @return 转送成功则返回true，否则返回false
     */
    private fun transmitStream(inputStream: InputStream,
                               outputStream: OutputStream,
                               sourceFile: File?) : Boolean {
        mLog.content("开始流的逻辑")

        try {
            val tempByte = ByteArray(2048)

            while (true) {
                val len = inputStream.read(tempByte)
                if (len == -1) {
                    outputStream.flush()
                    break
                }

                outputStream.write(tempByte, 0, len)

                checkProgress(sourceFile, mDownloadInfo.totalSize)

                if (!isRunning) {
                    mLog.content("线程停止【3】")
                    updateProgress(sourceFile!!.length(), mDownloadInfo.totalSize)
                    break
                }
            }
        } catch (e : IOException) {
            e.printStackTrace()
            mLog.content(e.message)
            return false
        } finally {
            mLog.content("流的逻辑结束")
            inputStream.closeable()
            outputStream.closeable()
        }

        updateProgress(sourceFile!!.length(), mDownloadInfo.totalSize)
        return true
    }

    private fun checkProgress(sourceFile: File?, totalSize: Long) {
        if (lastUpdateTime == -1L) {
            lastUpdateTime = System.currentTimeMillis()
            updateProgress(sourceFile!!.length(), totalSize)
            return
        }

        val duration = System.currentTimeMillis() - lastUpdateTime

        if (duration > DURATION) {
            lastUpdateTime = System.currentTimeMillis()
            updateProgress(sourceFile!!.length(), totalSize)
        }
    }

    private fun updateProgress(length: Long, totalSize: Long) {
        val percent = DownloadFileUtils.calculatePercent(length, totalSize)

        JLogUtils
            .getDefault()
            .title("Task ID: $mTaskId process")
            .content("length: $length")
            .content("totalSize: $totalSize")
            .content("percent: $percent")
            .showInfo()

        mDownloadInfo.percent = percent

        mHandler.post {
            val listener = mDownloadInfo.listener
            listener?.onProgress()
        }
    }

    companion object {
        const val TAG = "DownloadThread.java"
        const val DURATION = 2000
    }
}