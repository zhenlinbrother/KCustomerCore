package com.lit.kcustomercore.extension

import android.content.Context
import android.content.SharedPreferences
import com.lit.kcustomercore.LincApplication
import com.lit.kcustomercore.utils.GlobalUtil

/**
 * 获取SharedPreferences实例。
 */
val sharedPreferences: SharedPreferences = LincApplication.context.getSharedPreferences(GlobalUtil.appPackage + "_preferences", Context.MODE_PRIVATE)