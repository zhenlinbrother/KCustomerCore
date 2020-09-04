package com.lit.krecyclerview.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lit.krecyclerview.OrdinaryLoadMoreView
import com.lit.krecyclerview.config.KRecyclerConfig
import com.lit.krecyclerview.config.KRecyclerManager
import com.lit.krecyclerview.loadView.OrdinaryRefreshLoadView
import com.lit.krecyclerview.loadView.base.IBaseLoadMoreView
import com.lit.krecyclerview.loadView.base.IBaseRefreshLoadView

/**
 * author       : liaozhenlin
 * time         : 2020/8/30 0030 13:16
 * desc         : 刷新和下拉更多包装类
 * version      : 1.1.0
 */
class KRefreshAndLoadMoreAdapter(
    context: Context?,
    var realAdapter: RecyclerView.Adapter<*>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var mIsOpenRefresh = true
    var mIsOpenLoadMore = true
    var mRefreshLoadView : IBaseRefreshLoadView? = null
    var mLoadMoreView: IBaseLoadMoreView? = null
    var mOnLoadMoreListener : OnLoadMoreListener? = null
    var mOnRefreshListener : OnRefreshListener? = null

    init {
        if (mRefreshLoadView == null){
            if (KRecyclerManager.instance.mRefreshLoadView == null){
                this.mRefreshLoadView = OrdinaryRefreshLoadView(context)
            } else {
                this.mRefreshLoadView = KRecyclerManager.instance.mRefreshLoadView
            }
        }

        if (this.mLoadMoreView == null){
            if (KRecyclerManager.instance.mLoadMoreView == null){
                this.mLoadMoreView = OrdinaryLoadMoreView(context)
            } else {
                this.mLoadMoreView = KRecyclerManager.instance.mLoadMoreView
            }
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        val manager = recyclerView.layoutManager
        if (manager is GridLayoutManager){
            val  gridLayoutManager = manager
            gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup(){
                override fun getSpanSize(position: Int): Int {
                    return if (getItemViewType(position) != KRecyclerConfig.HEAD
                        && getItemViewType(position) != KRecyclerConfig.FOOT)
                        1 else gridLayoutManager.spanCount
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == KRecyclerConfig.HEAD){
            if (this.mOnRefreshListener != null){
                this.mRefreshLoadView?.mOnRefreshListener = this.mOnRefreshListener
            }

            return KRefreshAndLoadMoreAdapter.KRefreshViewHolder(this.mRefreshLoadView!!)
        } else if (viewType == KRecyclerConfig.FOOT){
            if (this.mOnLoadMoreListener != null){
                this.mLoadMoreView?.onLoadMoreListener = this.mOnLoadMoreListener
            }

            return KLoadMoreViewHolder(this.mLoadMoreView!!)
        } else{
            return this.realAdapter.onCreateViewHolder(parent, viewType)
        }
    }

    override fun getItemCount(): Int {
        var count = realAdapter.itemCount
        if (mIsOpenRefresh){
            ++count
        }

        if (mIsOpenLoadMore){
            ++count
        }

        return count
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder !is KRefreshViewHolder && holder !is KLoadMoreViewHolder){
            realAdapter.run { onBindViewHolder(holder, RealPosition(position)) }
        }
    }

    private fun RealPosition(position : Int) : Int{
        return if (mIsOpenRefresh) position - 1 else position
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0 && mIsOpenRefresh){
            KRecyclerConfig.HEAD
        } else {
            if (position == itemCount - 1 && mIsOpenLoadMore )
                KRecyclerConfig.FOOT
            else
                realAdapter.getItemViewType(RealPosition(position))
        }
    }

    fun setRefreshComplete(){
        mRefreshLoadView?.refreshComplete()
        notifyDataSetChanged()
    }

    fun setLoadComplete(){
        if (mIsOpenLoadMore){
            mLoadMoreView?.loadComplete()
        }
    }

    fun setLoadError(){
        if (mIsOpenLoadMore){
            mLoadMoreView?.loadError()
        }
    }

    fun resetLoadMore(){
        if (mIsOpenLoadMore){
            mLoadMoreView?.reset()
        }
    }

    fun setNoMore(){
        if (mIsOpenLoadMore){
            mLoadMoreView?.noMore()
        }
    }

    interface OnRefreshListener{
        /**
         * 刷新中回调
         */
        fun onRefreshing()
    }

    interface OnLoadMoreListener{

        /**
         * 加载更多数据中回调，加载完成后，可调用一下方法
         */
        fun onLoading()
    }

    class KLoadMoreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    class KRefreshViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}