package com.lit.kcustomercore.ui.home.commend

import android.app.Activity
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lit.kcustomercore.R
import com.lit.kcustomercore.bean.HomePageRecommend
import com.lit.kcustomercore.extension.*
import com.lit.kcustomercore.ui.holder.*
import com.lit.kcustomercore.ui.newdetail.NewDetailActivity
import com.lit.kcustomercore.utils.ActionUrlUtil
import com.lit.krecyclerview.BaseViewHolder
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack
import com.shuyu.gsyvideoplayer.video.GSYADVideoPlayer
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import kotlinx.android.synthetic.main.item_auto_play_video_ad.view.*
import kotlinx.android.synthetic.main.item_banner_item_type.view.*
import kotlinx.android.synthetic.main.item_column_card_list_type.view.*
import kotlinx.android.synthetic.main.item_column_card_list_type.view.tvRightText
import kotlinx.android.synthetic.main.item_follow_card_type.view.*
import kotlinx.android.synthetic.main.item_follow_card_type.view.ivAvatar
import kotlinx.android.synthetic.main.item_follow_card_type.view.tvDescription
import kotlinx.android.synthetic.main.item_follow_card_type.view.tvLabel
import kotlinx.android.synthetic.main.item_follow_card_type.view.tvTitle
import kotlinx.android.synthetic.main.item_information_card_type.view.*
import kotlinx.android.synthetic.main.item_information_card_type.view.recyclerView
import kotlinx.android.synthetic.main.item_information_card_type_item.view.*
import kotlinx.android.synthetic.main.item_text_card_type_footer_three.view.*
import kotlinx.android.synthetic.main.item_text_card_type_footer_two.view.*
import kotlinx.android.synthetic.main.item_text_card_type_header_eight.view.*
import kotlinx.android.synthetic.main.item_text_card_type_header_five.view.*
import kotlinx.android.synthetic.main.item_text_card_type_header_seven.view.*
import kotlinx.android.synthetic.main.item_ugc_selected_card_collection_type.view.*

