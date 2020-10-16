package com.lit.base.mvvm.activity

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lit.base.R
import com.lit.base.base.adapter.BitFrameAdapter
import com.lit.base.base.listener.IStateListener
import com.lit.base.base.manager.BitManager
import com.lit.krecyclerview.BaseViewHolder
import com.lit.krecyclerview.KRecyclerView
import com.lit.krecyclerview.adapter.KRefreshAndLoadMoreAdapter

/**
 * 基类 上拉刷新和下拉刷新 activity
 * @property kRecyclerView KRecyclerView
 * @property baseAdapter BitFrameAdapter
 * @property emptyView Int?
 * @property retryView Int?
 * @property loadingView Int?
 * @property retryBtnId Int?
 * @property emptyBtnId Int?
 */
abstract class BaseListActivity : BaseActivity(), IStateListener {

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        kRecyclerView = findViewById(R.id.base_recycle_view)
        initRecyclerView(kRecyclerView)
        initIntent()
        initData()
        setAdapter(getAdapter())
        baseAdapter.onLoading()
        getFirstData()
    }

    /**
     * 初始化recyclerview
     * @param recyclerView KRecyclerView
     */
    fun initRecyclerView(recyclerView: KRecyclerView){
        kRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    /**
     * 设置adapter 信息
     * @param adapter Adapter<BaseViewHolder>
     */
    fun setAdapter(adapter: RecyclerView.Adapter<BaseViewHolder>){
        baseAdapter = BitFrameAdapter(this, adapter)

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
        initAdapterForChild(adapter)
        kRecyclerView.adapter = baseAdapter
    }

    override fun getLayoutId(): Int {
        return R.layout.base_recycler_view
    }

    /**
     * 是否请求刷新
     * @return Boolean
     */
    fun requestRefresh(): Boolean{
        return true
    }

    /**
     * 是否请求加载更多
     * @return Boolean
     */
    fun requestLoadMore(): Boolean{
        return true
    }

    /**
     * 子view 可以加强 adapter 的功能
     * @param adapter Adapter<BaseViewHolder>
     */
    fun initAdapterForChild(adapter: RecyclerView.Adapter<BaseViewHolder>){}

    /**
     * 第一次拉取数据
     */
    abstract fun getFirstData()

    /**
     * 加载更多数据
     */
    abstract fun loadMoreData()

    abstract fun getAdapter(): RecyclerView.Adapter<BaseViewHolder>

    override fun onRetry() {
        getFirstData()
    }

    override fun onLoading() {

    }

    override fun onEmpty() {
        getFirstData()
    }

    open fun initData(){}

    open fun initIntent(){}
}