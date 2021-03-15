package com.lit.kcustomercore.ui.newdetail

import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.lit.kcustomercore.Constant
import com.lit.kcustomercore.R
import com.lit.kcustomercore.bean.VideoReplies
import com.lit.kcustomercore.extension.*
import com.lit.kcustomercore.ui.holder.RecyclerViewHelp
import com.lit.kcustomercore.ui.holder.ReplyViewHolder
import com.lit.kcustomercore.ui.holder.TextCardViewHeader4ViewHolder
import com.lit.kcustomercore.utils.DateUtil
import com.lit.kcustomercore.utils.GlobalUtil
import com.lit.krecyclerview.BaseViewHolder
import kotlinx.android.synthetic.main.item_new_detail_reply_type.view.*
import kotlinx.android.synthetic.main.item_text_card_type_header_four.view.*

class NewDetailReplyAdapter(val activity: NewDetailActivity, val dataList: List<VideoReplies.Item>)
    :    RecyclerView.Adapter<BaseViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when(viewType) {
        Constant.ItemViewType.MAX -> ReplyViewHolder(R.layout.item_new_detail_reply_type.inflate(parent))
        else -> RecyclerViewHelp.getViewHolder(parent, viewType)
    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val item = dataList[position]
        when(holder){
            is TextCardViewHeader4ViewHolder -> {
                with(holder.itemView){
                    tvTitle4.text = item.data.text
                    if (item.data.actionUrl != null && item.data.actionUrl.startsWith(Constant.ActionUrl.REPLIES_HOT)){
                        //热门评论
                        ivInto4.visible()
                        tvTitle4.layoutParams = (tvTitle4.layoutParams as LinearLayout.LayoutParams).apply {
                            setMargins(0, dp2px(30f), 0, dp2px(6f))
                        }
                        setOnClickListener { "该功能即将开放，敬请期待".showToast() }
                    } else {
                        //相关评论、最新评论
                        tvTitle4.layoutParams = (tvTitle4.layoutParams as LinearLayout.LayoutParams).apply {
                            setMargins(0, dp2px(24f), 0, dp2px(6f))
                        }
                        ivInto4.gone()
                    }
                }
            }

            is ReplyViewHolder -> {
                with(holder.itemView){
                    groupReply.gone()
                    ivAvatar.load(item.data.user?.avatar ?: "")
                    tvNickName.text = item.data.user?.nickname
                    tvLikeCount.text = if (item.data.likeCount == 0) "" else item.data.likeCount.toString()
                    tvMessage.text = item.data.message
                    tvTime.text = getTimeReply(item.data.createTime)
                    ivLike.setOnClickListener { "该功能即将开放，敬请期待".showToast() }

                    item.data.parentReply?.run {
                        groupReply.visible()
                        tvReplyUser.text = String.format(GlobalUtil.getString(R.string.reply_target), user?.nickname)
                        ivReplyAvatar.load(user?.avatar ?: "")
                        tvReplyNickName.text = user?.nickname
                        tvReplyMessage.text = message
                        tvShowConversation.setOnClickListener { R.string.currently_not_supported.showToast() }
                    }
                }
            }

            else -> {
                holder.itemView.gone()
            }
        }
    }

    override fun getItemViewType(position: Int) = when {
        dataList[position].type == "reply" && dataList[position].data.dataType == "ReplyBeanForClient" -> Constant.ItemViewType.MAX
        else -> RecyclerViewHelp.getItemViewType(dataList[position])
    }

    private fun getTimeReply(dataMillis: Long): String {
        val currentMillis = System.currentTimeMillis()
        val timePast = currentMillis - dataMillis
        if (timePast > -DateUtil.MINUTE) {  // 采用误差一分钟以内的算法，防止客户端和服务端时间不同步导致的显示问题
            when {
                timePast < DateUtil.DAY -> {
                    var pastHours = timePast / DateUtil.HOUR
                    if (pastHours <= 0){
                        pastHours = 1
                    }
                    return DateUtil.getDate(dataMillis, "HH:mm")
                }
                else -> return DateUtil.getDate(dataMillis, "yyyy/MM/dd")
            }
        } else {
            return DateUtil.getDate(dataMillis, "yyyy/MM/dd HH:mm")
        }
    }
}