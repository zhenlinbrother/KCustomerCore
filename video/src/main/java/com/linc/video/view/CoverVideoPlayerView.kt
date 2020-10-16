package com.linc.video.view

import android.content.Context
import android.graphics.Point
import android.util.AttributeSet
import android.view.Surface
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.SeekBar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.linc.video.R
import com.shuyu.gsyvideoplayer.utils.CommonUtil
import com.shuyu.gsyvideoplayer.utils.Debuger
import com.shuyu.gsyvideoplayer.utils.GSYVideoType
import com.shuyu.gsyvideoplayer.utils.NetInfoModule
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer
import com.shuyu.gsyvideoplayer.video.base.GSYBaseVideoPlayer
import com.shuyu.gsyvideoplayer.video.base.GSYVideoView
import kotlinx.android.synthetic.main.cover_video_layout.view.*

/**
 * author       : linc
 * time         : 2020/10/14
 * desc         : 自定义带封面的 video
 * version      : 1.0.0
 */
class CoverVideoPlayerView : StandardGSYVideoPlayer{

    constructor(context: Context?, fullFlag: Boolean?) : super(context, fullFlag)
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    override fun getLayoutId(): Int = R.layout.cover_video_layout

    override fun init(context: Context) {
        super.init(context)
        if (mThumbImageViewLayout != null
            && (mCurrentState == -1
                    || mCurrentState == GSYVideoView.CURRENT_STATE_NORMAL
                    || mCurrentState == GSYVideoView.CURRENT_STATE_ERROR)){
            mThumbImageViewLayout.visibility = View.VISIBLE
        }
    }

    /**
     * 设置URL
     * @param url String
     */
    fun setVideoUrl(url: String){
        mUrl = url
        mOriginUrl = url
    }

    /**
     * 是否缓存
     * @param cache Boolean
     */
    fun setVideoCache(cache: Boolean) {
        mCache = cache
    }

    /**
     * 设置title
     * @param title String
     */
    fun setVideoTitle(title: String){
        mTitle = title
    }

    /**
     * 保存播放状态
     * @return CoverVideoPlayerView
     */
    fun saveState(): CoverVideoPlayerView {
        val switchVideo = CoverVideoPlayerView(context)
        cloneParams(this, switchVideo)
        return switchVideo
    }

    fun cloneState(coverVideoPlayerView: CoverVideoPlayerView){
        cloneParams(coverVideoPlayerView, this)
    }

    /**
     * 加载图片
     * @param url String
     * @param res Int
     */
    fun loadCoverImage(url: String, res: Int){
        Glide.with(context.applicationContext)
            .load(url)
            .apply(RequestOptions.bitmapTransform(RoundedCorners(40)))
            .into(thumbImage)
    }

    override fun startWindowFullscreen(
        context: Context?,
        actionBar: Boolean,
        statusBar: Boolean
    ): GSYBaseVideoPlayer {
        val gsyBaseVideoPlayer: GSYBaseVideoPlayer =
            super.startWindowFullscreen(context, actionBar, statusBar)
        return gsyBaseVideoPlayer
    }

    override fun showSmallVideo(
        size: Point?,
        actionBar: Boolean,
        statusBar: Boolean
    ): GSYBaseVideoPlayer {
        val sampleCoverVideo = super.showSmallVideo(size, actionBar, statusBar) as CoverVideoPlayerView
        sampleCoverVideo.mStartButton.visibility = View.GONE
        sampleCoverVideo.mStartButton = null
        return sampleCoverVideo
    }

    override fun cloneParams(from: GSYBaseVideoPlayer?, to: GSYBaseVideoPlayer?) {
        super.cloneParams(from, to)
        val sf: CoverVideoPlayerView = from as CoverVideoPlayerView
        val st: CoverVideoPlayerView = to as CoverVideoPlayerView
        st.mShowFullAnimation = sf.mShowFullAnimation
    }

    /**
     * 退出window层播放全屏效果
     */
    override fun clearFullscreenLayout() {
        if (!mFullAnimEnd){
            return
        }
        mIfCurrentIsFullscreen = false
        var delay = 0
        if (mOrientationUtils != null){
            delay = mOrientationUtils.backToProtVideo()
            mOrientationUtils.isEnable = false
            if (mOrientationUtils != null){
                mOrientationUtils.releaseListener()
                mOrientationUtils = null
            }
        }

        if (!mShowFullAnimation){
            delay = 0
        }

        val viewGroup: ViewGroup = (CommonUtil.scanForActivity(context))
            .findViewById(Window.ID_ANDROID_CONTENT)
        val oldF: View = viewGroup.findViewById(fullId)
        val gsyVideoPlayer = oldF as CoverVideoPlayerView
        gsyVideoPlayer.mIfCurrentIsFullscreen = false

        if (delay == 0)
            backToNormal()
        else
            postDelayed(Runnable { backToNormal()}, delay.toLong())
    }

