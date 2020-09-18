package com.lit.krecyclerview.loadView.base

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.lit.krecyclerview.adapter.KRefreshAndLoadMoreAdapter
import com.lit.krecyclerview.utils.LogUtils

/**
 * author       : liaozhenlin
 * time         : 2020/8/30 0030 21:05
 * desc         : 加载更多抽象类
 * version      : 1.1.0
 */
abstract class IBaseLoadMoreView : IBaseWrapperView{

    var mOnLoadMoreListener : KRefreshAndLoadMoreAdapter.OnLoadMoreListener? = null

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun wrapper(context: Context?, view: View) {
        addView(view)
        measure(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        this.mHeight = measuredHeight
    }

    /**
     * 释放动作，会进入两种状态：1、等待刷新； 2、正在刷新；
     * @param visible Int 可见高度
     * @return Boolean 返回是否正在刷新
     */
    fun releaseAction(visible : Int): Boolean{
        //是否正在刷新
        var isOnRefresh = false
        LogUtils.i(TAG, "visible: $visible; height: $mHeight")

        //如果释放的时候，大于刷新视图的高度值并且未进入刷新状态，则需要进入刷新状态
        if (visible > this.mHeight && this.mCurState < 8){
            setState(8)
            isOnRefresh = true
        }

        smoothScrollTo(this.mHeight)
        return isOnRefresh
    }

    /**
     * 加载完毕
     */
    fun loadComplete(){
        setState(16)
        reset(mHeight)
    }

    /**
     * 重置
     */
    fun reset(){
        setState(2)
        reset(mHeight)
    }

    /**
     * 加载失误
     */
    fun loadError(){
        setState(1)
    }

    /**
     * 没有更多
     */
    fun noMore(){
        setState(32)
        smoothScrollTo(mHeight)
    }

    override fun onOther(state: Int) {
        when(state){
            1 -> onNoMore()
            32 -> onError()
        }
    }

    /**
     *
     * @param visibleHeight Int 可视高度
     * @param delta Float 垂直增量
     */
    fun onMove(visibleHeight : Int, delta : Float){
        //需要符合：1、可见高度大于控件高度； 2、拉动距离要大于0
        var viewHeight = (visibleHeight + delta).toFloat()
        if (viewHeight < mHeight.toFloat()){
            viewHeight = mHeight.toFloat()
        }

        setVisibleHeight(viewHeight.toInt())
        LogUtils.i(TAG, "visibleHeight: $visibleHeight; height: $mHeight; viewHeight: $viewHeight")

        //当前状态为：1、上拉刷新 2、释放刷新
        Log.d(TAG, "onMove: mCurState= ${this.mCurState}")
        if (this.mCurState <= 4){
            //小于loadView高度
            if (visibleHeight <= mHeight){

                if (this.mCurState == 1){
                    setState(1)
                } else {
                    setState(2)
                }

            } else {
                setState(4)
            }
        }
    }

    /**
     * 没有更多状态
     */
    abstract fun onNoMore()

    /**
     * 加载出错
     */
    abstract fun onError()
}