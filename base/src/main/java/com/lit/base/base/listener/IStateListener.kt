package com.lit.base.base.listener
/**
 * <状态回调> <功能详细描述>
 *
 * @author linc
 * @version 2020/9/16
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
interface IStateListener {

    /**
     * 重试回调
     */
    fun onRetry()

    /**
     * 加载回调
     */
    fun onLoading()

    /**
     * 无数据回调
     */
    fun onEmpty()
}