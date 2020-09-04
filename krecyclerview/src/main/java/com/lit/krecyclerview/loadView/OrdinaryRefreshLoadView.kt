package com.lit.krecyclerview.loadView

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.TextView
import com.lit.krecyclerview.R
import com.lit.krecyclerview.loadView.base.IBaseRefreshLoadView
import com.lit.krecyclerview.widget.BallSpinFadeLoader

/**
 * <下拉刷新视图> <功能详细描述>
 *
 * @author linc
 * @version 2020/8/31
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
class OrdinaryRefreshLoadView(
    context: Context?,
    attributes: AttributeSet? = null,
    defStyleAttr: Int = 0
) : IBaseRefreshLoadView(context, attributes, defStyleAttr) {

    private var mLoadView : View? = null
    private var mTvRefreshStatus : TextView? = null
    private var mIvArrow : ImageView? = null
    private var mBallLoader : BallSpinFadeLoader? = null
    private var mArrowToUpAnim : RotateAnimation?= null
    private var mArrowToDownAnim : RotateAnimation? = null

    init {
        mArrowToUpAnim = RotateAnimation(0.0F, 180.0F, 1, 0.5F, 1,0.5F)
        mArrowToUpAnim?.duration = 180L
        mArrowToUpAnim?.fillAfter = true
        mArrowToDownAnim = RotateAnimation(180F, 0.0F, 1, 0.5F, 1, 0.5F)
        mArrowToDownAnim?.duration = 180L
        mArrowToDownAnim?.fillAfter = true

    }

    override fun getLoadView(): View {
        return mLoadView as View
    }

    override fun initView(context: Context): View {
        this.mLoadView = LayoutInflater.from(context).inflate(R.layout.l_widget_oridinary_refresh_view, this, false)
        this.mTvRefreshStatus = mLoadView?.findViewById(R.id.tv_refresh_status)
        this.mIvArrow = mLoadView?.findViewById(R.id.iv_arrow)
        this.mBallLoader = mLoadView?.findViewById(R.id.ball_loader)
        return mLoadView as View
    }

    override fun onPullToAction() {
        this.mBallLoader?.visibility = View.GONE
        this.mIvArrow?.visibility = View.VISIBLE
        this.mTvRefreshStatus?.text = this.context.getString(R.string.l_recycler_pull_to_refresh)
        if (this.mCurState == 4){
            this.mIvArrow?.startAnimation(this.mArrowToDownAnim)
        } else {
            this.mIvArrow?.clearAnimation()
        }
    }

    override fun onReleaseAction() {
        this.mBallLoader?.visibility = View.GONE
        this.mIvArrow?.visibility = View.VISIBLE
        this.mTvRefreshStatus?.text = this.context.getString(R.string.l_recycler_release_to_refresh)
        this.mIvArrow?.clearAnimation()
        this.mIvArrow?.startAnimation(this.mArrowToUpAnim)
    }

    override fun onExecuting() {
        this.mBallLoader?.visibility = View.VISIBLE
        this.mIvArrow?.clearAnimation()
        this.mIvArrow?.visibility = View.GONE
        this.mTvRefreshStatus?.text = this.context.getString(R.string.l_recycler_refreshing)
    }

    override fun onDone() {
        this.mIvArrow?.clearAnimation()
        this.mIvArrow?.visibility = View.GONE
        this.mBallLoader?.visibility = View.GONE
        this.mTvRefreshStatus?.text = this.context.getString(R.string.l_recycler_refreshed)
    }


}