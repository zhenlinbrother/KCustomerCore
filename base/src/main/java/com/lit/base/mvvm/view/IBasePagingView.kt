package com.lit.base.mvvm.view
/**
 * <一句话解释功能> <功能详细描述>
 *
 * @author linc
 * @version 2020/9/17
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
interface IBasePagingView : IBaseView{
    /**
     * 加载更多失败
     */
    fun onLoadMoreFailure(message: String)

    /**
     * 没有更多了
     */
    fun onLoadMoreEmpty()
}