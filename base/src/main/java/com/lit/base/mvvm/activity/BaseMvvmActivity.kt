package com.lit.base.mvvm.activity

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.lit.base.mvvm.view.IBaseView
import com.lit.base.mvvm.viewmodel.IMvvmBaseViewModel
/**
 * <一句话解释功能> <功能详细描述>
 *
 * @author linc
 * @version 2020/9/18
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
abstract class BaseMvvmActivity<V : ViewDataBinding?, VM : IMvvmBaseViewModel<View>> : AppCompatActivity(), IBaseView {

    var binding : V? = null
    private var viewModel : VM? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initIntent()
        initViewModel()
        performDataBinding()
        initData()
    }

    private fun performDataBinding(){
        binding = DataBindingUtil.setContentView<V>(this, getLayoutId())
        this.viewModel = if (viewModel == null) getViewModel() as VM? else viewModel
        if (getBindingVariable() > 0){
            binding?.setVariable(getBindingVariable(), viewModel)
        }
        binding?.executePendingBindings()
    }

    private fun initViewModel(){
        viewModel = getViewModel() as VM?
        viewModel?.attachUi(this as View)
    }

    @LayoutRes
    abstract fun getLayoutId() : Int

    /**
     * 获取viewModel
     */
    abstract fun getViewModel() : IMvvmBaseViewModel<View>?

    /**
     * 获取参数Variable
     */
    abstract fun getBindingVariable() : Int

    abstract fun initData()

    abstract fun initIntent()
}