package com.lit.kcustomercore.extension

import com.lit.kcustomercore.LincApplication

/**
 * 根据手机的分辨率将dp转化为px
 * @param dp Float
 * @return Int
 */
fun dp2px(dp: Float): Int{
    val scale = LincApplication.context.resources.displayMetrics.density
    return (dp * scale + 0.5f).toInt()
}

/**
 * 根据手机的分辨率将px转化为dp
 * @param px Float
 * @return Int
 */
fun px2dp(px: Float): Int {
    val scale = LincApplication.context.resources.displayMetrics.density
    return (px / scale + 0.5f).toInt()
}

/**
 * 获取屏幕宽值
 */
val screenWidth
    get() = LincApplication.context.resources.displayMetrics.widthPixels

/**
 * 获取屏幕高度
 */
val screenHeight
    get() = LincApplication.context.resources.displayMetrics.heightPixels

/**
 * 获取屏幕像素：对获取的宽高进行拼接。例：1080X2340。
 */
fun screenPixel(): String {
    LincApplication.context.resources.displayMetrics.run {
        return "${widthPixels}X${heightPixels}"
    }
}