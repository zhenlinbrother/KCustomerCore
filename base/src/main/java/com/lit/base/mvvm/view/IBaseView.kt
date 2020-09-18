package com.lit.base.mvvm.view
/**
 * <界面UI显示切换> <功能详细描述>
 *
 * @author linc
 * @version 2020/9/17
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
interface IBaseView {
    /**
     * 显示内容
     */
    fun showContent()

    /**
     * 显示加载提示
     */
    fun showLoading()

    /**
     * 显示空页面
     */
    fun showEmpty()

    /**
     * 刷新失败
     */
    fun showFailure(message: String)
}