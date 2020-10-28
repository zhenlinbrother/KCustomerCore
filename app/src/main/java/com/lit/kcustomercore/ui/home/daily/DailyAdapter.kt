package com.lit.kcustomercore.ui.home.daily

import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eyepetizer.android.logic.model.Daily
import com.lit.kcustomercore.R
import com.lit.kcustomercore.extension.*
import com.lit.kcustomercore.ui.holder.*
import com.lit.kcustomercore.ui.home.commend.CommendAdapter
import com.lit.kcustomercore.ui.newdetail.NewDetailActivity
import com.lit.kcustomercore.utils.ActionUrlUtil
import com.lit.kcustomercore.utils.GlobalUtil
import com.lit.kcustomercore.utils.gone
import com.lit.kcustomercore.utils.visible
import com.lit.krecyclerview.BaseViewHolder
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import kotlinx.android.synthetic.main.item_banner_item_type.view.*
import kotlinx.android.synthetic.main.item_banner_item_type.view.tvLabel
import kotlinx.android.synthetic.main.item_follow_card_type.view.*
import kotlinx.android.synthetic.main.item_information_card_type.view.*
import kotlinx.android.synthetic.main.item_new_detail_custom_header_type.view.*
import kotlinx.android.synthetic.main.item_new_detail_custom_header_type.view.tvDescription
import kotlinx.android.synthetic.main.item_new_detail_custom_header_type.view.tvTitle
import kotlinx.android.synthetic.main.item_new_detail_reply_type.view.*
import kotlinx.android.synthetic.main.item_new_detail_reply_type.view.ivAvatar
import kotlinx.android.synthetic.main.item_text_card_type_footer_three.view.*
import kotlinx.android.synthetic.main.item_text_card_type_footer_two.view.*
import kotlinx.android.synthetic.main.item_text_card_type_header_eight.view.*
import kotlinx.android.synthetic.main.item_text_card_type_header_five.view.*
import kotlinx.android.synthetic.main.item_text_card_type_header_five.view.tvFollow
import kotlinx.android.synthetic.main.item_text_card_type_header_seven.view.*

/**
 * author       : linc
 * time         : 2020/10/16
 * desc         :
 * version      : 1.0.0
 */
class DailyAdapter (val fragment: DailyFragment, val dataList: List<Daily.Item>)
    : RecyclerView.Adapter<BaseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder
            = RecyclerViewHelp.getViewHolder(parent, viewType)

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val item = dataList[position]
        when(holder) {
            is TextCardViewHeader5ViewHolder -> {
                with(holder.itemView){
                    tvTitle5.text = item.data.text
                    if (item.data.actionUrl != null) ivInto5.visible() else ivInto5.gone()
                    if (item.data.follow != null) tvFollow.visible() else tvFollow.gone()
                    setOnClickListener(tvTitle5, ivInto5) {
                        ActionUrlUtil.process(fragment, item.data.actionUrl, item.data.text)
                    }
                }
            }

            is TextCardViewHeader7ViewHolder -> {
                with(holder.itemView){
                    tvTitle7.text = item.data.text
                    tvRightText7.text = item.data.rightText
                    setOnClickListener(tvRightText8, ivInto8) {
                        ActionUrlUtil.process(fragment, item.data.actionUrl, "${item.data.text}, ${item.data.rightText}")
                    }
                }
            }

            is TextCardViewHeader8ViewHolder -> {
                with(holder.itemView){
                    tvTitle8.text = item.data.text
                    tvRightText8.text = item.data.rightText
                    setOnClickListener(tvRightText8, ivInto8) { ActionUrlUtil.process(fragment, item.data.actionUrl, item.data.text) }
                }
            }

            is TextCardViewFooter2ViewHolder -> {
                with(holder.itemView){
                    tvFooterRightText2.text = item.data.text
                    setOnClickListener(tvFooterRightText2, ivTooterInto2) { ActionUrlUtil.process(fragment, item.data.actionUrl, item.data.text) }
                }
            }

            is TextCardViewFooter3ViewHolder -> {
                with(holder.itemView){
                    tvFooterRightText3.text = item.data.text
                    setOnClickListener(tvRefresh, tvFooterRightText3, ivTooterInto3) {
                        if (this == tvRefresh){
                            "${tvRefresh.text},${GlobalUtil.getString(R.string.currently_not_supported)}".showToast()
                        } else if (this == tvFooterRightText3 || this == ivTooterInto3){
                            ActionUrlUtil.process(fragment, item.data.actionUrl, item.data.text)
                        }
                    }
                }
            }

            is Banner3ViewHolder -> {
                with(holder.itemView){
                    ivPicture.load(item.data.image, 4f)
                    ivAvatar.load(item.data.header.icon)
                    tvTitle.text = item.data.header.title
                    tvDescription.text = item.data.header.description
                    if (item.data.label?.text.isNullOrEmpty()) tvLabel.invisible() else tvLabel.visible()
                    tvLabel.text = item.data.label?.text ?: ""
                    setOnClickListener { ActionUrlUtil.process(fragment, item.data.actionUrl, item.data.header.title) }
                }
            }

            is FollowCardViewHolder -> {
                with(holder.itemView){
                    ivVideo.load(item.data.content.data.cover.feed, 4f)
                    ivAvatar.loadCircle(item.data.header.icon)
                    tvVideoDuration.text = item.data.content.data.duration.conversionVideoDuration()
                    tvDescription.text = item.data.header.description
                    tvTitle.text = item.data.header.title
                    if (item.data.content.data.ad) tvLabel.visible() else tvLabel.gone()
                    if (item.data.content.data.library == "Daily") ivChoiceness.visible() else ivChoiceness.gone()
                    setOnClickListener {
                        item.data.content.data.run {
                            if (ad || author == null) {
                                NewDetailActivity.start(fragment.activity, id)
                            } else {
                                NewDetailActivity.start(
                                    fragment.activity, NewDetailActivity.VideoInfo(id, playUrl, title, description, category, library, consumption, cover, author, webUrl)
                                )
                            }
                        }
                    }
                }
            }

            is InformationCardFollowCardViewHolder -> {
                with(holder.itemView){
                    ivCover.load(item.data.backgroundImage, 4f, RoundedCornersTransformation.CornerType.TOP)
                    recyclerView.setHasFixedSize(true)
                    if (recyclerView.itemDecorationCount == 0) {
                        recyclerView.addItemDecoration(CommendAdapter.InformationCardFollowCardItemDecoration())
                    }
                    recyclerView.layoutManager = LinearLayoutManager(context)
                    recyclerView.adapter = CommendAdapter.InformationCardFollowCardAdapter(fragment.activity, item.data.actionUrl, item.data.titleList)
                    setOnClickListener { ActionUrlUtil.process(fragment, item.data.actionUrl) }
                }
            }

            else -> {
                holder.itemView.gone()
            }
        }
    }

    override fun getItemViewType(position: Int): Int = RecyclerViewHelp.getItemViewType(dataList[position])
}