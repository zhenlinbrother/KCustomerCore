package com.lit.kcustomercore.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.ImageView
import com.lit.kcustomercore.R
import com.lit.kcustomercore.extension.gone
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer
import com.shuyu.gsyvideoplayer.video.base.GSYVideoView
/**
 * author       : linc
 * time         : 2020/10/10
 * desc         : 视频详情页对应的视频播放器
 * version      : 1.0.0
 */
class NewDetailVideoPlayer : StandardGSYVideoPlayer {

    /**
     *  是否第一次加载视频。用于隐藏进度条、播放按钮等UI。播放完成后，重新加载视频，会重置为true。
     */
    private var initFirstLoad = true

    constructor(context: Context) : super(context)

    constructor(context: Context, fullFlag: Boolean?) : super(context, fullFlag)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    override fun getLayoutId() = R.layout.layout_new_detail_video_player

    override fun updateStartImage() {
        if (mStartButton is ImageView) {
            val imageView = mStartButton as ImageView
            when (mCurrentState) {
                GSYVideoView.CURRENT_STATE_PLAYING -> {
                    imageView.setImageResource(R.drawable.ic_pause_white_24dp)
                    imageView.setBackgroundResource(R.drawable.sel_pause_white_bg)
                }
                GSYVideoView.CURRENT_STATE_ERROR -> {
                    imageView.setImageResource(R.drawable.ic_play_white_24dp)
                    imageView.setBackgroundResource(R.drawable.sel_play_white_bg)
                }
                GSYVideoView.CURRENT_STATE_AUTO_COMPLETE -> {
                    imageView.setImageResource(R.drawable.ic_refresh_white_24dp)
                    imageView.setBackgroundResource(0)
                }
                else -> {
                    imageView.setImageResource(R.drawable.ic_play_white_24dp)
                    imageView.setBackgroundResource(R.drawable.sel_play_white_bg)
                }
            }

        } else {
            super.updateStartImage()
        }
    }

    //正常
    override fun changeUiToNormal() {
        super.changeUiToNormal()
        Log.d(javaClass.simpleName, "changeUiToNormal")
        initFirstLoad = true
    }

    //准备中
    override fun changeUiToPreparingShow() {
        super.changeUiToPreparingShow()
        Log.d(javaClass.simpleName, "changeUiToPreparingShow")
        mBottomContainer.gone()
        mStartButton.gone()
    }

    //播放中
    override fun changeUiToPlayingShow() {
        super.changeUiToPlayingShow()
        Log.d(javaClass.simpleName, "changeUiToPlayingShow")
        if (initFirstLoad) {
            mBottomContainer.gone()
            mStartButton.gone()
        }
        initFirstLoad = false
    }

    //开始缓冲
    override fun changeUiToPlayingBufferingShow() {
        super.changeUiToPlayingBufferingShow()
        Log.d(javaClass.simpleName, "changeUiToPlayingBufferingShow")
    }

    //暂停
    override fun changeUiToPauseShow() {
        super.changeUiToPauseShow()
        Log.d(javaClass.simpleName, "changeUiToPauseShow")
    }

    //自动播放结束
    override fun changeUiToCompleteShow() {
        super.changeUiToCompleteShow()
        Log.d(javaClass.simpleName, "changeUiToCompleteShow")
        mBottomContainer.gone()
    }

    //错误状态
    override fun changeUiToError() {
        super.changeUiToError()
        Log.d(javaClass.simpleName, "changeUiToError")
    }

    fun getBottomContainer() = mBottomContainer
}