package com.lit.krecyclerview.loadView.base

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.lit.krecyclerview.utils.LogUtils
import java.util.jar.Attributes
import kotlin.properties.Delegates

abstract class IBaseWrapperView : LinearLayout {

    companion object{
        const val TAG : String = "IBaseWrapperView.java"
        private const val INDEX : Int = 1
        const val SCROLL_DURATION : Int = 300
        //加载出错
        const val STATE_ERROR = INDEX
        //下拉刷新或上拉加载更多状态
        //1.还没操作
        //2.下拉高度未超过显示的高度
        const val STATE_PULL_TO_ACTION = INDEX.shl(1)
        //释放执行
        const val STATE_RELEASE_TO_ACTION = INDEX.shl(2)
        //执行中
        const val STATE_EXECUTING = INDEX.shl(3)
        //执行完毕
        const val STATE_DONE = INDEX.shl(4)
        //没有更多
        const val STATE_NO_MORE = INDEX.shl(5)
    }

    //当前状态
    var mCurState by Delegates.notNull<Int>()
    //本视图高度
    var mHeight by Delegates.notNull<Int>()

    constructor(context: Context?) : this(context, null, 0)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ){
        if (context != null) {
            init(context)
        }
    }

    private fun init(context: Context){
        mCurState = STATE_PULL_TO_ACTION

        var params : LinearLayout.LayoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        params.setMargins(0, 0, 0,0)
        layoutParams = params
        setPadding(0, 0, 0,0)

        var view = initView(context)
        wrapper(context, view)
    }

    fun getViewHeight() = this.mHeight

    fun setHeight(height : Int){
        this.mHeight = height
    }

    /**
     * 获取当前状态
     */
    fun getCurState() = mCurState

    /**
     * 获取刷新的根视图
     */
    abstract fun getLoadView() : View

    /**
     * 获取加载视图的高度
     */
    fun getVisibleHeight() : Int {
        var layoutParams = this.getLoadView().layoutParams as LinearLayout.LayoutParams
        return layoutParams.height
    }

    /**
     * 设置加载view的高度
     * @param height 设置可见高度
     */
    fun setVisibleHeight(height: Int) {
        var sHeight : Int = height
        if (height < 0){
            sHeight = 0
        }
        var layoutParams = this.getLoadView().layoutParams as LinearLayout.LayoutParams
        layoutParams.height = sHeight
        getLoadView().layoutParams = layoutParams
    }

    fun setState(state : Int){
        //如果与当前状态相同，不做任何处理
        if (state == this.mCurState){
            return
        }

        LogUtils.i(TAG, "state + $state")

        when(state){
            //下拉执行
            STATE_PULL_TO_ACTION -> onPullToAction()
            //释放执行
            STATE_RELEASE_TO_ACTION -> onReleaseAction()
            //执行中
            STATE_EXECUTING -> onExecuting()
            //执行完毕
            STATE_DONE -> onDone()

            else -> onOther(state)
        }
        //保存当前状态
        mCurState = state
    }


    protected fun reset(destHeight: Int){
        smoothScrollTo(destHeight)
        postDelayed(Runnable { setState(2) }, 200)
    }

    /**
     * 平滑滚动至某个高度
     */
    open fun smoothScrollTo(destHeight : Int){
        smoothScrollTo(destHeight, SCROLL_DURATION)
    }

    /**
     * 平滑滚动至某个高度
     * @param destHeight 目标高度
     * @param durTime 时长
     */
    open fun smoothScrollTo(destHeight: Int, durTime : Int){
        //设置从可见高度 -> 目标高度
        var valueAnimator : ValueAnimator = ValueAnimator.ofInt(getVisibleHeight(), destHeight)
        valueAnimator.duration = durTime.toLong()
        valueAnimator.addUpdateListener { animation ->
            setVisibleHeight(animation.animatedValue as Int) }
        valueAnimator.start()
    }

    abstract fun wrapper(context: Context?, view: View)

    /**
     * 初始化视图，用于加载自己的视图
     */
    abstract fun initView(context : Context) : View

    /**
     * 等待上拉 或 等待下拉的状态 视图表现
     */
    abstract fun onPullToAction()

    /**
     * 释放执行（释放刷新 或 释放加载更多）视图表现
     */
    abstract fun onReleaseAction()

    /**
     * 执行中 视图表现
     */
    abstract fun onExecuting()

    /**
     * 执行完视图表现
     */
    abstract fun onDone()

    /**
     * 拓展方法，主要用于后面拓展一些细节的状态
     */
    abstract fun onOther(state: Int)
}