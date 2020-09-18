package com.lit.krecyclerview.swipe

import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.lit.krecyclerview.R
import kotlinx.android.synthetic.main.l_swipe_wrapper.view.*

/**
 * <包含侧滑的 ViewHolder> <功能详细描述>
 *
 * @author linc
 * @version 2020/9/7
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
abstract class KSwipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    companion object{
        private const val NONE = -1
    }

    private var flLeftMenu : FrameLayout = itemView.findViewById(R.id.fl_left_menu)
    private var flContent : FrameLayout = itemView.findViewById(R.id.fl_content)
    private var flRightMenu : FrameLayout = itemView.findViewById(R.id.fl_right_menu)
    private var swipeItemLayout : KSwipeItemLayout = itemView.findViewById(R.id.swipe_item_layout)

    val leftMenuLayout : Int
        get() = -1
    val rightMenuLayout : Int
        get() = -1

    abstract val contentLayout : Int

    fun initLeftMenuItem(flLeftMenu : FrameLayout){}
    fun initRightMenuItem(flRightMenu : FrameLayout){}
    fun initContentItem(flContent : FrameLayout){}
    abstract fun initItem(var1 : FrameLayout)

    init {
        flLeftMenu.removeAllViews()
        flRightMenu.removeAllViews()
        flContent.removeAllViews()

        if (leftMenuLayout != -1){
            LayoutInflater.from(itemView.context)
                .inflate(leftMenuLayout, flLeftMenu, true)
            initLeftMenuItem(flLeftMenu)
            swipeItemLayout.menus[1] = flLeftMenu
        }
        if (rightMenuLayout != -1){
            LayoutInflater.from(itemView.context)
                .inflate(rightMenuLayout, flRightMenu, true)
            initRightMenuItem(flRightMenu)
            swipeItemLayout.menus[2] = flRightMenu
        }

        LayoutInflater.from(itemView.context)
            .inflate(contentLayout, flContent, true)
        initContentItem(flContent)
        initItem(swipeItemLayout)
    }
}