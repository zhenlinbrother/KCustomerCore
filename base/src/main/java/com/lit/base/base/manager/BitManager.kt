package com.lit.base.base.manager

import com.lit.base.R

/**
 * <一句话解释功能> <功能详细描述>
 *
 * @author linc
 * @version 2020/9/16
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
class BitManager {

    companion object{
        //默认
        const val NONE = -1
        //不可点击
        const val NO_CLICK = -2

        val instance = BitManager()
    }

    private var isDebug = false

    var loadingViewLayout = R.layout.bit_state_loading_view
    var retryViewLayout = R.layout.bit_state_retry_view
    var emptyViewLayout = R.layout.bit_state_empty_view
    var toolbarLayout = R.layout.bit_common_tool_bar

    var retryBtnId = NONE
    var emptyBtnId = NONE
}