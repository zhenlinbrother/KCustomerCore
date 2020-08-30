//package com.lit.krecyclerview.config
//
//import android.content.Context
//import android.util.AttributeSet
//import android.view.View
//import androidx.annotation.Nullable
//import com.lit.krecyclerview.loadView.base.IBaseWrapperView
//import com.lit.krecyclerview.utils.LogUtils
//
//abstract class IBaseRefreshLoadView @JvmOverloads constructor(
//    context: Context?,
//    @Nullable attrs: AttributeSet? = null,
//    defStyleAttr: Int = 0
//) :
//    IBaseWrapperView(context, attrs, defStyleAttr) {
//    private val mMoveInfo: MoveInfo
//    private var mOnRefreshListener: JRefreshAndLoadMoreAdapter.OnRefreshListener? = null
//    protected fun wrapper(
//        context: Context?,
//        view: View?
//    ) {
//        addView(view)
//        measure(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
//        this.mHeight = getMeasuredHeight()
//        setVisibleHeight(0)
//    }
//
//    var onRefreshListener: JRefreshAndLoadMoreAdapter.OnRefreshListener?
//        get() = mOnRefreshListener
//        set(listener) {
//            mOnRefreshListener = listener
//        }
//
//    /**
//     * 释放动作，会进入两种状态：1、等待刷新；2、正在刷新；
//     *
//     * @param visibleHeight 可见高度
//     * @return 是否正在刷新
//     */
//    fun releaseAction(visibleHeight: Int): Boolean {
//        // 是否正在刷新
//        var isOnRefresh = false
//        // 可见高度
////        int height = visibleHeight;
//        // 此次释放后，需要进入的目标高度
//        var destHeight = 0
//
//        // 当前已经正在刷新，则让视图回到加载视图的高度（正在刷新，不用返回true，否则会再触发）
//        if (this.mCurState === STATE_EXECUTING) {
//            destHeight = this.mHeight
//        }
//
//        // 如果释放的时候，大于刷新视图的高度值且未进入刷新状态，则需要进入刷新状态
//        if (visibleHeight > this.mHeight && this.mCurState < STATE_EXECUTING) {
//            setState(STATE_EXECUTING)
//            destHeight = this.mHeight
//            isOnRefresh = true
//        }
//        smoothScrollTo(destHeight)
//        return isOnRefresh
//    }
//
//    /**
//     * @param delta 垂直增量
//     */
//    fun onMove(visibleHeight: Int, delta: Int) {
//        LogUtils.i(
//            TAG, "[visibleHeight: " + visibleHeight + "; " +
//                    "delta: " + delta + "]"
//        )
//
//        //需要符合：1、可见高度大于0，即用户已有向下拉动；2、拉动距离要大于0
//        if (visibleHeight > 0 || delta > 0) {
//            setVisibleHeight(visibleHeight + delta)
//
//            //当前状态为1、下拉刷新；2、释放刷新
//            if (mCurState <= STATE_RELEASE_TO_ACTION) {
//
//                //小于loadView高度
//                if (visibleHeight <= mHeight) {
//                    setState(STATE_PULL_TO_ACTION)
//                } else {
//                    setState(STATE_RELEASE_TO_ACTION)
//                }
//            }
//            val height: Int
//            if (visibleHeight >= mHeight) {
//                height = mHeight
//            } else {
//                height = visibleHeight
//            }
//            mMoveInfo.setViewHeight(mHeight)
//            mMoveInfo.setDragHeight(visibleHeight)
//            mMoveInfo.setPercent(height * 100 / mHeight)
//            onMoving(mMoveInfo)
//        }
//    }
//
//    fun refreshComplete() {
//        setState(STATE_DONE)
//        reset(0)
//    }
//
//    protected fun onOther(state: Int) {
//        //目前空实现，需扩展，可子类进行重写
//    }
//
//    /**
//     * 下拉过程中的回调，可以更加细微的处理动画
//     *
//     * @param moveInfo
//     */
//    protected abstract fun onMoving(moveInfo: MoveInfo?)
//
//    init {
//        mMoveInfo = MoveInfo()
//    }
//}
