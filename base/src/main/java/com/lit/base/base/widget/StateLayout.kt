package com.lit.base.base.widget

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.lit.base.base.manager.BitManager


/**
 * author       : linc
 * time         : 2020/9/27
 * desc         : 各种状态的layout
 * version      : 1.0.0
 */
class StateLayout(context: Context) : FrameLayout(context){

    private val TAG = StateLayout::class.java.simpleName

    //加载的view
    private var mLoadingView: View? = null

    //重试的view
    private var mRetryView: View? = null

    //空内容的View
    private var mEmptyView: View? = null

    //内容View
    private var mContentView: View? = null

    private var mInflater: LayoutInflater? = null

    //是否正在刷新
    fun isLoading(): Boolean {
        return mLoadingView != null && mLoadingView!!.visibility === View.VISIBLE
    }

    fun showLoading() {
        showView(mLoadingView)
    }

    fun showRetry() {
        showView(mRetryView)
    }

    fun showContent() {
        showView(mContentView)
    }

    fun showEmpty() {
        showView(mEmptyView)
    }

    //显示参数中的view
    private fun showView(stateView: View?) {
        if (mRetryView != null) {
            mRetryView!!.visibility = if (stateView === mRetryView) View.VISIBLE else View.GONE
        }
        if (mContentView != null) {
            mContentView!!.visibility = if (stateView === mContentView) View.VISIBLE else View.GONE
        }
        if (mEmptyView != null) {
            mEmptyView!!.visibility = if (stateView === mEmptyView) View.VISIBLE else View.GONE
        }
        if (mLoadingView != null) {
            mLoadingView!!.visibility = if (stateView === mLoadingView) View.VISIBLE else View.GONE
        }
    }

    fun setContentView(layoutId: Int): View? {
        return setContentView(mInflater!!.inflate(layoutId, this, false))
    }

    fun setLoadingView(layoutId: Int): View? {
        return setLoadingView(mInflater!!.inflate(layoutId, this, false))
    }

    fun setEmptyView(layoutId: Int): View? {
        return setEmptyView(mInflater!!.inflate(layoutId, this, false))
    }

    fun setRetryView(layoutId: Int): View? {
        return setRetryView(mInflater!!.inflate(layoutId, this, false))
    }

    fun setLoadingView(view: View?): View? {
        val loadingView = mLoadingView
        if (loadingView != null && BitManager.instance.isDebug) {
            Log.w(TAG, "you have already set a loading view and would be instead of this new one.")
        }
        removeView(loadingView)
        addView(view)
        mLoadingView = view
        return mLoadingView
    }

    fun setEmptyView(view: View?): View? {
        val emptyView = mEmptyView
        if (emptyView != null && BitManager.instance.isDebug) {
            Log.w(TAG, "you have already set a empty view and would be instead of this new one.")
        }
        removeView(emptyView)
        addView(view)
        mEmptyView = view
        return mEmptyView
    }

    fun setRetryView(view: View?): View? {
        val retryView = mRetryView
        if (retryView != null && BitManager.instance.isDebug) {
            Log.w(TAG, "you have already set a retry view and would be instead of this new one.")
        }
        removeView(retryView)
        addView(view)
        mRetryView = view
        return mRetryView
    }

    fun setContentView(view: View?): View? {
        val contentView = mContentView
        if (contentView != null && BitManager.instance.isDebug) {
            Log.w(TAG, "you have already set a content view and would be instead of this new one.")
        }
        removeView(contentView)
        addView(view)
        mContentView = view
        return mContentView
    }

    fun getRetryView(): View? {
        return mRetryView
    }

    fun getLoadingView(): View? {
        return mLoadingView
    }

    fun getContentView(): View? {
        return mContentView
    }

    fun getEmptyView(): View? {
        return mEmptyView
    }


}