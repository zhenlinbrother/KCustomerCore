package com.lit.kcustomercore.ui.home.commend

import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.lit.base.mvvm.fragment.BaseListFragment
import com.lit.kcustomercore.bean.HomePageRecommend
import com.lit.kcustomercore.utils.InjectorUtil
import com.lit.krecyclerview.BaseViewHolder

/**
 * author       : linc
 * time         : 2020/9/30
 * desc         : 首页--推荐列表界面
 * version      : 1.0.0
 */
class CommendFragment : BaseListFragment<HomePageRecommend.Item>(){

    private val viewModel by lazy { ViewModelProvider(this, InjectorUtil.getHomePageCommendViewModelFactory()).get(CommendViewModel::class.java) }

    override fun getAdapter(): RecyclerView.Adapter<BaseViewHolder> {
        return CommendAdapter(this, mData)
    }

    override fun getFirstData() {
        viewModel.onRefresh()
    }

    override fun loadMoreData() {
        viewModel.onLoadMore()
    }

    override fun initView(view: View) {

        viewModel.dataListLiveData.observe(viewLifecycleOwner, Observer {
            val response = it.getOrNull()
            
            viewModel.nextPageUrl = response?.nextPageUrl
            onHandleResponseData(response?.itemList, viewModel.isFirst)
        })
    }

    companion object {
        fun newInstance() = CommendFragment()
    }
}