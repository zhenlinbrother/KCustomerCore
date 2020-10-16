package com.lit.kcustomercore.utils

import android.app.Activity
import com.lit.base.mvvm.fragment.BaseFragment
import com.lit.kcustomercore.Constant
import com.lit.kcustomercore.extension.showToast
import java.net.URLDecoder

/**
 * author       : linc
 * time         : 2020/10/10
 * desc         : actionUrl事件处理工具类
 * version      : 1.0.0
 */
object ActionUrlUtil {

    fun process(fragment: BaseFragment, actionUrl: String?, toastTitle: String = ""){
        process(fragment.activity, actionUrl, toastTitle)
    }

    fun process(activity: Activity, actionUrl: String?, toastTitle: String = ""){
        if (actionUrl == null) return
        val decodeUrl = URLDecoder.decode(actionUrl, "UTF-8")
        when {
            decodeUrl.startsWith(Constant.ActionUrl.WEBVIEW) -> {
                "$toastTitle,该功能即将开放,敬请期待".showToast()
            }
            decodeUrl == Constant.ActionUrl.RANKLIST -> {
                "${toastTitle},该功能即将开放,敬请期待".showToast()
            }
            decodeUrl.startsWith(Constant.ActionUrl.TAG) -> {
                "${toastTitle},该功能即将开放,敬请期待".showToast()
            }
            decodeUrl == Constant.ActionUrl.HP_SEL_TAB_TWO_NEWTAB_MINUS_THREE -> {
                //EventBus.getDefault().post(SwitchPagesEvent(DailyFragment::class.java))
            }
            decodeUrl == Constant.ActionUrl.CM_TAGSQUARE_TAB_ZERO -> {
                "${toastTitle},该功能即将开放,敬请期待".showToast()
            }
            decodeUrl == Constant.ActionUrl.CM_TOPIC_SQUARE -> {
                "${toastTitle},该功能即将开放,敬请期待".showToast()
            }
            decodeUrl == Constant.ActionUrl.CM_TOPIC_SQUARE_TAB_ZERO -> {
                "${toastTitle},该功能即将开放,敬请期待".showToast()
            }
            decodeUrl.startsWith(Constant.ActionUrl.COMMON_TITLE) -> {
                "${toastTitle},该功能即将开放,敬请期待".showToast()

            }
            actionUrl == Constant.ActionUrl.HP_NOTIFI_TAB_ZERO -> {

            }
            actionUrl.startsWith(Constant.ActionUrl.TOPIC_DETAIL) -> {
                "${toastTitle},该功能即将开放,敬请期待".showToast()
            }
            actionUrl.startsWith(Constant.ActionUrl.DETAIL) -> {
                "${toastTitle},该功能即将开放,敬请期待".showToast()
            }
            else -> {
                "${toastTitle},该功能即将开放,敬请期待".showToast()
            }
        }
    }

    private fun String.getWebViewInfo(): Array<String> {
        val title = this.split("title=").last().split("&url").first()
        val url = this.split("url=").last()
        return arrayOf(title, url)
    }
}