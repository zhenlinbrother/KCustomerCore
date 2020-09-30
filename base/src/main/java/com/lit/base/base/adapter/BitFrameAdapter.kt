package com.lit.base.base.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.lit.base.base.listener.IStateListener
import com.lit.base.base.listener.StateViewHolderListener
import com.lit.base.base.manager.BitManager
import com.lit.krecyclerview.BaseViewHolder
import com.lit.krecyclerview.adapter.KRefreshAndLoadMoreAdapter
import com.lit.krecyclerview.config.KRecyclerConfig

class BitFrameAdapter(context: Context,
                      realAdapter: RecyclerView.Adapter<BaseViewHolder>
) : KRefreshAndLoadMoreAdapter(context, realAdapter) {

    companion object{
        const val TAG = "BitFrameAdapter.java"
        /**
         * 重试类型
         */
        const val RETRY_TYPE = 0xABC201
        /**
         * 加载类型
         */
        const val LOADING_TYPE = 0xABC202
        /**
         * 空类型
         */
        const val EMPTY_TYPE = 0xABC203
        /**
         * 成功类型
         */
        const val SUCCESS_TYPE = 0xABC204
    }

    private var mCurrentType = LOADING_TYPE
    private var mLayoutInflater = LayoutInflater.from(context)

    var mEmptyView : Int? = null
    var mRetryView : Int? = null
    var mLoadingView : Int? = null
    var mRetryBtnId : Int? = null
    var mEmptyBtnId : Int? = null

    var mStateListener : IStateListener? = null
    var mStateViewHolderListener: StateViewHolderListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            RETRY_TYPE ->  RetryViewHolder(mLayoutInflater.inflate(mRetryView!!, parent, false))

            LOADING_TYPE -> LoadingViewHolder(mLayoutInflater.inflate(mLoadingView!!, parent, false))

            EMPTY_TYPE -> EmptyViewHolder(mLayoutInflater.inflate(mEmptyView!!, parent, false))

            else -> return super.onCreateViewHolder(parent, viewType)
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (mStateListener == null){
            super.onBindViewHolder(holder, position)
            return
        }

        if (holder is RetryViewHolder){
            mStateViewHolderListener?.handleRetryViewHolder(holder as RetryViewHolder)

            if (mEmptyBtnId == BitManager.NO_CLICK){
                return
            }

            if (mRetryBtnId == BitManager.NONE){
                holder.itemView.setOnClickListener {
                    mCurrentType = LOADING_TYPE
                    notifyDataSetChanged()
                    mStateListener?.onRetry()
                }
            } else {
                holder.itemView
                    .findViewById<View>(mRetryBtnId!!)
                    .setOnClickListener{
                        mCurrentType = LOADING_TYPE
                        super@BitFrameAdapter.notifyDataSetChanged()
                        mStateListener!!.onRetry()
                    }
            }

        } else if (holder is LoadingViewHolder){
            mStateViewHolderListener?.handleLoadingViewHolder(holder as LoadingViewHolder)
            mStateListener?.onLoading()

        } else if (holder is EmptyViewHolder){
            mStateViewHolderListener?.handleEmptyViewHolder(holder as EmptyViewHolder)

            if (mEmptyBtnId == BitManager.NO_CLICK){
                return
            }

            if (mEmptyBtnId == BitManager.NONE){
                holder.itemView.setOnClickListener {
                    mCurrentType = LOADING_TYPE
                    super@BitFrameAdapter.notifyDataSetChanged()
                    mStateListener?.onEmpty()
                }
            } else {
                val emptyBtnView = holder.itemView.findViewById<View>(mEmptyBtnId!!)
                emptyBtnView?.setOnClickListener {
                    mCurrentType = LOADING_TYPE
                    super@BitFrameAdapter.notifyDataSetChanged()
                    mStateListener?.onEmpty()
                }
            }
        } else {
            super.onBindViewHolder(holder, position)
        }
    }

    override fun getItemCount(): Int {
        if (isState()){
            return 1
        }
        return super.getItemCount()
    }

    override fun getItemViewType(position: Int): Int {
        if (mCurrentType == SUCCESS_TYPE){
            return super.getItemViewType(position)
        } else{
            return mCurrentType
        }
    }

    private fun isState(): Boolean { return mCurrentType != SUCCESS_TYPE }

    /**
     * 设置为加载状态
     */
    fun onLoading(){
        mCurrentType = LOADING_TYPE
        this.notifyDataSetChanged()
    }

    fun onSuccess(){
        super.setRefreshComplete()
        mCurrentType = SUCCESS_TYPE
        this.notifyDataSetChanged()
    }

    fun onError(){
        mCurrentType = RETRY_TYPE
        this.notifyDataSetChanged()
    }

    fun onEmpty(){
        mCurrentType = EMPTY_TYPE
        this.notifyDataSetChanged()
    }


    /**
     * 重试视图
     */
    class RetryViewHolder(itemView: View) : BaseViewHolder(itemView)
    /**
     * 加载视图
     */
    class LoadingViewHolder(itemView: View) : BaseViewHolder(itemView)
    /**
     * 空视图
     */
    class EmptyViewHolder(itemView: View) : BaseViewHolder(itemView)

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        val manager = recyclerView.layoutManager

        if (manager is GridLayoutManager){

            manager.spanSizeLookup = object : SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    val itemViewType = getItemViewType(position)

                    if (itemViewType == RETRY_TYPE ||
                            itemViewType == SUCCESS_TYPE ||
                            itemViewType == EMPTY_TYPE ||
                            itemViewType == LOADING_TYPE ||
                            itemViewType == KRecyclerConfig.FOOT ||
                            itemViewType == KRecyclerConfig.HEAD){
                        return manager.spanCount
                    }

                    return 1
                }
            }
        }

    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        val layoutParams = holder.itemView.layoutParams

        if (holder.layoutPosition == 0
            && layoutParams != null
            && layoutParams is StaggeredGridLayoutManager.LayoutParams){
            layoutParams.isFullSpan = true
        }

        Log.d(TAG, "onViewAttachedToWindow: position = ${holder.layoutPosition}, isLoadingHolder: ${holder is LoadingViewHolder} ")

        if (holder.layoutPosition != 0
            && layoutParams != null
            && holder is KRefreshAndLoadMoreAdapter.KLoadMoreViewHolder
            && layoutParams is StaggeredGridLayoutManager.LayoutParams){
            layoutParams.isFullSpan = true
        }

        if (realAdapter != null && holder !is LoadingViewHolder){
            realAdapter.onViewAttachedToWindow(holder as BaseViewHolder)
        }
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {

            realAdapter?.onViewDetachedFromWindow(holder as BaseViewHolder)

    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        realAdapter.onDetachedFromRecyclerView(recyclerView)
    }
}

