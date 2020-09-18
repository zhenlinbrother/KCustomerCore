package com.lit.kcustomercore

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.lit.krecyclerview.BaseViewHolder
import com.lit.krecyclerview.adapter.KRefreshAndLoadMoreAdapter
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {
    private val mData: MutableList<String> = ArrayList<String>()
    private val adapter : StringAdapter


    init {
        adapter = StringAdapter(mData)
        initData()
    }

    private fun initData() {
        for (i in 0 until 10){
            mData.add("12345")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mAdapter = KRefreshAndLoadMoreAdapter(this, adapter)

        mAdapter.mIsOpenLoadMore = true
        mAdapter.mIsOpenRefresh = true
        mAdapter.mOnRefreshListener = object : KRefreshAndLoadMoreAdapter.OnRefreshListener{
            override fun onRefreshing() {
                mData.clear()
                initData()
                mAdapter.notifyDataSetChanged()
                mAdapter.setRefreshComplete()
            }

        }


        mAdapter.mOnLoadMoreListener = object : KRefreshAndLoadMoreAdapter.OnLoadMoreListener{
            override fun onLoading() {
                initData()
                mAdapter.notifyDataSetChanged()
                mAdapter.setLoadComplete()
            }

        }

        k_recycler_view.layoutManager = LinearLayoutManager(this)
        k_recycler_view.adapter = mAdapter

    }
}
