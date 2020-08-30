package com.lit.krecyclerview.adapter
/**
 * author       : liaozhenlin
 * time         : 2020/8/30 0030 13:16
 * desc         : 刷新和下拉更多包装类
 * version      : 1.1.0
 */
class KRefreshAndLoadMoreAdapter {

    interface OnRefreshListener{
        /**
         * 刷新中回调
         */
        fun onRefreshing()
    }

    interface OnLoadMoreListener{

        /**
         * 加载更多数据中回调，加载完成后，可调用一下方法
         */
        fun onLoading()
    }
}