package com.lit.base.mvvm.fragment

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.lit.base.base.manager.BitManager
import com.lit.base.base.widget.StateLayout

/**
 * author       : linc
 * time         : 2020/9/27
 * desc         : 基类 Fragment
 * version      : 1.0.0
 */
abstract class BaseFragment : Fragment(){

    protected val TAG: String = this.javaClass.simpleName

    /**
     * 依附的activity
     */
    lateinit var activity: Activity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        //缓存当前依附的activity
        activity = getActivity()!!
        arguments?.let { initArgs(it) }
        Log.d(TAG, "BaseFragment-->onAttach()")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "BaseFragment-->onCreate()")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(TAG, "BaseFragment-->onCreateView")
        val fragmentView = inflater.inflate(getLayoutId(), container, false)
        return fragmentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG, "BaseFragment-->onViewCreated--start")
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        Log.d(TAG, "BaseFragment-->onViewCreated--end")
    }

    /**
     * 初始化参数
     * @param arguments Bundle
     */
    protected fun initArgs(arguments: Bundle){}

    /**
     * 获取布局
     * @return Int
     */
    abstract fun getLayoutId(): Int

    /**
     * 初始化控件
     * @param view View
     */
    abstract fun initView(view: View)

    /**
     * 包装fragment 添加多状态：重试、空、加载
     * @param view View 需要被包装的view
     * @return StateLayout 拥有多种状态的view
     */
    protected fun wrapFragmentView(view: View): StateLayout{
        val stateLayout: StateLayout = StateLayout(getActivity()!!)
        stateLayout.setContentView(view)
        stateLayout.setRetryView(BitManager.instance.retryViewLayout)
        stateLayout.setEmptyView(BitManager.instance.emptyViewLayout)
        stateLayout.setLoadingView(BitManager.instance.loadingViewLayout)
        return stateLayout
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d(TAG, "BaseFragment-->onActivityCreated")
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        Log.d(TAG, "BaseFragment-->onViewStateRestored")
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "BaseFragment-->onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "BaseFragment-->onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "BaseFragment-->onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "BaseFragment-->onStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "BaseFragment-->onDestroyView")
    }
}