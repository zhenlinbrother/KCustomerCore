package com.lit.kcustomercore.extension

import android.view.View

/**
 * 显示view
 * @receiver View?
 */
fun View?.visible(){
    this?.visibility = View.VISIBLE
}

/**
 * 隐藏view
 * @receiver View?
 */
fun View?.gone(){
    this?.visibility = View.GONE
}

/**
 * 占位隐藏view
 */
fun View?.invisible() {
    this?.visibility = View.INVISIBLE
}

/**
 * 批量设置控件点击事件
 * @param v Array<out View?>
 * @param block [@kotlin.ExtensionFunctionType] Function1<View, Unit>
 */
fun setOnClickListener(vararg  v: View?, block: View.() -> Unit){
    val listener = View.OnClickListener { it.block() }
    v.forEach { it?.setOnClickListener(listener) }
}