package com.lit.krecyclerview.swipe

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.nfc.Tag
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.widget.FrameLayout
import androidx.core.view.ViewCompat
import androidx.customview.widget.ViewDragHelper
import java.util.jar.Attributes
import kotlin.math.abs

class KSwipeItemLayout @JvmOverloads constructor(
    context: Context?,
    attributes: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context!!, attributes, defStyleAttr) {

    var TAG : String = this.javaClass.simpleName
    private var mDragHelper : ViewDragHelper
    private var mTouchSlop : Int = ViewConfiguration.get(context).scaledTouchSlop
    private var mVelocity : Int = ViewConfiguration.get(context).scaledMinimumFlingVelocity
    private var mDownX = 0F
    private var mDownY = 0F
    private var mIsDragged = false
    var isSwipeEnable = true
    private val mTouchRect : Rect
    private var mCurrentMenu : View? = null
    var isOpen = false
    val menus : LinkedHashMap<Int?, View?> = LinkedHashMap()
    private var mListeners : MutableList<SwipeListener?>? = null

    val contentView : View
        get() = getChildAt(this.childCount - 1)

    private val isLeftMenu : Boolean
        private get() = mCurrentMenu != null && mCurrentMenu == menus[1]

    private val isRightMenu : Boolean
        private get() = mCurrentMenu != null && mCurrentMenu == menus[2]

    init {
        mDragHelper = ViewDragHelper.create(this, DragCallBack())
        mTouchRect = Rect()
    }


    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        updateMenu()
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.action == 0){//手指按下
            if (isCloseAnimating){
                return false
            }
            if (isOpen && isTouchContent(ev.x.toInt(), ev.y.toInt())){
                close()
                return false
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (!isSwipeEnable){
            return super.onTouchEvent(event)
        } else {
            when(event?.action) {
                0 -> {
                    mIsDragged = false
                    mDownX = event.x
                    mDownY = event.y
                }
                1, 3 -> if (mIsDragged || isOpen) {
                    mDragHelper.processTouchEvent(event)
                    event.action = MotionEvent.ACTION_CANCEL
                    mIsDragged = false
                }
                2 -> {
                    val beforeCheckDrag = mIsDragged
                    checkCanDragged(event)
                    if (mIsDragged){
                        mDragHelper.processTouchEvent(event)
                    }
                    if (!beforeCheckDrag && mIsDragged){
                        val obtain = MotionEvent.obtain(event)
                        obtain.action = 3
                        super.onTouchEvent(event)
                    }
                }
                else -> if (mIsDragged){
                    mDragHelper.processTouchEvent(event!!)
                }
            }

            return mIsDragged || super.onTouchEvent(event) || !this.isClickable && menus.size > 0
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        if (!isSwipeEnable){
            return false
        }

        when(ev?.action){
            MotionEvent.ACTION_DOWN -> {
                mIsDragged = false
                mDownX = ev.x
                mDownY = ev.y
            }
            MotionEvent.ACTION_MOVE -> {
                checkCanDragged(ev)
            }
            MotionEvent.ACTION_CANCEL,
            MotionEvent.ACTION_UP -> {
                if (mIsDragged){
                    mDragHelper.processTouchEvent(ev)
                    mIsDragged = false
                }
            }
            else -> {
                if (mIsDragged){
                    if (ev != null) {
                        mDragHelper.processTouchEvent(ev)
                    }
                }
            }
        }

        return mIsDragged || super.onInterceptTouchEvent(ev)
    }

    /**
     * 判断是否可以拖动
     * @param ev MotionEvent
     */
    @SuppressLint("Recycle")
    private fun checkCanDragged(ev : MotionEvent){
        if (!mIsDragged){
            val dx = ev.x - mDownX
            val dy = ev.y - mDownY
            val isRightDrag =
                dx > mTouchSlop.toFloat() && abs(dx) > abs(dy)
            val isLeftDrag =
                dx < -mTouchSlop.toFloat() && abs(dx) > abs(dy)
            if (isOpen){
                val downX = mDownX.toInt()
                val downY = mDownY.toInt()
                if (isTouchContent(downX, downY)){
                    mIsDragged = true
                } else if (isTouchMenu(downX, downY)){
                    mIsDragged = isLeftMenu && isLeftDrag || isRightMenu && isRightDrag
                }
            } else if (isRightDrag){
                mCurrentMenu = menus[1]
                mIsDragged = mCurrentMenu != null
            } else if (isLeftDrag){
                mCurrentMenu = menus[2]
                mIsDragged = mCurrentMenu != null
            }

            if (mIsDragged){
                val obtain = MotionEvent.obtain(ev)
                obtain.action = 0
                mDragHelper.processTouchEvent(obtain)
                if (parent != null){
                    parent.requestDisallowInterceptTouchEvent(true)
                }
            }
        }
    }

    private fun open(){
        if (mCurrentMenu == null){
            isOpen = false
        } else {
            if (isLeftMenu){
                mDragHelper.smoothSlideViewTo(
                    contentView,
                    mCurrentMenu!!.width,
                    this.paddingTop
                )
            } else if (isRightMenu){
                mDragHelper.smoothSlideViewTo(
                    contentView,
                    -mCurrentMenu!!.width,
                    this.paddingTop
                )
            }

            isOpen = true
            if (mListeners != null){
                val listenerCount = mListeners!!.size
                for (i in listenerCount - 1 downTo 0){
                    mListeners!![i]!!.onSwipeOpen(this)
                }
            }

            this.invalidate()
        }
    }

    /**
     * 关闭菜单
     * 1、如果mCurrentMenu不为空 则滑动到原点 并判断是否有监听器，有则执行onSwipeClose方法
     */
    fun close(){
        if (mCurrentMenu == null){
            isOpen = false
        } else {
            mDragHelper.smoothSlideViewTo(
                contentView,
                this.paddingLeft,
                this.paddingTop
            )

            isOpen = false
            if (mListeners != null){
                val listenCount = mListeners!!.size
                for (i in listenCount - 1 downTo 0){
                    mListeners!![i]!!.onSwipeClose(this)
                }
            }

            this.invalidate()
        }
    }

    /**
     * 更新菜单
     */
    private fun updateMenu(){
        val contentView = contentView
        var contentLeft = contentView?.left
        if (contentLeft == 0){
            val var3: Iterator<*> = menus.values.iterator()

            while (var3.hasNext()){
                val view = var3.next() as View
                view.visibility = View.INVISIBLE
            }
        } else if (mCurrentMenu != null){
            mCurrentMenu?.visibility = View.VISIBLE
        }

    }

    /**
     * 是否触碰菜单
     * @param x Int
     * @param y Int
     * @return Boolean
     */
    private fun isTouchMenu(x : Int, y : Int) : Boolean{
        return if (mCurrentMenu == null){
            false
        } else{
            mCurrentMenu!!.getHitRect(mTouchRect)
            mTouchRect.contains(x, y)
        }
    }

    /**
     * 是否触碰内容
     * @param x Int
     * @param y Int
     * @return Boolean
     */
    private fun isTouchContent(x : Int, y : Int) : Boolean{
        val contentView = contentView
        return run {
            contentView.getHitRect(mTouchRect)
            mTouchRect.contains(x, y)
        }
    }

    private val isOpenAnimating : Boolean
        private get() {
            if (mCurrentMenu != null){
                val contentLeft = this.contentView.left
                var menuWidth = this.mCurrentMenu!!.width
                //if (isOpen && ())
            }

            return false
        }

    private val isCloseAnimating : Boolean
        private get() {
            if (mCurrentMenu != null){
                val contentLeft = this.contentView.left
                if (!isOpen && (isLeftMenu && contentLeft > 0 || isRightMenu && contentLeft < 0)){
                    return true
                }
            }

            return false
        }

    fun addSwipeListener(listener: SwipeListener?){
        if (listener != null){
            if (mListeners == null){
                mListeners = ArrayList()
            }
            mListeners!!.add(listener)
        }
    }

    fun removeSwipeListener(listener: SwipeListener?){
        if (listener != null){
            if (mListeners != null){
                mListeners!!.remove(listener)
            }
        }
    }

    override fun computeScroll() {
        super.computeScroll()
        if (mDragHelper.continueSettling(true)){
            ViewCompat.postInvalidateOnAnimation(this)
        }
    }

    interface SwipeListener{
        fun onSwipeOpen(var1 : KSwipeItemLayout)

        fun onSwipeClose(var2 : KSwipeItemLayout)
    }

    private inner class DragCallBack : ViewDragHelper.Callback() {
        override fun tryCaptureView(child: View, pointerId: Int): Boolean {
            return child == contentView || menus.containsValue(child)
        }

        override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
            if (contentView == child){
                if (isRightMenu){
                    return when {
                        left > 0 -> 0
                        left < -mCurrentMenu!!.width -> -mCurrentMenu!!.width
                        else -> left
                    }
                }

                if (isLeftMenu){
                    return when {
                        left > mCurrentMenu!!.width -> mCurrentMenu!!.width
                        left < 0 -> 0
                        else -> left
                    }
                }
            } else {
                val contentView : View
                var newLeft : Int
                if (isRightMenu){
                    contentView = this@KSwipeItemLayout.contentView
                    newLeft = contentView.left + dx
                    if (newLeft > 0){
                        newLeft = 0
                    } else if (newLeft < -child.width){
                        newLeft = -child.width
                    }
                    contentView.layout(
                        newLeft,
                        contentView.top,
                        newLeft + contentView.width,
                        contentView.bottom
                    )

                    return child.left
                }
            }
            return 0
        }

        override fun onViewPositionChanged(
            changedView: View,
            left: Int,
            top: Int,
            dx: Int,
            dy: Int
        ) {
            super.onViewPositionChanged(changedView, left, top, dx, dy)
            updateMenu()
        }

        override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
            if (isLeftMenu){
                when{
                    xvel > mVelocity.toFloat() -> open()
                    xvel < -mVelocity.toFloat() -> close()
                    contentView.left > mCurrentMenu!!.width /3 * 2 -> open()
                    else -> close()
                }
            } else if (isRightMenu){
                when{
                    xvel < -mVelocity.toFloat() -> open()
                    xvel > mVelocity.toFloat() -> close()
                    contentView.left < -mCurrentMenu!!.width / 3 * 2 -> open()
                    else -> close()
                }
            }
        }
    }
}