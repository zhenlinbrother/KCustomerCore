//package com.linc.lrecyclerview.swipe
//
//import android.content.Context
//import android.graphics.Rect
//import android.util.AttributeSet
//import android.view.MotionEvent
//import android.view.View
//import android.view.ViewConfiguration
//import android.widget.FrameLayout
//import androidx.core.view.ViewCompat
//import androidx.customview.widget.ViewDragHelper
//import java.util.*
//
///**
// * <侧滑 layout> <功能详细描述>
// *
// * @author linc
// * @version 2020/8/11
// * @see [相关类/方法]
// *
// * @since [产品/模块版本]
//</功能详细描述></侧滑> */
//class LSwipeItemLayout @JvmOverloads constructor(
//    context: Context?,
//    attrs: AttributeSet? = null as AttributeSet?,
//    defStyleAttr: Int = 0
//) :
//    FrameLayout(context!!, attrs, defStyleAttr) {
//    var TAG: String = this.javaClass.simpleName
//    private val mDragHelper: ViewDragHelper
//    private val mTouchSlop: Int
//    private val mVelocity: Int
//    private var mDownX = 0f
//    private var mDownY = 0f
//    private var mIsDragged = false
//    var isSwipeEnable: Boolean = true
//    private val mTouchRect: Rect
//    private var mCurrentMenu: View? = null
//    var isOpen = false
//        private set
//    val menus: LinkedHashMap<Int?, View?>
//    private var mListeners: MutableList<SwipeListener?>? =
//        null
//
//    override fun onLayout(
//        changed: Boolean,
//        left: Int,
//        top: Int,
//        right: Int,
//        bottom: Int
//    ) {
//        super.onLayout(changed, left, top, right, bottom)
//        updateMenu()
//    }
//
//    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
//        if (ev.action == 0) {
//            if (isCloseAnimating) {
//                return false
//            }
//            if (isOpen && isTouchContent(ev.x.toInt(), ev.y.toInt())) {
//                close()
//                return false
//            }
//        }
//        return super.dispatchTouchEvent(ev)
//    }
//
//    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
//        return if (!isSwipeEnable) {
//            false
//        } else {
//            val action = ev.action
//            when (action) {
//                0 -> {
//                    mIsDragged = false
//                    mDownX = ev.x
//                    mDownY = ev.y
//                }
//                1, 3 -> if (mIsDragged) {
//                    mDragHelper.processTouchEvent(ev)
//                    mIsDragged = false
//                }
//                2 -> checkCanDragged(ev)
//                else -> if (mIsDragged) {
//                    mDragHelper.processTouchEvent(ev)
//                }
//            }
//            mIsDragged || super.onInterceptTouchEvent(ev)
//        }
//    }
//
//    override fun onTouchEvent(ev: MotionEvent): Boolean {
//        return if (!isSwipeEnable) {
//            super.onTouchEvent(ev)
//        } else {
//            val action = ev.action
//            when (action) {
//                0 -> {
//                    mIsDragged = false
//                    mDownX = ev.x
//                    mDownY = ev.y
//                }
//                1, 3 -> if (mIsDragged || isOpen) {
//                    mDragHelper.processTouchEvent(ev)
//                    ev.action = 3
//                    mIsDragged = false
//                }
//                2 -> {
//                    val beforeCheckDrag = mIsDragged
//                    checkCanDragged(ev)
//                    if (mIsDragged) {
//                        mDragHelper.processTouchEvent(ev)
//                    }
//                    if (!beforeCheckDrag && mIsDragged) {
//                        val obtain = MotionEvent.obtain(ev)
//                        obtain.action = 3
//                        super.onTouchEvent(obtain)
//                    }
//                }
//                else -> if (mIsDragged) {
//                    mDragHelper.processTouchEvent(ev)
//                }
//            }
//            mIsDragged || super.onTouchEvent(ev) || !this.isClickable && menus.size > 0
//        }
//    }
//
//    private fun checkCanDragged(ev: MotionEvent) {
//        if (!mIsDragged) {
//            val dx = ev.x - mDownX
//            val dy = ev.y - mDownY
//            val isRightDrag =
//                dx > mTouchSlop.toFloat() && Math.abs(dx) > Math.abs(
//                    dy
//                )
//            val isLeftDrag =
//                dx < -mTouchSlop as Float && Int(dx) > Math.abs(
//                    dy
//                )
//            if (isOpen) {
//                val downX = mDownX.toInt()
//                val downY = mDownY.toInt()
//                if (isTouchContent(downX, downY)) {
//                    mIsDragged = true
//                } else if (isTouchMenu(downX, downY)) {
//                    mIsDragged =
//                        isLeftMenu && isLeftDrag || isRightMenu && isRightDrag
//                }
//            } else if (isRightDrag) {
//                mCurrentMenu = menus[1]
//                mIsDragged = mCurrentMenu != null
//            } else if (isLeftDrag) {
//                mCurrentMenu = menus[2]
//                mIsDragged = mCurrentMenu != null
//            }
//            if (mIsDragged) {
//                val obtain = MotionEvent.obtain(ev)
//                obtain.action = 0
//                mDragHelper.processTouchEvent(obtain)
//                if (this.parent != null) {
//                    this.parent.requestDisallowInterceptTouchEvent(true)
//                }
//            }
//        }
//    }
//
//    val contentView: View
//        get() = getChildAt(this.childCount - 1)
//
//    private val isLeftMenu: Boolean
//        private get() = mCurrentMenu != null && mCurrentMenu === menus[1]
//
//    private val isRightMenu: Boolean
//        private get() = mCurrentMenu != null && mCurrentMenu === menus[2]
//
//    fun isTouchMenu(x: Int, y: Int): Boolean {
//        return if (mCurrentMenu == null) {
//            false
//        } else {
//            mCurrentMenu!!.getHitRect(mTouchRect)
//            mTouchRect.contains(x, y)
//        }
//    }
//
//    fun isTouchContent(x: Int, y: Int): Boolean {
//        val contentView = contentView
//        return if (contentView == null) {
//            false
//        } else {
//            contentView.getHitRect(mTouchRect)
//            mTouchRect.contains(x, y)
//        }
//    }
//
//    fun close() {
//        if (mCurrentMenu == null) {
//            isOpen = false
//        } else {
//            mDragHelper.smoothSlideViewTo(
//                contentView,
//                this.paddingLeft,
//                this.paddingTop
//            )
//            isOpen = false
//            if (mListeners != null) {
//                val listenerCount = mListeners!!.size
//                for (i in listenerCount - 1 downTo 0) {
//                    mListeners!![i]!!.onSwipeClose(
//                        this
//                    )
//                }
//            }
//            this.invalidate()
//        }
//    }
//
//    fun open() {
//        if (mCurrentMenu == null) {
//            isOpen = false
//        } else {
//            if (isLeftMenu) {
//                mDragHelper.smoothSlideViewTo(
//                    contentView,
//                    mCurrentMenu!!.width,
//                    this.paddingTop
//                )
//            } else if (isRightMenu) {
//                mDragHelper.smoothSlideViewTo(
//                    contentView,
//                    -mCurrentMenu!!.width,
//                    this.paddingTop
//                )
//            }
//            isOpen = true
//            if (mListeners != null) {
//                val listenerCount = mListeners!!.size
//                for (i in listenerCount - 1 downTo 0) {
//                    mListeners!![i]!!.onSwipeOpen(
//                        this
//                    )
//                }
//            }
//            this.invalidate()
//        }
//    }
//
//    private val isOpenAnimating: Boolean
//        private get() {
//            if (mCurrentMenu != null) {
//                val contentLeft = contentView.left
//                val menuWidth = mCurrentMenu!!.width
//                if (isOpen && (isLeftMenu && contentLeft < menuWidth || isRightMenu && -contentLeft < menuWidth)) {
//                    return true
//                }
//            }
//            return false
//        }
//
//    private val isCloseAnimating: Boolean
//        private get() {
//            if (mCurrentMenu != null) {
//                val contentLeft = contentView.left
//                if (!isOpen && (isLeftMenu && contentLeft > 0 || isRightMenu && contentLeft < 0)) {
//                    return true
//                }
//            }
//            return false
//        }
//
//    private fun updateMenu() {
//        val contentView = contentView
//        if (contentView != null) {
//            val contentLeft = contentView.left
//            if (contentLeft == 0) {
//                val var3: Iterator<*> = menus.values.iterator()
//                while (var3.hasNext()) {
//                    val view = var3.next() as View
//                    view.visibility = View.INVISIBLE
//                }
//            } else if (mCurrentMenu != null) {
//                mCurrentMenu!!.visibility = View.VISIBLE
//            }
//        }
//    }
//
//    fun addSwipeListener(listener: SwipeListener?) {
//        if (listener != null) {
//            if (mListeners == null) {
//                mListeners = ArrayList<Any?>()
//            }
//            mListeners!!.add(listener)
//        }
//    }
//
//    fun removeSwipeListener(listener: SwipeListener?) {
//        if (listener != null) {
//            if (mListeners != null) {
//                mListeners!!.remove(listener)
//            }
//        }
//    }
//
//    override fun computeScroll() {
//        super.computeScroll()
//        if (mDragHelper.continueSettling(true)) {
//            ViewCompat.postInvalidateOnAnimation(this)
//        }
//    }
//
//    interface SwipeListener {
//        fun onSwipeOpen(var1: LSwipeItemLayout?)
//        fun onSwipeClose(var1: LSwipeItemLayout?)
//    }
//
//    private inner class DragCallBack : ViewDragHelper.Callback() {
//        override fun tryCaptureView(
//            child: View,
//            pointerId: Int
//        ): Boolean {
//            return child === contentView || menus.containsValue(
//                child
//            )
//        }
//
//        override fun clampViewPositionHorizontal(
//            child: View,
//            left: Int,
//            dx: Int
//        ): Int {
//            if (child === contentView) {
//                if (isRightMenu) {
//                    return if (left > 0) 0 else if (left < -mCurrentMenu!!.width) -mCurrentMenu!!.width else left
//                }
//                if (isLeftMenu) {
//                    return if (left > mCurrentMenu!!.width) mCurrentMenu!!.width else if (left < 0) 0 else left
//                }
//            } else {
//                val contentView: View
//                var newLeft: Int
//                if (isRightMenu) {
//                    contentView = this@LSwipeItemLayout.contentView
//                    newLeft = contentView.left + dx
//                    if (newLeft > 0) {
//                        newLeft = 0
//                    } else if (newLeft < -child.width) {
//                        newLeft = -child.width
//                    }
//                    contentView.layout(
//                        newLeft,
//                        contentView.top,
//                        newLeft + contentView.width,
//                        contentView.bottom
//                    )
//                    return child.left
//                }
//                if (isLeftMenu) {
//                    contentView = this@LSwipeItemLayout.contentView
//                    newLeft = contentView.left + dx
//                    if (newLeft < 0) {
//                        newLeft = 0
//                    } else if (newLeft > child.width) {
//                        newLeft = child.width
//                    }
//                    contentView.layout(
//                        newLeft,
//                        contentView.top,
//                        newLeft + contentView.width,
//                        contentView.bottom
//                    )
//                    return child.left
//                }
//            }
//            return 0
//        }
//
//        override fun onViewPositionChanged(
//            changedView: View,
//            left: Int,
//            top: Int,
//            dx: Int,
//            dy: Int
//        ) {
//            super.onViewPositionChanged(changedView, left, top, dx, dy)
//            updateMenu()
//        }
//
//        override fun onViewReleased(
//            releasedChild: View,
//            xVel: Float,
//            yVel: Float
//        ) {
//            if (isLeftMenu) {
//                if (xVel > mVelocity.toFloat()) {
//                    open()
//                } else if (xVel < -mVelocity as Float) {
//                    close()
//                } else if (contentView
//                        .left > mCurrentMenu!!.width / 3 * 2
//                ) {
//                    open()
//                } else {
//                    close()
//                }
//            } else if (isRightMenu) {
//                if (xVel < (-mVelocity).toFloat()) {
//                    open()
//                } else if (xVel > mVelocity.toFloat()) {
//                    close()
//                } else if (contentView
//                        .left < -mCurrentMenu!!.width / 3 * 2
//                ) {
//                    open()
//                } else {
//                    close()
//                }
//            }
//        }
//    }
//
//    init {
//        menus = LinkedHashMap<Any?, Any?>()
//        mTouchSlop = ViewConfiguration.get(context).scaledTouchSlop
//        mVelocity = ViewConfiguration.get(context).scaledMinimumFlingVelocity
//        mDragHelper = ViewDragHelper.create(
//            this,
//            DragCallBack()
//        )
//        mTouchRect = Rect()
//    }
//}