package com.lit.krecyclerview.config
/**
 * author       : liaozhenlin
 * time         : 2020/8/30 0030 11:37
 * desc         :
 * version      : 1.1.0
 */
class KRecyclerManager{
    companion object{
        private val INSTANCE = KRecyclerManager()
        val instance : KRecyclerManager
            get() = INSTANCE
    }

    var mIsDebug = false
        private set


}