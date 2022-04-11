package com.lit.kcustomercore

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lit.base.base.adapter.CommonPageAdapter
import com.lit.base.mvvm.activity.BaseActivity
import com.lit.base.mvvm.activity.BaseListActivity
import com.lit.base.mvvm.fragment.BaseFragment
import com.lit.kcustomercore.ui.home.HomePageFragment
import com.lit.kcustomercore.ui.home.discovery.DiscoveryFragment
import com.lit.krecyclerview.BaseViewHolder
import com.lit.krecyclerview.adapter.KRefreshAndLoadMoreAdapter
import com.next.easynavigation.view.EasyNavigationBar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : BaseActivity() {

    private var fragmentList = listOf<BaseFragment>(HomePageFragment.newInstance())
    private lateinit var adapter: PageAdapter

    private val REQUEST_EXTERNAL_STORAGE = 1
    private val PERMISSION = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

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

        adapter = PageAdapter(supportFragmentManager, fragmentList)
        viewPager.offscreenPageLimit = fragmentList.size
        viewPager.adapter = adapter

        bottomView.setupWithViewPager(viewPager)
        checkAllPermission()
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

    inner class PageAdapter(@NonNull fm: FragmentManager?,
                            private val fragments: List<Fragment>
    ) : FragmentPagerAdapter(fm!!, BEHAVIOR_SET_USER_VISIBLE_HINT){
        @NonNull
        override fun getItem(position: Int): Fragment {
            return fragments[position]
        }

        override fun getCount(): Int {
            return fragments.size
        }
    }

    private val REQUEST_CODE = 0
    private val NO_PERMISSION = arrayListOf<String>()
    private fun checkAllPermission() {
        NO_PERMISSION.clear()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PERMISSION.forEach {
                if (checkSelfPermission(it) != PackageManager.PERMISSION_GRANTED) {
                    NO_PERMISSION.add(it)
                }
            }

            if (NO_PERMISSION.size != 0) {
                requestPermissions(
                    NO_PERMISSION.toTypedArray(),
                    REQUEST_CODE
                )
            }
        }
    }
}
