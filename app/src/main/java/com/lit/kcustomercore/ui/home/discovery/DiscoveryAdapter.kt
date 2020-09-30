package com.lit.kcustomercore.ui.home.discovery

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.lit.kcustomercore.R
import com.lit.kcustomercore.bean.Discovery
import com.lit.kcustomercore.extension.*
import com.lit.kcustomercore.ui.holder.*
import com.lit.kcustomercore.view.GridListItemDecoration
import com.lit.krecyclerview.BaseViewHolder
import com.zhpan.bannerview.BaseBannerAdapter
import com.zhpan.bannerview.constants.PageStyle
import kotlinx.android.synthetic.main.item_banner_item_type.view.*
import kotlinx.android.synthetic.main.item_column_card_list_type.view.*
import kotlinx.android.synthetic.main.item_follow_card_type.view.*
import kotlinx.android.synthetic.main.item_follow_card_type.view.tvLabel
import kotlinx.android.synthetic.main.item_follow_card_type.view.tvTitle
import kotlinx.android.synthetic.main.item_text_card_type_footer_three.view.*
import kotlinx.android.synthetic.main.item_text_card_type_footer_two.view.*
import kotlinx.android.synthetic.main.item_text_card_type_header_eight.view.*
import kotlinx.android.synthetic.main.item_text_card_type_header_five.view.*
import kotlinx.android.synthetic.main.item_text_card_type_header_seven.view.*
/**
 * author       : linc
 * time         : 2020/9/29
 * desc         : 发现页面 适配器
 * version      : 1.0.0
 */
