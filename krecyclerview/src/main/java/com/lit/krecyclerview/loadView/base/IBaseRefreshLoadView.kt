package com.lit.krecyclerview.loadView.base

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import com.lit.krecyclerview.adapter.KRefreshAndLoadMoreAdapter
import com.lit.krecyclerview.utils.LogUtils
import java.util.jar.Attributes

/**
 * author       : liaozhenlin
 * time         : 2020/8/30 0030 13:04
 * desc         : 下拉刷新
 * version      : 1.1.0
 */
abstract class IBaseRefreshLoadView constructor(
    context: Context?,
    @Nullable attributes: AttributeSet? = null,
    defStyleAttr : Int = 0)
    : IBaseWrapperView(context, attributes, defStyleAttr){

    /**
     * 下拉刷新监听
     */
    var mOnRefreshListener : KRefreshAndLoadMoreAdapter.OnRefreshListener ?= null

    override fun wrapper(context: Context?, view: View) {
        addView(view)
        measure(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        this.mHeight = measuredHeight
        setVisibleHeight(0)
    }

    /**
     * 释放动作，会进入两种状态：1、等待刷新 2、正在刷新
     * @param visibleHeight Int
     * @return Boolean
     */
    fun releaseAction(visibleHeight : Int) : Boolean {
        //是否正在刷新
        var isOnRefresh = false
        //此次释放后，需要进入的目标高度
        var destHeight = 0

        //当前已经正在刷新，则让视图回到加载视图的高度（正在刷新，不用返回true，否则会再触发）
        if (mCurState == STATE_EXECUTING){
            destHeight = mHeight
        }

        // 如果释放的时候，大于刷新视图的高度值且未进入刷新状态，则需要进入刷新状态
        if (visibleHeight > this.mHeight && this.mCurState < STATE_EXECUTING){
            setState(STATE_EXECUTING)
            destHeight = this.mHeight
            isOnRefresh = true
        }

        smoothScrollTo(destHeight)
        return isOnRefresh
    }

    fun onMove(visibleHeight: Int, delta : Int){
        LogUtils.i(TAG, "[visibleHeight: $visibleHeight; delta: $delta")

        if (visibleHeight > 0 || delta > 0){
            this.setVisibleHeight(visibleHeight + delta)
        }

        //需要符合：1、可见高度大于0，即用户已有向下拉动；2、拉动距离要大于0
        if (mCurState <= STATE_RELEASE_TO_ACTION){

            //小于loadView高度
            if (visibleHeight <= mHeight){
                setState(STATE_PULL_TO_ACTION)
            } else {
                setState(STATE_RELEASE_TO_ACTION)
            }

        }
    }

    fun refreshComplete(){
        setState(STATE_DONE)
        reset(0)
    }

    override fun onOther(state: Int) {

    }
}