package com.lit.base.base.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager
/**
 * author       : linc
 * time         : 2020/9/28
 * desc         : 不可滑动的viewPager
 * version      : 1.0.0
 */
class NoScrollViewPager : ViewPager{

    var noScroll: Boolean = true

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        return if (noScroll){
            false
        } else {
            super.onTouchEvent(ev)
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return if (noScroll){
            false
        } else {
            super.onInterceptTouchEvent(ev)
        }
    }

    override fun setCurrentItem(item: Int) {
        super.setCurrentItem(item, false)
    }
}