package com.lit.kcustomercore

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lit.base.mvvm.activity.BaseListActivity
import com.lit.krecyclerview.BaseViewHolder
import com.lit.krecyclerview.adapter.KRefreshAndLoadMoreAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : BaseListActivity() {
    private val mData: MutableList<String> = ArrayList<String>()
    private val adapter : StringAdapter

    private val data: MutableList<String> = ArrayList<String>()
    init {
        adapter = StringAdapter(data)
        initData()
        data.addAll(mData)
    }

    private fun initData() {
        for (i in 0 until 10){
            mData.add("12345")
        }
    }

    override fun getFirstData() {

        GlobalScope.launch {
            withContext(Dispatchers.IO){
                mData.clear()
                initData()
                delay(3000)
            }

            withContext(Dispatchers.Main){
                data.clear()
                data.addAll(mData)
                baseAdapter.onSuccess()
            }
        }

    }

    override fun loadMoreData() {

        GlobalScope.launch {
            withContext(Dispatchers.IO){
                initData()
                delay(3000)
            }

            addData()
        }

    }

    override fun getAdapter(): RecyclerView.Adapter<BaseViewHolder> {
        return adapter
    }

    private suspend fun addData(){
        withContext(Dispatchers.Main){
            data.addAll(mData)
            baseAdapter.setLoadComplete()
        }
    }
}
