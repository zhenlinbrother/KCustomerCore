package com.lit.base.mvvm.viewmodel
/**
 * <定义 ViewModel 与 V 的关联> <功能详细描述>
 *
 * @author linc
 * @version 2020/9/17
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
interface IMvvmBaseViewModel<V> {
    /**
     * 关联view
     */
    fun attachUi(view: V)

    /**
     * 获取view
     */
    fun getPageView() : V

    /**
     * 是否已关联view
     */
    fun isUiAttach() : Boolean

    /**
     * 解除关联
     */
    fun detachUi()
}