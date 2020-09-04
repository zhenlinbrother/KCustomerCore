package com.lit.krecyclerview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.lit.krecyclerview.R
import com.lit.krecyclerview.loadView.base.IBaseLoadMoreView
import com.lit.krecyclerview.widget.BallSpinFadeLoader

/**
 * <上拉刷新视图> <功能详细描述>
 *
 * @author linc
 * @version 2020/8/31
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
class OrdinaryLoadMoreView : IBaseLoadMoreView {

    private var mLoadMoreView: View? = null
    private var mProgressBar : BallSpinFadeLoader? = null
    private var mTvTip: TextView? = null
    private var mIvReload: ImageView? = null

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onNoMore() {
        mProgressBar?.visibility = GONE
        mTvTip?.text = this.context.getString(R.string.l_recycler_no_more)
        mTvTip!!.visibility = VISIBLE
        mIvReload!!.visibility = GONE
    }

    override fun onError() {
        mProgressBar?.visibility = GONE
        mTvTip?.text = this.context.getString(R.string.l_recycler_reload)
        mTvTip?.visibility = VISIBLE
        mIvReload?.visibility = VISIBLE
    }

    override fun getLoadView(): View {
        return this.mLoadMoreView!!
    }

    override fun initView(context: Context): View {
        this.mLoadMoreView = LayoutInflater.from(context)
            .inflate(R.layout.l_widget_oridinary_load_more_view, this, false)
        mProgressBar = this.mLoadMoreView!!.findViewById(R.id.ball_loader) as BallSpinFadeLoader
        mTvTip = mLoadMoreView!!.findViewById(R.id.tv_tip) as TextView
        mIvReload = mLoadMoreView!!.findViewById(R.id.iv_reload) as ImageView
        return this.mLoadMoreView as View
    }

    override fun onPullToAction() {
        mProgressBar?.visibility = GONE
        mTvTip?.text = this.context.getString(R.string.l_recycler_pull_to_load)
        mTvTip!!.visibility = VISIBLE
        mIvReload!!.visibility = GONE
    }

    override fun onReleaseAction() {
        mProgressBar?.visibility = GONE
        mTvTip?.text = this.context.getString(R.string.l_recycler_release_to_load)
        mTvTip!!.visibility = VISIBLE
        mIvReload!!.visibility = GONE
    }

    override fun onExecuting() {
        mProgressBar?.visibility = VISIBLE
        mTvTip?.text = this.context.getString(R.string.l_recycler_loading)
        mTvTip!!.visibility = VISIBLE
        mIvReload!!.visibility = GONE
    }

    override fun onDone() {
        mProgressBar?.visibility = GONE
        mTvTip?.text = this.context.getString(R.string.l_recycler_loaded)
        mTvTip!!.visibility = VISIBLE
        mIvReload!!.visibility = GONE
    }
}