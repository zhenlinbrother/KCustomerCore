package com.lit.kcustomercore.extension

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlin.math.min

/**
 * 解析xml布局
 */
fun Int.inflate(parent: ViewGroup, attachToRoot: Boolean = false) : View{
    return LayoutInflater.from(parent.context).inflate(this, parent, attachToRoot)
}

/**
 * 转换时间格式
 * @receiver Int
 * @return String
 */
fun Int.conversionVideoDuration(): String{
    val minute = 1 * 60
    val hour = 60 * minute
    val day = 24 * hour

    return when{
        this < day -> {
            String.format("%02d:%02d", this / minute, this % 60)
        }
        else -> {
            String.format("%02d:%02d:%02d", this / hour, (this % hour) / minute, this % 60)
        }
    }
}