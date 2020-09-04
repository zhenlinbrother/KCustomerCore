package com.lit.krecyclerview.swipe

import android.content.Context
import android.graphics.Rect
import android.nfc.Tag
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.widget.FrameLayout
import androidx.customview.widget.ViewDragHelper
import java.util.jar.Attributes

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
    var mSwipeEnable = true
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
//            if (isOpen && is){
//                return false
//            }
        }
        return super.dispatchTouchEvent(ev)
    }

    /**
     * 更新菜单
     */
    fun updateMenu(){
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


    interface SwipeListener{
        fun onSwipeOpen(var1 : KSwipeItemLayout)

        fun onSwipeClose(var2 : KSwipeItemLayout)
    }

    private inner class DragCallBack : ViewDragHelper.Callback() {
        override fun tryCaptureView(child: View, pointerId: Int): Boolean {
            TODO("Not yet implemented")
        }

    }
}