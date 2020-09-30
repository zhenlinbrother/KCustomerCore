package com.lit.kcustomercore.ui.home.discovery

import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.lit.base.mvvm.fragment.BaseListFragment
import com.lit.kcustomercore.bean.Discovery
import com.lit.kcustomercore.extension.showToast
import com.lit.kcustomercore.utils.InjectorUtil
import com.lit.kcustomercore.utils.ResponseHandler
import com.lit.krecyclerview.BaseViewHolder

/**
 * author       : linc
 * time         : 2020/9/28
 * desc         : 发现页面
 * version      : 1.0.0
 */
class DiscoveryFragment : BaseListFragment<Discovery.Item>(){

    private val viewModel by lazy { ViewModelProvider(this, InjectorUtil.getDiscoveryViewModelFactory()).get(DiscoveryViewModel::class.java) }

    override fun getAdapter(): RecyclerView.Adapter<BaseViewHolder> {
        return DiscoveryAdapter(this, mData)
    }

    override fun getFirstData() {
        viewModel.onRefresh()
    }

    override fun loadMoreData() {
        viewModel.onLoadMore()
    }

    override fun initView(view: View) {
        observe()
    }

    override fun requestRefresh(): Boolean = false

    private fun observe(){
        viewModel.dataListLiveData.observe(viewLifecycleOwner, Observer {
            val response = it.getOrNull()
            if (response == null){
                if (!viewModel.isFirst && viewModel.nextPageUrl.isNullOrEmpty()){
                    baseAdapter.setNoMore()
                    return@Observer
                }
                ResponseHandler.getFailureTips(it.exceptionOrNull()).let { it ->
                    it.showToast()
                    if (viewModel.isFirst) baseAdapter.onError() else baseAdapter.setLoadError()
                }
                return@Observer

            }

            viewModel.nextPageUrl = response.nextPageUrl
            onHandleResponseData(response.itemList, viewModel.isFirst)
        })
    }

    companion object {
        fun newInstance() = DiscoveryFragment()
    }
}