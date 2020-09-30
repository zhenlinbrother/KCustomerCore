package com.lit.base.base.adapter

import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

/**
 * author       : linc
 * time         : 2020/9/28
 * desc         : 通用页面适配器
 * version      : 1.0.0
 */
class CommonPageAdapter(
    @NonNull fm: FragmentManager?,
    private val fragments: List<Fragment>
) : FragmentPagerAdapter(fm!!, BEHAVIOR_SET_USER_VISIBLE_HINT) {


    @NonNull
    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int {
        return fragments.size
    }

}
