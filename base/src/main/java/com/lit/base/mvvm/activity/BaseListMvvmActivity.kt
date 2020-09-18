package com.lit.base.mvvm.activity

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lit.base.R
import com.lit.base.base.adapter.BitFrameAdapter
import com.lit.base.base.listener.IStateListener
import com.lit.base.base.manager.BitManager
import com.lit.base.mvvm.viewmodel.IMvvmBaseViewModel
import com.lit.krecyclerview.BaseViewHolder
import com.lit.krecyclerview.KRecyclerView
import com.lit.krecyclerview.adapter.KRefreshAndLoadMoreAdapter
import kotlinx.android.synthetic.main.base_recycler_view.view.*

/**
 * <基于MVVM模式的 带有上拉刷新和下拉加载 基类 Activity> <功能详细描述>
 *
 * @author linc
 * @version 2020/9/17
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
abstract class BaseListMvvmActivity<V : ViewDataBinding?, VM : IMvvmBaseViewModel<View>> : BaseMvvmActivity<V, VM>(), IStateListener {

    var recyclerView : KRecyclerView? = null
    var baseAdapter : BitFrameAdapter? = null

    var requestLoadMore = true
    var requestRefresh = true

    var retryView : Int = 0
        get() {return BitManager.instance.retryViewLayout}
    var loadingView : Int = 0
        get() = BitManager.instance.loadingViewLayout
    var emptyView : Int = 0
        get() = BitManager.instance.emptyViewLayout
    var retryBtnId : Int = 0
        get() = BitManager.instance.retryBtnId
    var emptyBtnId : Int = 0
        get() = BitManager.instance.emptyBtnId

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        recyclerView = binding!!.root.findViewById(R.id.recycle_view)
        recyclerView!!.layoutManager = LinearLayoutManager(this)
        setAdapter(getAdapter())
        baseAdapter!!.onLoading()
        getFirstData()
    }

    private fun setAdapter(adapter: RecyclerView.Adapter<BaseViewHolder>){
        baseAdapter = BitFrameAdapter(this, adapter)

        baseAdapter!!.mRetryView = retryView
        baseAdapter!!.mLoadingView = loadingView
        baseAdapter!!.mEmptyView = emptyView
        baseAdapter!!.mRetryBtnId = retryBtnId
        baseAdapter!!.mEmptyBtnId = emptyBtnId

        baseAdapter!!.mIsOpenRefresh = requestRefresh
        if (requestRefresh){
            baseAdapter!!.mOnRefreshListener = object : KRefreshAndLoadMoreAdapter.OnRefreshListener{
                override fun onRefreshing() {
                    getFirstData()
                }
            }
        }

        baseAdapter!!.mIsOpenLoadMore = requestLoadMore
        baseAdapter!!.mOnLoadMoreListener = object : KRefreshAndLoadMoreAdapter.OnLoadMoreListener{
            override fun onLoading() {
                getFirstData()
            }
        }

        baseAdapter!!.mStateListener = this
        initAdapterForChild(adapter)
        recyclerView!!.adapter = baseAdapter
    }

    /**
     * 第一次获取数据
     */
    abstract fun getFirstData()

    abstract fun getAdapter() : RecyclerView.Adapter<BaseViewHolder>

    /**
     * 子View 可以加强 adapter 的功能
     */
    fun initAdapterForChild(adapter: RecyclerView.Adapter<BaseViewHolder>){}
}