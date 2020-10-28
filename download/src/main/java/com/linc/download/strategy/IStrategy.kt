package com.linc.download.strategy
/**
 * author       : linc
 * time         : 2020/10/28
 * desc         :
 * version      : 1.0.0
 */
interface IStrategy {

    fun run(): Boolean

    fun stop()
}