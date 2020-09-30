package com.lit.base.mvvm.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.gyf.immersionbar.ImmersionBar
import com.lit.base.R
import com.lit.base.mvvm.utils.ActivityCollector
import java.lang.ref.WeakReference

/**
 * author       : linc
 * time         : 2020/9/25
 * desc         : 基类Activity
 * version      : 1.0.0
 */
abstract class BaseActivity : AppCompatActivity(){

    protected val TAG: String = this.javaClass.simpleName

    /**
     * 判断当前activity是否在前台
     */
    protected var isActive: Boolean = false

    /**
     * 当前activity的实例
     */
    protected var activity: Activity? = null

    /**
     * 当前activity的弱引用，防止内存泄漏
     */
    private var activityWR: WeakReference<Activity>? = null

    abstract fun getLayoutId() : Int
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "BaseActivity --> onCreate()")
        setContentView(getLayoutId())
        activity = this
        activityWR = WeakReference(activity!!)
        ActivityCollector.pushTask(activityWR)
        setStatusBarBackground(R.color.common_white)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d(TAG, "BaseActivity-->onSaveInstanceState()")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        Log.d(TAG, "BaseActivity-->onRestoreInstanceState()")
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Log.d(TAG, "BaseActivity-->onNewIntent()")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "BaseActivity-->onRestart()")
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "BaseActivity-->onStart()")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "BaseActivity-->onResume()")
        isActive = true
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "BaseActivity-->onPause()")
        isActive = false
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "BaseActivity-->onStop()")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "BaseActivity-->onDestroy()")
    }

    open fun setStatusBarBackground(@ColorRes statusBarColor: Int) {
        ImmersionBar.with(this)
            .autoStatusBarDarkModeEnable(true, 0.2f)
            .statusBarColor(statusBarColor)
            .fitsSystemWindows(true)
            .init()
    }
}
