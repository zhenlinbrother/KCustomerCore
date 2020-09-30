package com.lit.kcustomercore.ui.home

import android.view.View
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.BaseOnTabSelectedListener
import com.lit.base.base.adapter.CommonPageAdapter
import com.lit.base.mvvm.fragment.BaseLazyFragment
import com.lit.kcustomercore.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_main_page_title_bar.*

/**
 * author       : linc
 * time         : 2020/9/28
 * desc         : 首页主页面
 * version      : 1.0.0
 */
class HomePageFragment : BaseLazyFragment(){

    val tabTitles = arrayOf("发现", "推荐", "日常")
    lateinit var pageAdapter: CommonPageAdapter

    init {

    }
    override fun getLayoutId(): Int {
        return R.layout.fragment_home
    }

    override fun initView(view: View) {
        ivNetCandle.visibility = View.VISIBLE

        viewPager.offscreenPageLimit = 1
        viewPager.adapter = pageAdapter
        tabLayout.setupWithViewPager(viewPager)
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.addOnTabSelectedListener(object : BaseOnTabSelectedListener<TabLayout.Tab> {
            override fun onTabReselected(p0: TabLayout.Tab?) {
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {
            }

            override fun onTabSelected(p0: TabLayout.Tab?) {
                viewPager.currentItem = p0!!.position
            }

        })
    }
}