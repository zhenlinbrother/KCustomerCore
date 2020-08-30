//package com.lit.krecyclerview.config
//
//import android.animation.ValueAnimator
//import android.widget.LinearLayout
//
//class JRecyclerManager {
//    // 下拉刷新视图
//    private var mRefreshLoadView: IBaseRefreshLoadView? = null
//
//    // 上拉更多视图
//    private var mLoadMoreView: IBaseLoadMoreView? = null
//
//    // 动画
//    private var mItemAnimations: Array<IBaseAnimation>
//
//    // 是否处于 debug
//    var isDebug = false
//        private set
//
//    private fun JRecycleViewManager() {
//        mItemAnimations = arrayOf<IBaseAnimation>(SlideInBottomAnimation())
//        isDebug = false
//    }
//
//    val refreshLoadView: IBaseRefreshLoadView?
//        get() = mRefreshLoadView
//
//    fun setRefreshLoadView(refreshLoadView: IBaseRefreshLoadView?): JRecycleViewManager {
//        mRefreshLoadView = refreshLoadView
//        return this
//    }
//
//    val loadMoreView: IBaseLoadMoreView?
//        get() = mLoadMoreView
//
//    fun setLoadMoreView(loadMoreView: IBaseLoadMoreView?): JRecycleViewManager {
//        mLoadMoreView = loadMoreView
//        return this
//    }
//
//    val itemAnimations: Array<Any>
//        get() = mItemAnimations
//
//    fun setItemAnimations(itemAnimations: Array<IBaseAnimation>): JRecycleViewManager {
//        mItemAnimations = itemAnimations
//        return this
//    }
//
//    fun setIsDebug(isDebug: Boolean): JRecycleViewManager {
//        this.isDebug = isDebug
//        return this
//    }
//
//    protected fun setVisibleHeight(height: Int) {
//        var height = height
//        if (height <= 0) {
//            height = 0
//        }
//        val layoutParams =
//            getLoadView().getLayoutParams() as LinearLayout.LayoutParams
//        layoutParams.height = height
//        getLoadView().setLayoutParams(layoutParams)
//    }
//
//    companion object {
//        private val INSTANCE: JRecycleViewManager = JRecycleViewManager()
//        val instance: JRecycleViewManager
//            get() = INSTANCE
//    }
//
//    protected fun smoothScrollTo(destHeight: Int, durTime: Int) {
//        // 设置从可见高度->目标高度
//        val valueAnimator = ValueAnimator.ofInt(getVisibleHeight(), destHeight)
//        valueAnimator.duration = durTime.toLong()
//        valueAnimator.addUpdateListener { animation -> setVisibleHeight((animation.animatedValue as Int)) }
//        valueAnimator.start()
//    }
//}