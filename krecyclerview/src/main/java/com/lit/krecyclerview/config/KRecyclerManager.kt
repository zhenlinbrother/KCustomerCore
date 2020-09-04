package com.lit.krecyclerview.config

import com.lit.krecyclerview.loadView.base.IBaseLoadMoreView
import com.lit.krecyclerview.loadView.base.IBaseRefreshLoadView

/**
 * author       : liaozhenlin
 * time         : 2020/8/30 0030 11:37
 * desc         :
 * version      : 1.1.0
 */
class KRecyclerManager{

    var mIsDebug = false
        private set

    var mRefreshLoadView : IBaseRefreshLoadView? = null
        private set
    var mLoadMoreView : IBaseLoadMoreView? = null
        private set

    companion object{
        private val INSTANCE = KRecyclerManager()
        val instance : KRecyclerManager
            get() = INSTANCE
    }

}