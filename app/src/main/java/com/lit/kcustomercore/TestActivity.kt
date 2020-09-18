package com.lit.kcustomercore

import android.os.Bundle
import android.view.View
import com.gyf.immersionbar.ImmersionBar
import com.lit.base.mvvm.activity.BaseMvvmActivity
import com.lit.base.mvvm.viewmodel.IMvvmBaseViewModel
import com.lit.kcustomercore.databinding.ActivityTestBinding
import kotlinx.android.synthetic.main.activity_test.*

class TestActivity : BaseMvvmActivity<ActivityTestBinding, IMvvmBaseViewModel<View>>() {
    override fun getLayoutId(): Int {
        return R.layout.activity_test
    }

    override fun getViewModel(): IMvvmBaseViewModel<View>? {
        return null
    }

    override fun getBindingVariable(): Int {
        return 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ImmersionBar.with(this)
            .statusBarColor(R.color.main_color_bar)
            .navigationBarColor(R.color.main_color_bar)
            .fitsSystemWindows(true)
            .autoDarkModeEnable(true)
            .init()
    }

    override fun initData() {

    }

    override fun initIntent() {

    }

    override fun showContent() {

    }

    override fun showLoading() {

    }

    override fun showEmpty() {

    }

    override fun showFailure(message: String) {

    }

}