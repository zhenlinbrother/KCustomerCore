package com.lit.kcustomercore

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.lit.base.mvvm.fragment.BaseListFragment
import com.lit.krecyclerview.BaseViewHolder

class TestFragment : BaseListFragment<String>(){

    private var data: MutableList<String> = ArrayList()
    override fun getAdapter(): RecyclerView.Adapter<BaseViewHolder> {
        return StringAdapter(mData)
    }

    override fun getFirstData() {
        for (i in 0..10){
            data.add("122")
        }

        onHandleResponseData(data, true)
    }

    override fun loadMoreData() {

    }

    override fun initView(view: View) {

    }

    companion object {
        fun newInstance() = TestFragment()
    }
}