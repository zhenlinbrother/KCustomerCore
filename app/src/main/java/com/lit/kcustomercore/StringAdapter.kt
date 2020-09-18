package com.lit.kcustomercore

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lit.krecyclerview.BaseViewHolder
import kotlinx.android.synthetic.main.item_string.view.*

class StringAdapter(data : List<String>?) :
    RecyclerView.Adapter<BaseViewHolder>() {

    private val mData: List<String>? = data

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ContentViewHolder {
        return ContentViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_string, parent, false))
    }

    override fun getItemCount(): Int {
        return mData?.size ?: 0
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val contentViewHolder = holder as ContentViewHolder
        contentViewHolder.itemView.tv_content.text = mData?.get(position)
    }

    class ContentViewHolder(itemView: View) : BaseViewHolder(itemView) {}

}