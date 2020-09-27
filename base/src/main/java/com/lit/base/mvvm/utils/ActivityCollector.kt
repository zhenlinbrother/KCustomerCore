package com.lit.base.mvvm.utils

import android.app.Activity
import java.lang.ref.WeakReference
import java.util.*

/**
 * author       : linc
 * time         : 2020/9/25
 * desc         : 管理应用程序中所有activity
 * version      : 1.0.0
 */
object ActivityCollector {

    private val activitys = Stack<WeakReference<Activity>>()

    /**
     * 将activity压入application栈
     * @param task WeakReference<Activity>?
     */
    fun pushTask(task: WeakReference<Activity>?) {
        activitys.push(task)
    }

    /**
     * 将传入的activity从栈中移除
     * @param task WeakReference<Activity>?
     */
    fun removeTask(task: WeakReference<Activity>?){
        activitys.remove(task)
    }

    /**
     * 根据指定位置从栈中移除activity
     * @param index Int
     */
    fun removeTask(index: Int) {
        if (activitys.size > index) activitys.removeAt(index)
    }

    /**
     * 将栈中Activity移除至栈顶
     */
    fun removeToTop(){
        val end = activitys.size
        val start = 1
        for (i in end - 1 downTo  start){
            val mActivity = activitys[i].get()
            if (mActivity != null && !mActivity.isFinishing){
                mActivity.finish()
            }
        }
    }

    /**
     * 移除全部
     */
    fun removeAll() {
        for (task in activitys){
            val mActivity = task.get()
            if (mActivity != null && !mActivity.isFinishing){
                mActivity.finish()
            }
        }
    }
}