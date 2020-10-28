package com.lit.kcustomercore.ui.home.daily

import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.eyepetizer.android.logic.model.Daily
import com.lit.base.mvvm.fragment.BaseListFragment
import com.lit.kcustomercore.utils.InjectorUtil
import com.lit.krecyclerview.BaseViewHolder

/**
 * author       : linc
 * time         : 2020/10/16
 * desc         : 首页-日报列表
 * version      : 1.0.0
 */
class DailyFragment : BaseListFragment<Daily.Item>() {

    private val viewModel by lazy {
        ViewModelProvider(this,
            InjectorUtil.getDailyViewModelFactory())
            .get(DailyViewModel::class.java)
    }

    override fun getAdapter(): RecyclerView.Adapter<BaseViewHolder> {
        return DailyAdapter(this, mData)
    }

    override fun getFirstData() {
        viewModel.onRefresh()
    }

    override fun loadMoreData() {
        viewModel.onLoadMore()
    }

    override fun initView(view: View) {
        viewModel.dataListLiveData.observe(viewLifecycleOwner, Observer {
            val  response = it.getOrNull()

            viewModel.nextPageUrl = response?.nextPageUrl
            onHandleResponseData(response?.itemList, viewModel.isFirst)
        })
    }

    override fun onFragmentResume() {
        super.onFragmentResume()
        Log.d(TAG, "onFragmentResume: mData.size:" + mData.size)
        baseAdapter.notifyDataSetChanged()
    }

    companion object {
        fun newInstance() = DailyFragment()
    }
}