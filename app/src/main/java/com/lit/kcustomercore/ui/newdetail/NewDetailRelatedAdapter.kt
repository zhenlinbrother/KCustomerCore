package com.lit.kcustomercore.ui.newdetail

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.linc.download.constant.DownloadConstant
import com.linc.download.jerry.JerryDownload
import com.linc.download.listener.DownloadListener
import com.linc.download.model.CurStatus
import com.linc.download.model.DownloadInfo
import com.lit.kcustomercore.Constant
import com.lit.kcustomercore.R
import com.lit.kcustomercore.bean.VideoRelated
import com.lit.kcustomercore.extension.*
import com.lit.kcustomercore.ui.holder.CustomeHeaderViewHolder
import com.lit.kcustomercore.ui.holder.RecyclerViewHelp
import com.lit.kcustomercore.ui.holder.TextCardViewHeader4ViewHolder
import com.lit.kcustomercore.ui.holder.VideoSmallCardViewHolder
import com.lit.krecyclerview.BaseViewHolder
import kotlinx.android.synthetic.main.item_banner_item_type.view.*
import kotlinx.android.synthetic.main.item_follow_card_type.view.*
import kotlinx.android.synthetic.main.item_new_detail_custom_header_type.view.*
import kotlinx.android.synthetic.main.item_new_detail_custom_header_type.view.ivAvatar
import kotlinx.android.synthetic.main.item_new_detail_custom_header_type.view.ivShare
import kotlinx.android.synthetic.main.item_new_detail_custom_header_type.view.tvDescription
import kotlinx.android.synthetic.main.item_new_detail_custom_header_type.view.tvTitle
import kotlinx.android.synthetic.main.item_text_card_type_header_four.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class NewDetailRelatedAdapter(private val activity: NewDetailActivity,
                              val dataList: List<VideoRelated.Item>,
                              private var videoInfoData: NewDetailActivity.VideoInfo?)
    : RecyclerView.Adapter<BaseViewHolder>() {

    private var mDownloadInfo: DownloadInfo? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when(viewType){
        Constant.ItemViewType.CUSTOM_HEADER -> CustomeHeaderViewHolder(R.layout.item_new_detail_custom_header_type.inflate(parent))
        Constant.ItemViewType.MAX -> SimpleHotReplyCardViewHolder(View(parent.context))
        else -> RecyclerViewHelp.getViewHolder(parent, viewType)
    }

    override fun getItemCount(): Int = dataList.size + 1

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        when(holder){
            is CustomeHeaderViewHolder -> {
                videoInfoData?.let {
                    holder.run {
                        itemView.groupAuthor.gone()
                        itemView.tvTitle.text = videoInfoData?.title
                        itemView.tvCategory.text =
                            if (videoInfoData?.library == "DAILY") "#${videoInfoData?.category} / 开眼精选" else "#${videoInfoData?.category}"
                        itemView.tvDescription.text = videoInfoData?.description
                        itemView.tvCollectionCount.text = videoInfoData?.consumption?.collectionCount.toString()
                        itemView.tvShareCount.text = videoInfoData?.consumption?.shareCount.toString()
                        videoInfoData?.author?.run {
                            itemView.groupAuthor.visible()
                            itemView.ivAvatar.load(videoInfoData?.author?.icon ?: "")
                            itemView.tvAuthorDescription.text = videoInfoData?.author?.description
                            itemView.tvAuthorName.text = videoInfoData?.author?.name
                        }
                        setOnClickListener(itemView.ivCollectionCount, itemView.tvCollectionCount, itemView.ivShare, itemView.tvShareCount, itemView.ivCache, itemView.tvCache, itemView.ivFavorites, itemView.tvFavorites, itemView.tvFollow){
                            when(this){
                                tvCache, ivCache -> {
                                    val downloadInfo = JerryDownload.instance!!.download(
                                        videoInfoData!!.videoId,
                                        videoInfoData!!.playUrl,
                                        videoInfoData!!.title,
                                        DownloadConstant.DEFAULT_USER_ID,
                                        DownloadConstant.DEFAULT_DOMAIN,
                                        videoInfoData!!.cover.blurred
                                    )

                                    mDownloadInfo = downloadInfo

                                    setDownloadListener(itemView.tvCache)
                                }
                                else -> "该功能即将开放，敬请期待".showToast()
                            }
                        }


                        if (mDownloadInfo != null) {
                            if (mDownloadInfo!!.isCurStatusContains(CurStatus.ERROR)
                                || mDownloadInfo!!.isCurStatusContains(CurStatus.TIP)
                                || mDownloadInfo!!.isCurStatusContains(CurStatus.PAUSE)) {
                                itemView.tvCache.text = "缓存"
                                return
                            }

                            if (mDownloadInfo!!.isCurStatusContains(CurStatus.FINISH)) {
                                itemView.tvCache.text = "已缓存"
                                return
                            }

                            if (mDownloadInfo!!.isCurStatusContains(CurStatus.DOWNLOADING)) {
                                itemView.tvCache.text = "${mDownloadInfo!!.percent}%"
                                return
                            }
                        }

                    }
                }
            }

            is SimpleHotReplyCardViewHolder -> {
                //不做任何处理, 忽略此ViewHolder
            }

            is TextCardViewHeader4ViewHolder -> {
                val item = dataList[position - 1]
                holder.itemView.tvTitle4.text = item.data.text
            }

            is VideoSmallCardViewHolder -> {
                val item = dataList[position - 1]
                with(holder.itemView){
                    ivPicture.load(item.data.cover.feed, 4f)
                    tvDescription.text = if (item.data.library == "DAILY") "#${item.data.category} / 开眼精选" else "#${item.data.category}"
                    tvDescription.setTextColor(ContextCompat.getColor(activity, R.color.whiteAlpha35))
                    tvTitle.text = item.data.title
                    tvTitle.setTextColor(ContextCompat.getColor(activity, R.color.white))
                    tvVideoDuration.text = item.data.duration.conversionVideoDuration()

                }
            }

            else -> {
                holder.itemView.gone()
            }
        }
    }

    override fun getItemViewType(position: Int) = when {
        position == 0 -> Constant.ItemViewType.CUSTOM_HEADER

        dataList[position - 1].type == "simpleHotReplyScrollCard"
                && dataList[position - 1].data.dataType == "ItemCollection" -> Constant.ItemViewType.MAX

        else -> RecyclerViewHelp.getItemViewType(dataList[position - 1])
    }

    inner class SimpleHotReplyCardViewHolder(view: View) : BaseViewHolder(view)

    fun bindVideoInfo(videoInfoData: NewDetailActivity.VideoInfo?){
        this.videoInfoData = videoInfoData
    }

    fun bindDownloadInfo(downloadInfo: DownloadInfo?) {
        this.mDownloadInfo = downloadInfo
    }

    private fun setDownloadListener(view: TextView) {
        mDownloadInfo?.listener = object : DownloadListener {
            override fun onPause() {
                view.text = "继续"
            }

            override fun onWaiting() {

            }

            override fun onInit() {

            }

            override fun onDownloading() {
                view.text = "${mDownloadInfo?.percent}%"
            }

            override fun onTip() {
                view.text = "缓存"
            }

            override fun onError() {
                view.text = "缓存"
            }

            override fun onFinish() {
                view.text = "已缓存"
            }

            override fun onProgress() {
                view.text = "${mDownloadInfo?.percent}%"
            }

        }
    }
}