class CommendAdapter(val fragment: CommendFragment, val dataList: List<HomePageRecommend.Item>)
    : RecyclerView.Adapter<BaseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder =
        RecyclerViewHelp.getViewHolder(parent, viewType)

    override fun getItemCount(): Int = dataList.size

    override fun getItemViewType(position: Int): Int = RecyclerViewHelp.getItemViewType(dataList[position])

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val item = dataList[position]

        when(holder){
            is TextCardViewHeader5ViewHolder -> {
                with(holder.itemView){
                    tvTitle5.text = item.data.text
                    if (item.data.actionUrl != null) ivInto5.visible() else ivInto5.gone()
                    if (item.data.follow != null) tvFollow.visible() else tvFollow.gone()

                }
            }

            is TextCardViewHeader7ViewHolder -> {
                with(holder.itemView){
                    tvTitle7.text = item.data.text
                    tvRightText7.text = item.data.rightText

                }
            }

            is TextCardViewHeader8ViewHolder -> {
                with(holder.itemView){
                    tvTitle8.text = item.data.text
                    tvRightText8.text = item.data.rightText

                }
            }

            is TextCardViewFooter2ViewHolder -> {
                with(holder.itemView){
                    tvFooterRightText2.text = item.data.text

                }
            }

            is FollowCardViewHolder -> {
                with(holder.itemView){
                    ivVideo.load(item.data.content.data.cover.feed, 4f)
                    ivAvatar.load(item.data.header.icon)
                    tvVideoDuration.text = item.data.content.data.duration.conversionVideoDuration()
                    tvDescription.text = item.data.header.description
                    tvTitle.text = item.data.header.title
                    if (item.data.content.data.ad) tvLabel.visible() else tvLabel.gone()
                    if (item.data.content.data.library == "DAILY") ivChoiceness.visible() else ivChoiceness.gone()
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

            is BannerViewHolder -> {
                holder.itemView.ivPicture.load(item.data.image, 4f)
            }

            is Banner3ViewHolder -> {
                with(holder.itemView){
                    ivPicture.load(item.data.image, 4f)
                    ivAvatar.load(item.data.header.icon)
                    tvTitle.text = item.data.header.title
                    tvDescription.text = item.data.header.description
                    if (item.data.label?.text.isNullOrEmpty()) tvLabel.invisible() else tvLabel.visible()
                    tvLabel.text = item.data.label?.text ?: ""
                }
            }

            is InformationCardFollowCardViewHolder -> {
                with(holder.itemView){
                    ivCover.load(item.data.backgroundImage, 4f, RoundedCornersTransformation.CornerType.TOP)
                    recyclerView.setHasFixedSize(true)
                    if (recyclerView.itemDecorationCount == 0){
                        recyclerView.addItemDecoration(InformationCardFollowCardItemDecoration())
                    }
                    recyclerView.layoutManager = LinearLayoutManager(context)
                    recyclerView.adapter = InformationCardFollowCardAdapter(fragment.activity, item.data.actionUrl, item.data.titleList)

                }
            }

            is VideoSmallCardViewHolder -> {
                with(holder.itemView){
                    ivPicture.load(item.data.cover.feed, 4f)
                    tvDescription.text = if (item.data.library == "DAILY") "#${item.data.category} / 开眼精选" else "#${item.data.category}"
                    tvTitle.text = item.data.title
                    tvVideoDuration.text = item.data.duration.conversionVideoDuration()

                }
            }

            is UgcSelectedCardCollectionViewHolder -> {
                with(holder.itemView){
                    tvTitle.text = item.data.header.title
                    tvRightText.text = item.data.header.rightText
                    item.data.itemList.forEachIndexed { index, it ->
                        when(index){
                            0 -> {
                                ivCoverLeft.load(it.data.url, 4f, RoundedCornersTransformation.CornerType.LEFT)
                                if (!it.data.urls.isNullOrEmpty() && it.data.urls.size > 1) ivLayersLeft.visible()
                                ivAvatarLeft.load(it.data.userCover)
                                tvNicknameLeft.text = it.data.nickname
                            }
                            1 -> {
                                ivCoverRightTop.load(it.data.url, 4f, RoundedCornersTransformation.CornerType.TOP_RIGHT)
                                if (!it.data.urls.isNullOrEmpty() && it.data.urls.size > 1) ivLayersRightTop.visible()
                                ivAvatarRightTop.load(it.data.userCover)
                                tvNicknameRightTop.text = it.data.nickname
                            }
                            2 -> {
                                ivCoverRightBottom.load(it.data.url, 4f, RoundedCornersTransformation.CornerType.BOTTOM_RIGHT)
                                if (!it.data.urls.isNullOrEmpty() && it.data.urls.size > 1) ivLayersRightBottom.visible()
                                ivAvatarRightBottom.load(it.data.userCover)
                                tvNicknameRightBottom.text = it.data.nickname
                            }
                        }
                    }
                    setOnClickListener { "该功能即将开放，敬请期待".showToast() }
                }
            }

            is TagBriefCardViewHolder -> {
                with(holder.itemView){
                    ivPicture.load(item.data.icon, 4f)
                    tvDescription.text = item.data.description
                    tvTitle.text = item.data.title
                    if (item.data.follow != null) tvFollow.visible() else tvFollow.gone()
                    setOnClickListener { "${item.data.title}, 该功能即将开放，敬请期待".showToast() }
                }
            }

            is TopicBriefCardViewHolder -> {
                with(holder.itemView){
                    ivPicture.load(item.data.icon, 4f)
                    tvDescription.text = item.data.description
                    tvTitle.text = item.data.title
                    setOnClickListener { "${item.data.title}, 该功能即将开放，敬请期待".showToast() }
                }
            }

            is AutoPlayVideoAdViewHolder -> {
                item.data.detail?.run {
                    holder.itemView.ivAvatar.load(item.data.detail.icon)
                    holder.itemView.tvTitle.text = item.data.detail.title
                    holder.itemView.tvDescription.text = item.data.detail.description
//                    startAutoPlay(fragment.activity, holder.itemView.videoPlayer as GSYADVideoPlayer, position, url, imageUrl, TAG, object : GSYSampleCallBack(){
//                        override fun onPrepared(url: String?, vararg objects: Any?) {
//                            super.onPrepared(url, *objects)
//                            GSYVideoManager.instance().isNeedMute = true
//                        }
//
//                        override fun onClickBlank(url: String?, vararg objects: Any?) {
//                            super.onClickBlank(url, *objects)
//                            ActionUrlUtil.process(fragment, item.data.actionUrl)
//                        }
//                    })

                    setOnClickListener(holder.itemView.videoPlayer.thumbImageView, holder.itemView){
                        ActionUrlUtil.process(fragment, item.data.detail.actionUrl)
                    }
                }
            }

            else -> {
                holder.itemView.gone()
            }
        }
    }

    class InformationCardFollowCardAdapter(val activity: Activity, val actionUrl: String?, val dataList: List<String>):
            RecyclerView.Adapter<InformationCardFollowCardAdapter.ViewHolder>(){

        inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {}

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_information_card_type_item, parent, false))
        }

        override fun getItemCount(): Int = dataList.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = dataList[position]
            holder.itemView.tvNews.text = item
        }
    }

    class InformationCardFollowCardItemDecoration: RecyclerView.ItemDecoration(){
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            val position = parent.getChildAdapterPosition(view)
            val count = parent.adapter?.itemCount
            count?.run {
                when(position){
                    0 -> outRect.top = dp2px(18f)
                    count.minus(1) -> {
                        outRect.top = dp2px(13f)
                        outRect.bottom = dp2px(18f)
                    }
                    else -> outRect.top = dp2px(13f)
                }
            }
        }
    }

    companion object {
        const val TAG = "CommendAdapter"

        fun startAutoPlay(activity: Activity,
                          player: GSYADVideoPlayer,
                          position: Int,
                          playUrl: String,
                          coverUrl: String,
                          playTag: String,
                          callback: GSYSampleCallBack? = null){
            player.run {
                //防止错位设置
                setPlayTag(playTag)
                //设置播放位置放置错乱
                setPlayPosition(position)
                //音频焦点冲突时是否释放
                isReleaseWhenLossAudio = false
                //设置循环播放
                isLooping = true
                //增加封面
                val cover = ImageView(activity)
                cover.scaleType = ImageView.ScaleType.CENTER_CROP
                cover.load(coverUrl, 4f)
                cover.parent?.run { removeView(cover) }
                thumbImageView = cover
                //设置播放过程中的回调
                setVideoAllCallBack(callback)
                //设置播放URL
                setUp(playUrl, false, null)
            }
        }
    }
}