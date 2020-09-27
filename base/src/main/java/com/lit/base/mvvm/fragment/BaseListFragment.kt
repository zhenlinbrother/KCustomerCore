package com.lit.base.mvvm.fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lit.base.R
import com.lit.base.base.adapter.BitFrameAdapter
import com.lit.base.base.listener.IStateListener
import com.lit.base.base.manager.BitManager
import com.lit.krecyclerview.BaseViewHolder
import com.lit.krecyclerview.KRecyclerView
import com.lit.krecyclerview.adapter.KRefreshAndLoadMoreAdapter
import kotlinx.android.synthetic.main.base_recycler_view.view.*

/**
 * author       : linc
 * time         : 2020/9/27
 * desc         : 带有上拉刷新和下拉刷新 基类 fragment
 * version      : 1.0.0
 */
abstract class BaseListFragment : BaseLazyFragment(), IStateListener{

    protected lateinit var kRecyclerView: KRecyclerView
    protected lateinit var baseAdapter: BitFrameAdapter

    protected var emptyView: Int? = null
        get() = BitManager.instance.emptyViewLayout
    protected var retryView: Int? = null
        get() = BitManager.instance.retryViewLayout
    protected var loadingView: Int? = null
        get() = BitManager.instance.loadingViewLayout
    protected var retryBtnId: Int? = null
        get() = BitManager.instance.retryBtnId
    protected var emptyBtnId: Int? = null
        get() = BitManager.instance.emptyBtnId

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        kRecyclerView = view.findViewById(R.id.base_recycle_view)
        initRecyclerView(kRecyclerView)
        setAdapter(getAdapter())
    }

    override fun getLayoutId(): Int {
        return R.layout.base_recycler_view
    }

    fun initRecyclerView(recyclerView: KRecyclerView){
        recyclerView.layoutManager = LinearLayoutManager(context)
    }

    private fun setAdapter(adapter: RecyclerView.Adapter<BaseViewHolder>){
        baseAdapter = BitFrameAdapter(context!!, adapter)

        baseAdapter.mEmptyView = emptyView
        baseAdapter.mLoadingView = loadingView
        baseAdapter.mRetryView = retryView
        baseAdapter.mRetryBtnId = retryBtnId
        baseAdapter.mEmptyBtnId = emptyBtnId

        baseAdapter.mIsOpenRefresh = requestRefresh()
        if (requestRefresh()){
            baseAdapter.mOnRefreshListener = object : KRefreshAndLoadMoreAdapter.OnRefreshListener{
                override fun onRefreshing() {
                    getFirstData()
                }
            }
        }

        baseAdapter.mIsOpenLoadMore = requestLoadMore()
        if (requestLoadMore()){
            baseAdapter.mOnLoadMoreListener = object : KRefreshAndLoadMoreAdapter.OnLoadMoreListener{
                override fun onLoading() {
                    loadMoreData()
                }
            }
        }

        baseAdapter.mStateListener = this
        kRecyclerView.adapter = baseAdapter
    }

    /**
     * 子view 可以加强 adapter 的功能
     * @param adapter Adapter<BaseViewHolder>
     */
    protected fun initAdapterForChild(adapter: RecyclerView.Adapter<BaseViewHolder>){

    }

    /**
     * 是否需要下拉刷新
     * @return Boolean
     */
    protected fun requestRefresh() = true

    /**
     * 是否需要加载更多
     * @return Boolean
     */
    protected fun requestLoadMore() = true

    abstract fun getAdapter(): RecyclerView.Adapter<BaseViewHolder>

    abstract fun getFirstData()

    abstract fun loadMoreData()

    override fun onRetry() {
        getFirstData()
    }

    override fun onLoading() {

    }

    override fun onEmpty() {
        getFirstData()
    }
}