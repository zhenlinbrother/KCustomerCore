package com.lit.kcustomercore.ui.home

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.BaseOnTabSelectedListener
import com.lit.base.base.adapter.CommonPageAdapter
import com.lit.base.mvvm.fragment.BaseFragment
import com.lit.base.mvvm.fragment.BaseLazyFragment
import com.lit.kcustomercore.R
import com.lit.kcustomercore.TestFragment
import com.lit.kcustomercore.ui.home.commend.CommendFragment
import com.lit.kcustomercore.ui.home.discovery.DiscoveryFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.viewPager
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.layout_main_page_title_bar.*

/**
 * author       : linc
 * time         : 2020/9/28
 * desc         : 首页主页面
 * version      : 1.0.0
 */
class HomePageFragment : BaseFragment(){

    val tabTitles = arrayOf("发现", "推荐")
    lateinit var pageAdapter: CommonPageAdapter
    private var fragments : MutableList<BaseFragment> = ArrayList<BaseFragment>()

    override fun getLayoutId(): Int {
        return R.layout.fragment_home
    }

    override fun initView(view: View) {
        ivNetCandle.visibility = View.VISIBLE
        val viewPager = view.findViewById<ViewPager>(R.id.viewPager)

        fragments.add(DiscoveryFragment.newInstance())
        fragments.add(CommendFragment.newInstance())

        pageAdapter = CommonPageAdapter(childFragmentManager, fragments, tabTitles)

        viewPager.offscreenPageLimit = 1
        viewPager.adapter = pageAdapter
        viewPager.currentItem = 0
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

        tabLayout.setupWithViewPager(viewPager)
    }

    companion object {
        fun newInstance() = HomePageFragment()
    }

}