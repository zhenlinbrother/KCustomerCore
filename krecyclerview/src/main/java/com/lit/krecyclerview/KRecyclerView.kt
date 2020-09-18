package com.lit.krecyclerview

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.MotionEvent.*
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.lit.krecyclerview.adapter.KRefreshAndLoadMoreAdapter
import com.lit.krecyclerview.linstener.IStick
import com.lit.krecyclerview.linstener.KRecyclerListener
import com.lit.krecyclerview.loadView.base.IBaseLoadMoreView
import com.lit.krecyclerview.loadView.base.IBaseRefreshLoadView
import com.lit.krecyclerview.swipe.KSwipeItemLayout
import com.lit.krecyclerview.utils.LogUtils

class KRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    companion object{
        const val TAG = "KRecyclerView.java"
        const val DRAG_FACTOR = 1
    }

    private var mLastY : Float = -1.0f
    private var mRefreshViewPos : Int = 0
    private val mFrame : Rect = Rect()
    private var mIsTouching : Boolean = false
    private var isScrolling :Boolean = false
    private var mListener : KRecyclerListener? = null

    init {
        addOnScrollListener(ScrollerListener())
        overScrollMode = View.OVER_SCROLL_NEVER
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val action = ev.actionMasked
        if (action == ACTION_DOWN) {
            val x = ev.x.toInt()
            val y = ev.y.toInt()
            val openItemView = findOpenItem()
            if (openItemView != null && openItemView !== getTouchItem(x, y)) {
                val swipeItemLayout: KSwipeItemLayout? = findSwipeItemLayout(openItemView)
                if (swipeItemLayout != null) {
                    swipeItemLayout.close()
                    return false
                }
            }
        } else if (action == ACTION_POINTER_DOWN) {
            return false
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onTouchEvent(e: MotionEvent): Boolean {
        if (mLastY == -1.0f) {
            mLastY = e.rawY
        }
        when (e.actionMasked) {
            ACTION_DOWN -> mLastY = e.rawY
            ACTION_UP, ACTION_CANCEL, ACTION_OUTSIDE -> {
                mLastY = -1.0f
                handleRefreshLoad()
                handleLoadMore()
                if (mListener != null) {
                    mListener!!.onUp(e)
                }
                mIsTouching = false
                if (isScrollStick()) {
                    return true
                }
            }
            ACTION_MOVE -> {
                mIsTouching = true
                val deltaY = (e.rawY - mLastY).toInt()
                LogUtils.i("loadMoreView: [rawY: " + e.rawY + "; " + "lastY: " + mLastY + "; " + "deltaY: " + deltaY + "]")
                mLastY = e.rawY
                if (handleTouch(e, deltaY.toFloat())) {
                    e.action = ACTION_DOWN
                    super.onTouchEvent(e)
                    return false
                }
                var visibleHeight: Int
                if (getRefreshLoadView() != null && isScrolledTop(e)) {
                    visibleHeight = getRefreshVisibleHeight()
                    if (visibleHeight != -1) {
                        getRefreshLoadView()!!.onMove(visibleHeight, (deltaY shr 1))
                    }
                    if (visibleHeight > 0 && getRefreshLoadView()!!.getCurState() < 8) {
                        e.action = ACTION_DOWN
                        super.onTouchEvent(e)
                        return false
                    }
                }
                if (getLoadMoreView() != null && isScrolledBottom()) {
                    visibleHeight = getLoadMoreVisibleHeight()
                    if (visibleHeight != -1) {
                        getLoadMoreView()!!
                            .onMove(visibleHeight, (-deltaY shr 1).toFloat())
                        return super.onTouchEvent(e)
                    }
                }
            }
        }
        return super.onTouchEvent(e)
    }

    //处理触碰事件（可在此制作黏性标题栏事件)
    private fun handleTouch(e: MotionEvent, deltaY: Float): Boolean {
        return if (mListener != null) {
            LogUtils.i("mListener.onTouch: [rawY: " + e.rawY + "; " + "lastY: " + mLastY + "; " + "deltaY: " + deltaY + "]")
            mLastY = e.rawY
            mListener!!.onTouch(e, deltaY)
        } else {
            false
        }
    }

    //判断是否滑动到顶部
    //todo 可以继续优化当LayoutManager 为网格布局或者瀑布流布局时
    private fun isScrolledTop(e: MotionEvent): Boolean {
        if (this.layoutManager is StaggeredGridLayoutManager) {
            val layoutManager =
                layoutManager as StaggeredGridLayoutManager?
            return (this.childCount > 1 && getChildAt(0) is IBaseRefreshLoadView
                    && getChildAt(1).y >= 0.0f && layoutManager!!.findFirstVisibleItemPositions(
                IntArray(
                    layoutManager.spanCount
                )
            )[0] == 0)
        }
        return (this.childCount > 1 && getChildAt(0) is IBaseRefreshLoadView
                && (this.layoutManager as LinearLayoutManager?)!!.findFirstVisibleItemPosition() <= 1 && getChildAt(
            1
        ).y >= 0.0f)
    }

    //判断是否滑动到底部
    //todo 可以继续优化当LayoutManager 为网格布局或者瀑布流布局时
    private fun isScrolledBottom(): Boolean {
        val itemTotal: Int
        itemTotal =
            if (getRefreshLoadView() != null && getRefreshLoadView()!!.getCurState() === 8) {
                this.adapter!!.itemCount - 2
            } else {
                this.adapter!!.itemCount - 3
            }
        return (this.layoutManager is LinearLayoutManager
                && (this.layoutManager as LinearLayoutManager?)!!.findLastCompletelyVisibleItemPosition() >= itemTotal)
    }

    fun getListener(): KRecyclerListener? {
        return mListener
    }

    fun setListener(mListener: KRecyclerListener?) {
        this.mListener = mListener
    }

    private fun findOpenItem(): View? {
        val childCount = this.childCount
        for (i in 0 until childCount) {
            val KSwipeItemLayout: KSwipeItemLayout? = findSwipeItemLayout(getChildAt(i))
            if (KSwipeItemLayout != null && KSwipeItemLayout.isOpen) {
                return getChildAt(i)
            }
        }
        return null
    }

    private fun findSwipeItemLayout(view: View): KSwipeItemLayout? {
        if (view is KSwipeItemLayout) {
            return view as KSwipeItemLayout
        } else {
            if (view is ViewGroup) {
                val group = view
                val count = group.childCount
                for (i in 0 until count) {
                    val swipeItemLayout: KSwipeItemLayout? =
                        findSwipeItemLayout(group.getChildAt(i))
                    if (swipeItemLayout != null) {
                        return swipeItemLayout
                    }
                }
            }
        }
        return null
    }

    private fun getRefreshLoadView(): IBaseRefreshLoadView? {
        return if (this.adapter is KRefreshAndLoadMoreAdapter) {
            val lAdapter: KRefreshAndLoadMoreAdapter? =
                this.adapter as KRefreshAndLoadMoreAdapter?

            lAdapter?.mRefreshLoadView
        } else {
            null
        }
    }

    private fun getLoadMoreView(): IBaseLoadMoreView? {
        return if (this.adapter is KRefreshAndLoadMoreAdapter) {
            val lAdapter: KRefreshAndLoadMoreAdapter? =
                this.adapter as KRefreshAndLoadMoreAdapter?
            lAdapter?.mLoadMoreView
        } else {
            null
        }
    }

    /**
     * 判断是否正在处理下拉刷新
     * @return
     */
    private fun handleRefreshLoad(): Boolean {
        return if (getRefreshLoadView() == null) {
            false
        } else {
            val visibleHeight = getRefreshVisibleHeight()
            if (visibleHeight == -1) {
                false
            } else if (getRefreshLoadView()!!.releaseAction(visibleHeight)) {
                if (getRefreshLoadView()?.mOnRefreshListener != null) {
                    getRefreshLoadView()?.mOnRefreshListener?.onRefreshing()
                }
                true
            } else {
                false
            }
        }
    }

    private fun handleLoadMore(): Boolean {
        return if (getLoadMoreView() == null) {
            false
        } else {
            val visibleHeight = getLoadMoreVisibleHeight()
            if (visibleHeight == -1) {
                false
            } else if (getLoadMoreView()!!.releaseAction(visibleHeight)) {
                if (getLoadMoreView()?.mOnLoadMoreListener != null) {
                    getLoadMoreView()?.mOnLoadMoreListener?.onLoading()
                }
                true
            } else {
                false
            }
        }
    }

    private fun getLoadMoreVisibleHeight(): Int {
        if (getLoadMoreView() == null) {
            return -1
        }
        val childCount = this.layoutManager!!.childCount
        if (childCount <= 0) {
            return -1
        }
        val view = this.layoutManager!!.getChildAt(childCount - 1)
        return if (view == null) {
            -1
        } else if (view !== getLoadMoreView()) {
            -1
        } else {
            val layoutHeight = this.layoutManager!!.height
            val top = view.getTop()
            layoutHeight - top
        }
    }

    /**
     * 获取下拉刷新视图的可见高度
     * @return
     */
    private fun getRefreshVisibleHeight(): Int {
        return if (getRefreshLoadView() == null) {
            -1
        } else {
            val childCount = this.layoutManager!!.childCount
            if (childCount <= 0) {
                -1
            } else {
                val view =
                    this.layoutManager?.getChildAt(mRefreshViewPos)
                if (view == null) {
                    -1
                } else if (view != getRefreshLoadView()) {
                    -1
                } else {
                    val top = view.top
                    val bottom = view.bottom
                    return bottom - top
                }
            }
        }
    }

    private fun isScrollStick(): Boolean {
        return if (!isScrolling) {
            false
        } else {
            val theFirstView = getChildAt(0)
            if (theFirstView == null) {
                false
            } else {
                val childViewHolder = getChildViewHolder(theFirstView)
                if (childViewHolder == null) {
                    false
                } else if (childViewHolder !is IStick) {
                    false
                } else {
                    var isInAction = false
                    if (handleRefreshLoad()) {
                        isInAction = true
                    }
                    if (handleLoadMore()) {
                        isInAction = true
                    }
                    if (isInAction) {
                        false
                    } else {
                        stopScroll()
                        val y = theFirstView.y
                        val height = theFirstView.height
                        val isShowAll =
                            Math.abs(y) > (height / 2).toFloat()
                        val offset = if (isShowAll) height.toFloat() + y else y
                        this.smoothScrollBy(0, offset.toInt())
                        true
                    }
                }
            }
        }
    }

    private fun getTouchItem(x: Int, y: Int): View? {
        for (i in 0 until this.childCount) {
            val child = getChildAt(i)
            if (child.visibility == View.VISIBLE) {
                child.getHitRect(mFrame)
                if (mFrame.contains(x, y)) {
                    return child
                }
            }
        }
        return null
    }

    /**
     * 滑动监听器
     */
    private inner class ScrollerListener : OnScrollListener() {
        override fun onScrollStateChanged(
            recyclerView: RecyclerView,
            newState: Int
        ) {
            when (newState) {
                0 -> this@KRecyclerView.isScrolling = false
                1, 2 -> this@KRecyclerView.isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            if (!this@KRecyclerView.mIsTouching) {
                this@KRecyclerView.isScrollStick()
            }
        }
    }
}