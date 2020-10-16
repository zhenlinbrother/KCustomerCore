package com.linc.video.helper

import android.content.Context
import android.view.View
import com.linc.video.view.CoverVideoPlayerView
import com.shuyu.gsyvideoplayer.listener.GSYMediaPlayerListener

/**
 * author       : linc
 * time         : 2020/10/15
 * desc         : 播放帮助类
 * version      : 1.0.0
 */
object VideoPlayerHelper {

    private var mVideoView: CoverVideoPlayerView? = null
    private var mMediaPlayerListener: GSYMediaPlayerListener? = null

    
    fun optionPlayer(gsyVideoPlayer: CoverVideoPlayerView,
                     context: Context?,
                     url: String?,
                     cache: Boolean,
                     title: String?){

        //增加title
        gsyVideoPlayer.titleTextView.visibility = View.GONE
        //增加返回键
        gsyVideoPlayer.backButton.visibility = View.VISIBLE
        //设置全屏按键功能
        gsyVideoPlayer.fullscreenButton.setOnClickListener {
            gsyVideoPlayer.startWindowFullscreen(context, false, statusBar = false)
        }
        //是否根据视频尺寸，自动选择竖屏全屏
        gsyVideoPlayer.isAutoFullWithSize = true
        //音频焦点冲突时是否释放
        gsyVideoPlayer.isReleaseWhenLossAudio = true
        //全屏动画
        gsyVideoPlayer.isShowFullAnimation = false
        //小屏时不触碰滑动
        gsyVideoPlayer.setIsTouchWiget(false)
        url?.let { gsyVideoPlayer.setVideoUrl(it) }
        gsyVideoPlayer.setVideoCache(cache)
        title?.let { gsyVideoPlayer.setVideoTitle(it) }
    }

    fun release(){
        mMediaPlayerListener?.onAutoCompletion()
        mVideoView = null
        mMediaPlayerListener = null
    }
}
