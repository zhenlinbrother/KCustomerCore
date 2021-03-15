package com.linc.download.strategy

import android.os.Handler
import android.os.Looper
import com.abc.lib_log.JLogUtils
import com.linc.download.model.CurStatus
import com.linc.download.model.DownloadInfo
import com.linc.download.model.Status
import com.linc.download.utils.CloseUtils
import com.linc.download.utils.CloseUtils.close
import okhttp3.Call
import okhttp3.Response

/**
 * <传输策略 基类> <功能详细描述>
 *
 * @author linc
 * @version 2020/10/28
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
abstract class BaseStrategy(
    val mDownloadInfo: DownloadInfo,
    val mStrategyName: String,
    val mLog: JLogUtils
) : IStrategy{

    private val mHandler = Handler(Looper.getMainLooper())
    protected var mIsRunning: Boolean = true
    protected var mResponse: Response? = null
    protected var mCall: Call? = null

    /**
     * 设置错误状态
     * @param errorMsg String?
     */
    protected fun setErrorStatus(errorMsg: String?) {
        //如果是被暂停的，则不记录错误信息
        if (!mIsRunning) {
            mDownloadInfo.curState = CurStatus.PAUSE
            return
        }

        mDownloadInfo.errorMsg = errorMsg
        mDownloadInfo.curState = CurStatus.ERROR
        mDownloadInfo.status = Status.ERROR
        mDownloadInfo.update()

        CloseUtils.close(mResponse)
    }

    /**
     * 保存异常信息
     * @param tipMsg String?
     */
    protected fun setTipStatus(tipMsg: String?) {
        //如果是被暂停的，则不记录异常信息
        if (!mIsRunning) {
            mDownloadInfo.curState = CurStatus.PAUSE
            return
        }

        mDownloadInfo.tip = tipMsg
        mDownloadInfo.addCurStatus(CurStatus.TIP)
        mDownloadInfo.addStatus(Status.TIP)
        mDownloadInfo.update()

        CloseUtils.close(mResponse)
    }

    override fun stop() {
        mIsRunning = false
        mCall?.cancel()
        CloseUtils.close(mResponse)
    }
}

