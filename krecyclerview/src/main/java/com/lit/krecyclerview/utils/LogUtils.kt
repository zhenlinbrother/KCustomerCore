package com.lit.krecyclerview.utils

import android.util.Log
import com.lit.krecyclerview.config.KRecyclerManager

/**
 * author       : liaozhenlin
 * time         : 2020/8/30 0030 11:51
 * desc         : 打印日志工具
 * version      : 1.1.0
 */
object LogUtils {
    private const val TAG = "JRecycleView"

    fun i(tag : String, msg : String?){
        if (KRecyclerManager.instance.mIsDebug){
            Log.i(tag, msg)
        }
    }

    fun d(tag: String, msg: String?){
        if (KRecyclerManager.instance.mIsDebug){
            Log.d(tag, msg)
        }
    }

    fun w (tag: String, msg: String?){
        if (KRecyclerManager.instance.mIsDebug){
            Log.w(tag, msg)
        }
    }

    fun v(tag: String, msg: String?){
        if (KRecyclerManager.instance.mIsDebug){
            Log.v(tag, msg)
        }
    }

    fun e(tag: String, msg: String?){
        if (KRecyclerManager.instance.mIsDebug){
            Log.e(tag, msg)
        }
    }

    fun i(msg: String?) {
        Log.i(TAG, msg)
    }

    fun d(msg: String?) {
        Log.d(TAG, msg)
    }

    fun w(msg: String?) {
        Log.w(TAG, msg)
    }

    fun v(msg: String?) {
        Log.v(TAG, msg)
    }

    fun e(msg: String?) {
        Log.i(TAG, msg)
    }
}