    override fun onSurfaceUpdated(surface: Surface?) {
        super.onSurfaceUpdated(surface)
        if (mThumbImageViewLayout != null && mThumbImageViewLayout.visibility == View.VISIBLE)
            mThumbImageViewLayout.visibility = View.INVISIBLE
    }

    override fun setViewShowState(view: View?, visibility: Int) {
        if (view == mThumbImageViewLayout && visibility != View.VISIBLE)
            return
        super.setViewShowState(view, visibility)
    }

    override fun onSurfaceAvailable(surface: Surface?) {
        super.onSurfaceAvailable(surface)
        if (GSYVideoType.getRenderType() != GSYVideoType.TEXTURE){
            if (mThumbImageViewLayout != null && mThumbImageViewLayout.visibility == View.VISIBLE){
                mThumbImageViewLayout.visibility = View.INVISIBLE
            }
        }
    }

    /******************* 下方重载方法，在播放开始不显示底部进度和按键，不需要可屏蔽 ********************/

    protected var byStartedClick: Boolean = false

    override fun onClickUiToggle() {
        if (mIfCurrentIsFullscreen && mLockCurScreen && mNeedLockFull){
            setViewShowState(mLockScreen, View.VISIBLE)
            return
        }
        byStartedClick = true
        super.onClickUiToggle()
    }

    override fun changeUiToNormal() {
        super.changeUiToNormal()
        byStartedClick = false
    }

    override fun changeUiToPreparingShow() {
        super.changeUiToPreparingShow()
        Debuger.printfLog("Sample changeUiToPreparingShow")
        setViewShowState(mBottomContainer, View.INVISIBLE)
        setViewShowState(mStartButton, View.INVISIBLE)
    }

    override fun changeUiToPlayingBufferingShow() {
        super.changeUiToPlayingBufferingShow()
        Debuger.printfLog("Sample changeUiToPlayingBufferingShow")
        if (!byStartedClick) {
            setViewShowState(mBottomContainer, View.INVISIBLE)
            setViewShowState(mStartButton, View.INVISIBLE)
        }
    }

    override fun changeUiToPlayingShow() {
        super.changeUiToPlayingShow()
        Debuger.printfLog("Sample changeUiToPlayingShow")
        if (!byStartedClick) {
            setViewShowState(mBottomContainer, View.INVISIBLE)
            setViewShowState(mStartButton, View.INVISIBLE)
        }
    }

    override fun startAfterPrepared() {
        super.startAfterPrepared()
        Debuger.printfLog("Sample startAfterPrepared")
        setViewShowState(mBottomContainer, View.INVISIBLE)
        setViewShowState(mStartButton, View.INVISIBLE)
        setViewShowState(mBottomProgressBar, View.VISIBLE)
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
        byStartedClick = true
        super.onStartTrackingTouch(seekBar)
    }

    /**
     * 利用反射 解决gsy库中导致的内存泄漏
     */
    fun cancel() {
        mAudioManager.abandonAudioFocus(onAudioFocusChangeListener)
        try {
            if (mNetInfoModule != null){
                //拿到NetInfoModule对象中 mConnectivityBroadcastReceiver字段
                val mConnectivityBroadcastReceiver = NetInfoModule::class.java
                    .getDeclaredField("mConnectivityBroadcastReceiver")
                //由于是私有字段，所以需要调用setAccessible(true)，否则会报错
                mConnectivityBroadcastReceiver.isAccessible = true
                //根据当前mNetInfoModule对象的 mConnectivityBroadcastReceiver字段值为null
                mConnectivityBroadcastReceiver.set(mNetInfoModule, null)
                val mNetChangeListener = NetInfoModule::class.java.getDeclaredField("mNetChangeListener")
                mNetChangeListener.isAccessible = true
                mNetChangeListener.set(mNetInfoModule, null)
            }
        } catch (e: IllegalAccessException){
            e.printStackTrace()
        } catch (e: NoSuchFieldException){
            e.printStackTrace()
        }

        mAudioManager = null
        mContext = null
    }
}