class DiscoveryAdapter(val fragment: DiscoveryFragment, val dataList: List<Discovery.Item>)
    : RecyclerView.Adapter<BaseViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder
            = RecyclerViewHelp.getViewHolder(parent, viewType)

    override fun getItemCount() = dataList.size

    override fun getItemViewType(position: Int): Int = RecyclerViewHelp.getItemViewType(dataList[position])

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val item = dataList[position]
        when(holder){
            is TextCardViewHeader5ViewHolder ->{
                holder.itemView.tvTitle5.text = item.data.text
                if (item.data.actionUrl != null) holder.itemView.ivInto5.visible() else holder.itemView.ivInto5.gone()
                if (item.data.follow != null) holder.itemView.tvFollow.visible() else holder.itemView.tvFollow.gone()
                holder.itemView.tvFollow.setOnClickListener {
                    //todo 跳转到登录页面
                }
            }

            is TextCardViewHeader7ViewHolder -> {
                holder.itemView.tvTitle7.text = item.data.text
                holder.itemView.tvRightText7.text = item.data.rightText
            }

            is TextCardViewHeader8ViewHolder -> {
                holder.itemView.tvTitle8.text = item.data.text
                holder.itemView.tvRightText8.text = item.data.rightText
            }

            is TextCardViewFooter2ViewHolder -> {
                holder.itemView.tvFooterRightText2.text = item.data.text
            }

            is TextCardViewFooter3ViewHolder -> {
                holder.itemView.tvFooterRightText3.text = item.data.text
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
                    ivShare.setOnClickListener {
                        //todo 打开分享窗口
                    }
                    setOnClickListener {
                        item.data.content.data.run {
                            //todo 跳转动作
                        }
                    }
                    }
                }

            is HorizontalScrollCardViewHolder -> {
                holder.bannerViewPager.run {
                    setCanLoop(true)
                    setRoundCorner(dp2px(4f))
                    //setRevealWidth(LincApplication.context.resources.getDimensionPixelOffset(R.dimen.listSpaceSize))
                    //if (item.data.itemList.size == 1) setPageMargin(0) else setPageMargin(dp2px(4f))
                    setIndicatorVisibility(View.GONE)
                    setAdapter(HorizontalScrollCardAdapter())
                    //removeDefaultPageTransformer()
                    setPageStyle(PageStyle.MULTI_PAGE_OVERLAP)

                    create(item.data.itemList)
                }

            }

            is SpecialSquareCardCollectionViewHolder -> {
                with(holder.itemView){
                    tvTitle.text = item.data.header.title
                    tvRightText.text = item.data.header.rightText

                    recyclerView.layoutManager = GridLayoutManager(fragment.activity, 2).apply {
                        orientation = GridLayoutManager.HORIZONTAL
                    }
                    if (recyclerView.itemDecorationCount == 0){
                        recyclerView.addItemDecoration(SpecialSquareCardCollectionItemDecoration())
                    }
                    val list = item.data.itemList.filter { it.type == "squareCardOfCategory" && it.data.dataType == "SquareCard" }
                    recyclerView.adapter = SpecialSquareCardCollectionAdapter(list)
                }
            }

            is ColumnCardListViewHolder -> {
                with(holder.itemView){
                    tvTitle.text = item.data.header.title
                    tvRightText.text = item.data.header.rightText
                    setOnClickListener(tvRightText, ivInto) { item.data.header.rightText.toShowToast() }
                    recyclerView.layoutManager = GridLayoutManager(fragment.activity, 2)
                    if (recyclerView.itemDecorationCount == 0){
                        recyclerView.addItemDecoration(GridListItemDecoration(2))
                    }
                    val list = item.data.itemList.filter { it.type == "squareCardOfColumn" && it.data.dataType == "SquareCard" }
                    recyclerView.adapter = ColumnCardListAdapter(list)
                }
            }

            is BannerViewHolder -> {
                holder.itemView.ivPicture.load(item.data.image, 4f)
                holder.itemView.setOnClickListener {
                    //TODO
                }
            }

            is Banner3ViewHolder -> {
                with(holder.itemView){
                    ivPicture.load(item.data.image, 4f)
                    ivAvatar.load(item.data.header.icon)
                    tvTitle.text = item.data.header.title
                    tvDescription.text =  item.data.header.description
                    if (item.data.label?.text.isNullOrEmpty()) tvLabel.invisible() else tvLabel.visible()
                    tvLabel.text = item.data.label?.text ?: ""

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

            is TagBriefCardViewHolder -> {
                with(holder.itemView){
                    ivPicture.load(item.data.icon, 4f)
                    tvDescription.text = item.data.description
                    tvTitle.text = item.data.title
                    if (item.data.follow != null) tvFollow.visible() else tvFollow.gone()
                    setOnClickListener { item.data.title.toShowToast() }
                }
            }

            is TopicBriefCardViewHolder -> {
                with(holder.itemView){
                    ivPicture.load(item.data.icon, 4f)
                    tvDescription.text = item.data.description
                    tvTitle.text = item.data.title
                    setOnClickListener { item.data.title.toShowToast() }
                }
            }

            else -> {
                holder.itemView.gone()
            }
        }
    }

    /**
     * 广告适配器
     */
    inner class HorizontalScrollCardAdapter : BaseBannerAdapter<Discovery.ItemX, HorizontalScrollCardAdapter.ViewHolder>() {
        inner class ViewHolder(val view: View): com.zhpan.bannerview.BaseViewHolder<Discovery.ItemX>(view){
            override fun bindData(data: Discovery.ItemX, position: Int, pageSize: Int) {
                with(itemView){
                    if (data.data.label?.text.isNullOrEmpty()) tvLabel.invisible() else tvLabel.visible()
                    tvLabel.text = data.data.label?.text ?: ""
                    ivPicture.load(data.data.image, 4f)
                }
            }
        }

        override fun getLayoutId(viewType: Int): Int {
            return R.layout.item_banner_item_type
        }

        override fun createViewHolder(itemView: View, viewType: Int): ViewHolder {
            return ViewHolder(itemView)
        }

        override fun onBind(
            holder: ViewHolder,
            data: Discovery.ItemX,
            position: Int,
            pageSize: Int
        ) {
            holder.bindData(data, position, pageSize)
        }
    }

    /**
     * 热门分类 横向列表 适配器
     * @property dataList List<ItemX>
     * @constructor
     */
    inner class SpecialSquareCardCollectionAdapter(val dataList: List<Discovery.ItemX>):
        RecyclerView.Adapter<SpecialSquareCardCollectionAdapter.ViewHolder>(){

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){}

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_special_square_card_collection_type_item, parent, false))
        }

        override fun getItemCount(): Int = dataList.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = dataList[position]
            with(holder.itemView){
                ivPicture.load(item.data.image, 4f)
                tvTitle.text = item.data.title
                setOnClickListener {
                    "${item.data.title},该功能即将开放，敬请期待".showToast()
                }
            }
        }
    }

    /**
     * 热门分类 横向列表 间隔
     */
    inner class SpecialSquareCardCollectionItemDecoration : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            val position = parent.getChildAdapterPosition(view) // item position
            val count = parent.adapter?.itemCount
            val spanIndex = (view.layoutParams as GridLayoutManager.LayoutParams).spanIndex
            val spanCount = 2
            val lastRowFirshItemPosition = count?.minus(spanCount)  //最后一行，第一个item索引
            val space = dp2px(2f)
            val rightCountSpace = dp2px(14f)

            when(spanIndex){
                0 -> outRect.bottom = space
                1 -> outRect.top = space
                else -> {
                    outRect.top = space
                    outRect.bottom = space
                }
            }

            when{
                position < spanCount -> outRect.right = space
                position < lastRowFirshItemPosition!! -> {
                    outRect.left = space
                    outRect.right = space
                }
                else -> {
                    outRect.left = space
                    outRect.right = rightCountSpace
                }
            }
        }
    }

    /**
     * 专题策略 横向列表 适配器
     * @constructor
     */
    inner class ColumnCardListAdapter(val dataList: List<Discovery.ItemX>)
        : RecyclerView.Adapter<ColumnCardListAdapter.ViewHolder>() {

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){}

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_column_card_list_type_item, parent, false))
        }

        override fun getItemCount(): Int = dataList.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = dataList[position]
            with(holder.itemView){
                ivPicture.load(item.data.image, 4f)
                tvTitle.text = item.data.title
                setOnClickListener {
                    item.data.title.toShowToast()
                }
            }
        }
    }


}