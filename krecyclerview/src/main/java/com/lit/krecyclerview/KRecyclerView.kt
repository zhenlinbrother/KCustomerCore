package com.lit.krecyclerview

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView
import com.lit.krecyclerview.linstener.KRecyclerListener

class KRecyclerView : RecyclerView {

    companion object{
        const val TAG = "KRecyclerView.java"

        const val DRAG_FACTOR = 1
    }

    var mLastY : Float = -1f
    //刷新视图的下标
    var mRefreshViewPos : Int = 1
    //用于获取 Item 的范围
    val mFrame : Rect ? = null
    //是否正在触碰
    var mIsTouching : Boolean = false
    //是否正在滚动
    var mIsScrolling : Boolean = false

    lateinit var mListener : KRecyclerListener

    fun setListener(listener: KRecyclerListener) {
        mListener = listener
    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )
}