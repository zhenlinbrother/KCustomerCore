package com.lit.kcustomercore.extension

import android.widget.Toast
import com.lit.kcustomercore.LincApplication
import com.lit.kcustomercore.net.EyepetizerNetwork

/**
 * 弹出Toast提示
 * @receiver CharSequence
 * @param duration Int
 */
fun CharSequence.showToast(duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(LincApplication.context, this, duration).show()
}

fun CharSequence?.toShowToast(duration: Int = Toast.LENGTH_SHORT){
    "$this,该功能即将开放，敬请期待".showToast(duration)
}