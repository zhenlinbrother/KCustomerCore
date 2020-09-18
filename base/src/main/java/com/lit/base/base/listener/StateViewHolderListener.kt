package com.lit.base.base.listener

import com.lit.base.base.adapter.BitFrameAdapter

/**
 * <状态页视图回调> <功能详细描述>
 *
 * @author linc
 * @version 2020/9/16
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
interface StateViewHolderListener {

    fun handleEmptyViewHolder(holder: BitFrameAdapter.EmptyViewHolder)

    fun handleRetryViewHolder(holder: BitFrameAdapter.RetryViewHolder)

    fun handleLoadingViewHolder(holder: BitFrameAdapter.LoadingViewHolder)
}