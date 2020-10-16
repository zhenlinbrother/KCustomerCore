package com.linc.video.helper

import android.content.Context
import android.graphics.Rect
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.linc.video.R
import com.shuyu.gsyvideoplayer.utils.CommonUtil
import com.shuyu.gsyvideoplayer.utils.NetworkUtils
import com.shuyu.gsyvideoplayer.video.base.GSYBaseVideoPlayer

/**
 * author       : linc
 * time         : 2020/10/14
 * desc         : 计算列表滑动、自动播放的帮助类
 * version      : 1.0.0
 */
class ScrollCalculatorHelper {

    private var firstVisible = 0

    private var lastVisible = 0

    private var visibleCount = 0

    private var playId: Int = 0

    private var rangeTop: Int = 0

    private var rangeBottom: Int = 0

    private var runnalbe: PlayRunnable? = null

    private val playHandler = Handler()

    constructor(playId: Int, rangeTop: Int, rangeBottom: Int) {
        this.playId = playId
        this.rangeTop = rangeTop
        this.rangeBottom = rangeBottom
    }

    fun onScrollStateChanged(
        view: RecyclerView,
        scrollState: Int
    ) {
        when (scrollState) {
            RecyclerView.SCROLL_STATE_IDLE -> playVideo(view)
        }
    }

    fun onScroll(view: RecyclerView, firstVisibleItem: Int, lastVisibleItem: Int, visibleItemCount: Int){
        if (firstVisible == firstVisibleItem) return
        firstVisible = firstVisibleItem
        lastVisible = lastVisibleItem
        visibleCount = visibleItemCount
    }

    /**
     * 播放
     * @param view RecyclerView
     */
    private fun playVideo(view: RecyclerView){

        val layoutManager = view.layoutManager

        var gsyBaseVideoPlayer: GSYBaseVideoPlayer? = null

        var needPlay = false

        for (i in 0..visibleCount){
            if (layoutManager?.getChildAt(i) != null
                && layoutManager.getChildAt(i)!!.findViewById<View>(playId) != null){

                val player = layoutManager.getChildAt(i)!!.findViewById<GSYBaseVideoPlayer>(playId)
                var rect = Rect()
                player.getLocalVisibleRect(rect)
                val height = player.height
                //说明第一个完全可视
                if (rect.top == 0 && rect.bottom == height){
                    gsyBaseVideoPlayer = player
                    if (player.currentPlayer.currentState == GSYBaseVideoPlayer.CURRENT_STATE_NORMAL
                        || player.currentPlayer.currentState == GSYBaseVideoPlayer.CURRENT_STATE_ERROR){
                        needPlay = true
                    }
                    break
                }
            }
        }

        if (gsyBaseVideoPlayer != null && needPlay){
            if (runnalbe != null){
                val tmpPlayer = runnalbe!!.gsyBaseVideoPlayer
                playHandler.removeCallbacks(runnalbe)
                runnalbe = null
                if (tmpPlayer == gsyBaseVideoPlayer)
                    return
            }
            runnalbe = PlayRunnable(gsyBaseVideoPlayer)
            //降低频率
            playHandler.postDelayed(runnalbe, 400)
        }

    }

    inner class PlayRunnable : Runnable{

        var gsyBaseVideoPlayer: GSYBaseVideoPlayer? = null

        constructor(gsyBaseVideoPlayer: GSYBaseVideoPlayer?) {
            this.gsyBaseVideoPlayer = gsyBaseVideoPlayer
        }

        override fun run() {
            var inPosition = false
            //如果未播放，需要播放
            if (gsyBaseVideoPlayer != null){
                val screenPosition = IntArray(2)
                gsyBaseVideoPlayer!!.getLocationOnScreen(screenPosition)
                val halfHeight = gsyBaseVideoPlayer!!.height / 2
                val rangePosition = screenPosition[1] + halfHeight
                //中心点在播放区域
                if (rangePosition in rangeTop..rangeBottom){
                    inPosition = true
                }

                if (inPosition){
                    startPlayLogic(gsyBaseVideoPlayer!!, gsyBaseVideoPlayer!!.context)
                }
            }
        }
    }

    /********************自动播放的点击播放确认*******************************/
    fun startPlayLogic(gsyBaseVideoPlayer: GSYBaseVideoPlayer, context: Context){
        if (!CommonUtil.isWifiConnected(context)){
            //判断是否有WiFi
            showWifiDialog(gsyBaseVideoPlayer, context)
            return
        }

        gsyBaseVideoPlayer.startPlayLogic()
    }

    private fun showWifiDialog(gsyBaseVideoPlayer: GSYBaseVideoPlayer, context: Context){
        if (!NetworkUtils.isAvailable(context)){
            Toast.makeText(context, "当前找不到网络", Toast.LENGTH_SHORT).show()
            return
        }

        val builder = AlertDialog.Builder(context)
        builder.setMessage(context.resources.getString(R.string.tips_not_wifi))
        builder.setPositiveButton(context.resources.getString(R.string.tips_not_wifi_confirm)
        ) { dialog, which ->
            dialog.dismiss()
            gsyBaseVideoPlayer.startPlayLogic()
        }

        builder.setNegativeButton(context.resources.getString(R.string.tips_not_wifi_cancel)){ dialog, which ->
            dialog.dismiss()
        }
        builder.create().show()
    }


}

