package com.lit.kcustomercore

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lit.base.base.adapter.CommonPageAdapter
import com.lit.base.mvvm.activity.BaseActivity
import com.lit.base.mvvm.activity.BaseListActivity
import com.lit.base.mvvm.fragment.BaseFragment
import com.lit.kcustomercore.ui.home.discovery.DiscoveryFragment
import com.lit.krecyclerview.BaseViewHolder
import com.lit.krecyclerview.adapter.KRefreshAndLoadMoreAdapter
import com.next.easynavigation.view.EasyNavigationBar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : BaseActivity() {

    private var fragmentList = listOf<BaseFragment>(DiscoveryFragment.newInstance(), TestFragment.newInstance())
    private lateinit var adapter: CommonPageAdapter

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bottomView.defaultSetting()
            .fragmentList(fragmentList)
            .titleItems(tabText)
            .selectIconItems(selectedIcons)
            .normalIconItems(unselectedIcons)
            .mode(EasyNavigationBar.NavigationMode.MODE_ADD)
            .selectTextColor(ContextCompat.getColor(this, R.color.selected_color))
            .navigationBackground(ContextCompat.getColor(this, R.color.common_white))
            .centerImageRes(R.drawable.ic_wu)
            .centerIconSize(36f)
            .build()

        adapter = CommonPageAdapter(supportFragmentManager, fragmentList)
        viewPager.offscreenPageLimit = 1
        viewPager.adapter = adapter

        bottomView.setupWithViewPager(viewPager)

    }

    companion object{
        val tabText = arrayOf("首页", "社区", "通知", "我的")
        val selectedIcons = intArrayOf(R.drawable.ic_home_selected,
            R.drawable.ic_social_selected,
            R.drawable.ic_notification_selected,
            R.drawable.ic_mine_selected)
        val unselectedIcons = intArrayOf(R.drawable.ic_home_unselected,
            R.drawable.ic_social_unselected,
            R.drawable.ic_notification_unselected,
            R.drawable.ic_mine_unselected)
    }
}
