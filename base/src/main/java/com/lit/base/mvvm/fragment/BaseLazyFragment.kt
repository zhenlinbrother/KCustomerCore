package com.lit.base.mvvm.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
/**
 * author       : linc
 * time         : 2020/9/27
 * desc         : 基类 懒加载fragment
 * version      : 1.0.0
 */
abstract class BaseLazyFragment : BaseFragment() {

    /**
     * view是否创建
     */
    protected var isViewCreated: Boolean = false

    /**
     * 当前可见状态
     */
    protected var currentVisibleState: Boolean = false

    /**
     * 是否第一次可见
     */
    protected var isFirstVisible = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        isViewCreated = true
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!isHidden && userVisibleHint){
            //可见状态 进行时间分发
            dispatchUserVisibleHint(true)
        }
    }

    /**
     * 修改fragment的可见性 setUserVisibleHint 被调用有两种情况:
     * 1、在切换tab的时候，会先于所有fragment的其他生命周期，先调用这个函数
     * 2、对于之前已经调用过setUserVisibleHint方法的fragment后，让fragment从可见到不可见状态的变化
     * @param isVisibleToUser
     */
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        Log.d(TAG, "setUserVisibleHint: $isVisibleToUser")
        if (isViewCreated){
            //情况二需分情况考虑：1、可见  2、可见 -> 不可见
            //对于1，首先必须是可见的（isVisibleToUser == true) 而且只有当可见状态进行改变的时候才会切换，
            //否则则进行反复调用，从而导致时间分发的多次更新
            if (isVisibleToUser && !currentVisibleState){
                dispatchUserVisibleHint(true)
            } else {
                dispatchUserVisibleHint(false)
            }
        }
    }

    private fun dispatchUserVisibleHint(isVisible: Boolean){
        Log.d(TAG, "dispatchUserVisibleHint: $isVisible")

        //首先考虑下fragment嵌套fragment的情况
        if (isVisible && isParentInvisible()){
            return
        }

        //如果当前状态与需要设置的状态一致，则不处理
        if (currentVisibleState == isVisible) return

        currentVisibleState = isVisible
        when(isVisible){
            true -> {
                if (isFirstVisible){
                    isFirstVisible = false
                    onFragmentFirstVisible()
                }
                onFragmentResume()
                //分发事件给内嵌的fragment
                dispatchUserVisibleState(true)
            }
            else -> {
                onFragmentPause()
                dispatchUserVisibleState(false)
            }
        }
    }

    /**
     * 在双重ViewPager嵌套的情况下，第一次滑到Fragment嵌套ViewPager(fragment)的场景时候
     * 此时只会加载外层fragment的数据，而不会加载内嵌viewpager中的fragment的数据
     * 因此，我们需要在此增加一个当外层fragment可见的时候，分发可见事件给自己内嵌的所有fragment显示
     * @param visible Boolean
     */
    private fun dispatchUserVisibleState(visible: Boolean){
        Log.d(TAG, "dispatchUserVisibleState: $visible")
        val fragments: List<Fragment> = childFragmentManager.fragments
        fragments.forEach {
            if (it is BaseLazyFragment
                && !it.isHidden
                && it.userVisibleHint){
                it.dispatchUserVisibleHint(true)
            }
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        Log.d(TAG, "onHiddenChanged: $hidden")
        super.onHiddenChanged(hidden)
        //这里的可见返回false
        if (hidden){
            dispatchUserVisibleHint(false)
        } else {
            dispatchUserVisibleHint(true)
        }
    }

    private fun isParentInvisible() : Boolean{
        if (parentFragment is BaseLazyFragment){
            return !currentVisibleState
        }

        return false
    }

    /**
     * 第一次可见，根据业务进行初始化操作
     */
    protected open fun onFragmentFirstVisible(){
        Log.d(TAG, "onFragmentFirstVisible: 第一次可见")
    }

    /**
     * Fragment真正的Resume，开始处理网络加载等耗时操作
     */
    protected open fun onFragmentResume(){
        Log.d(TAG, "onFragmentResume: 真正的Resume 开始进行相关耗时操作")
    }

    /**
     * Fragment真正的Pause，暂停一切网络耗时操作
     */
    protected open fun onFragmentPause(){
        Log.d(TAG, "onFragmentPause: 真正的Pause 结束相关耗时操作")
    }

    override fun onResume() {
        super.onResume()
        if (!isFirstVisible){
            if (!isHidden && !currentVisibleState && userVisibleHint){
                dispatchUserVisibleHint(true)
            }
        }
    }

    /**
     * 只有当当前页面由可见状态转变到不可见状态时才需要调用
     * dispatchUserVisibleHint currentVisibleState && getUserVisibleHint() 能够限定是当前可见的 Fragment
     * 当前fragment 包含 子fragment 的时候 dispatchUserVisibleHint内部本身会通知子fragment不可见 子fragment走到这里时候自身又会调用一趟
     */
    override fun onPause() {
        super.onPause()
        if (currentVisibleState && userVisibleHint){
            dispatchUserVisibleHint(false)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        isViewCreated = false
        Log.d(TAG, "onDestroyView: ")
    }


}