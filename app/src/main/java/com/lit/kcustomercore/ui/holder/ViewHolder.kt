/*
 * Copyright (c) 2020. vipyinzhiwei <vipyinzhiwei@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lit.kcustomercore.ui.holder

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.eyepetizer.android.logic.model.Daily
import com.lit.kcustomercore.Constant.ItemViewType.Companion.AUTO_PLAY_VIDEO_AD
import com.lit.kcustomercore.Constant.ItemViewType.Companion.BANNER
import com.lit.kcustomercore.Constant.ItemViewType.Companion.BANNER3
import com.lit.kcustomercore.Constant.ItemViewType.Companion.COLUMN_CARD_LIST
import com.lit.kcustomercore.Constant.ItemViewType.Companion.FOLLOW_CARD
import com.lit.kcustomercore.Constant.ItemViewType.Companion.HORIZONTAL_SCROLL_CARD
import com.lit.kcustomercore.Constant.ItemViewType.Companion.INFORMATION_CARD
import com.lit.kcustomercore.Constant.ItemViewType.Companion.SPECIAL_SQUARE_CARD_COLLECTION
import com.lit.kcustomercore.Constant.ItemViewType.Companion.TAG_BRIEFCARD
import com.lit.kcustomercore.Constant.ItemViewType.Companion.TEXT_CARD_FOOTER2
import com.lit.kcustomercore.Constant.ItemViewType.Companion.TEXT_CARD_FOOTER3
import com.lit.kcustomercore.Constant.ItemViewType.Companion.TEXT_CARD_HEADER4
import com.lit.kcustomercore.Constant.ItemViewType.Companion.TEXT_CARD_HEADER5
import com.lit.kcustomercore.Constant.ItemViewType.Companion.TEXT_CARD_HEADER7
import com.lit.kcustomercore.Constant.ItemViewType.Companion.TEXT_CARD_HEADER8
import com.lit.kcustomercore.Constant.ItemViewType.Companion.TOPIC_BRIEFCARD
import com.lit.kcustomercore.Constant.ItemViewType.Companion.UGC_SELECTED_CARD_COLLECTION
import com.lit.kcustomercore.Constant.ItemViewType.Companion.UNKNOWN
import com.lit.kcustomercore.Constant.ItemViewType.Companion.VIDEO_SMALL_CARD
import com.lit.kcustomercore.R
import com.lit.kcustomercore.bean.*
import com.lit.kcustomercore.extension.inflate
import com.lit.kcustomercore.ui.home.discovery.DiscoveryAdapter
import com.lit.krecyclerview.BaseViewHolder
import com.zhpan.bannerview.BannerViewPager

/**
 * 项目通用ViewHolder，集中统一管理。
 *
 * @author vipyinzhiwei
 * @since  2020/5/1
 */

/**
 * 未知类型，占位进行容错处理。
 */
class EmptyViewHolder(view: View) : BaseViewHolder(view)

class TextCardViewHeader4ViewHolder(view: View) : BaseViewHolder(view) {}

class TextCardViewHeader5ViewHolder(view: View) : BaseViewHolder(view) {

}

class TextCardViewHeader7ViewHolder(view: View) : BaseViewHolder(view) {

}

class TextCardViewHeader8ViewHolder(view: View) : BaseViewHolder(view) {

}

class TextCardViewFooter2ViewHolder(view: View) : BaseViewHolder(view) {

}

class TextCardViewFooter3ViewHolder(view: View) : BaseViewHolder(view) {

}

class HorizontalScrollCardViewHolder(view: View) : BaseViewHolder(view) {
    val bannerViewPager: BannerViewPager<Discovery.ItemX, DiscoveryAdapter.HorizontalScrollCardAdapter.ViewHolder> = view.findViewById(R.id.bannerViewPager)
}

class SpecialSquareCardCollectionViewHolder(view: View) : BaseViewHolder(view) {}

class ColumnCardListViewHolder(view: View) : BaseViewHolder(view) {

}

class FollowCardViewHolder(view: View) : BaseViewHolder(view) {

}

class Banner3ViewHolder(view: View) : BaseViewHolder(view) {

}

class VideoSmallCardViewHolder(view: View) : BaseViewHolder(view) {

}

class TagBriefCardViewHolder(view: View) : BaseViewHolder(view) {

}

class TopicBriefCardViewHolder(view: View) : BaseViewHolder(view) {

}

class InformationCardFollowCardViewHolder(view: View) : BaseViewHolder(view) {

}

class AutoPlayVideoAdViewHolder(view: View) : BaseViewHolder(view) {

}

class BannerViewHolder(view: View) : BaseViewHolder(view) {

}

class UgcSelectedCardCollectionViewHolder(view: View) : BaseViewHolder(view) {

}

/**
 * RecyclerView帮助类，获取通用ViewHolder或ItemViewType。
 */
object RecyclerViewHelp {

    fun getViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {

        TEXT_CARD_HEADER4 -> TextCardViewHeader4ViewHolder(R.layout.item_text_card_type_header_four.inflate(parent))

        TEXT_CARD_HEADER5 -> TextCardViewHeader5ViewHolder(R.layout.item_text_card_type_header_five.inflate(parent))

        TEXT_CARD_HEADER7 -> TextCardViewHeader7ViewHolder(R.layout.item_text_card_type_header_seven.inflate(parent))

        TEXT_CARD_HEADER8 -> TextCardViewHeader8ViewHolder(R.layout.item_text_card_type_header_eight.inflate(parent))

        TEXT_CARD_FOOTER2 -> TextCardViewFooter2ViewHolder(R.layout.item_text_card_type_footer_two.inflate(parent))

        TEXT_CARD_FOOTER3 -> TextCardViewFooter3ViewHolder(R.layout.item_text_card_type_footer_three.inflate(parent))

        HORIZONTAL_SCROLL_CARD -> HorizontalScrollCardViewHolder(R.layout.item_horizontal_scroll_card_type.inflate(parent))

        SPECIAL_SQUARE_CARD_COLLECTION -> SpecialSquareCardCollectionViewHolder(R.layout.item_special_square_card_collection_type.inflate(parent))

        COLUMN_CARD_LIST -> ColumnCardListViewHolder(R.layout.item_column_card_list_type.inflate(parent))

        BANNER -> BannerViewHolder(R.layout.item_banner_type.inflate(parent))

        BANNER3 -> Banner3ViewHolder(R.layout.item_banner_three_type.inflate(parent))

        VIDEO_SMALL_CARD -> VideoSmallCardViewHolder(R.layout.item_video_small_card_type.inflate(parent))

        TAG_BRIEFCARD -> TagBriefCardViewHolder(R.layout.item_brief_card_tag_brief_card_type.inflate(parent))

        TOPIC_BRIEFCARD -> TopicBriefCardViewHolder(R.layout.item_brief_card_topic_brief_card_type.inflate(parent))

        FOLLOW_CARD -> FollowCardViewHolder(R.layout.item_follow_card_type.inflate(parent))

        INFORMATION_CARD -> InformationCardFollowCardViewHolder(R.layout.item_information_card_type.inflate(parent))

        UGC_SELECTED_CARD_COLLECTION -> UgcSelectedCardCollectionViewHolder(R.layout.item_ugc_selected_card_collection_type.inflate(parent))

        //AUTO_PLAY_VIDEO_AD -> AutoPlayVideoAdViewHolder(R.layout.item_auto_play_video_ad.inflate(parent))

        else -> EmptyViewHolder(View(parent.context))
    }

    fun getItemViewType(type: String, dataType: String) = when (type) {

        "horizontalScrollCard" -> {
            when (dataType) {
                "HorizontalScrollCard" -> HORIZONTAL_SCROLL_CARD
                else -> UNKNOWN
            }
        }
        "specialSquareCardCollection" -> {
            when (dataType) {
                "ItemCollection" -> SPECIAL_SQUARE_CARD_COLLECTION
                else -> UNKNOWN
            }
        }
        "columnCardList" -> {
            when (dataType) {
                "ItemCollection" -> COLUMN_CARD_LIST
                else -> UNKNOWN
            }
        }
        /*"textCard" -> {
            when (item.data.type) {
                "header5" -> TEXT_CARD_HEADER5
                "header7" -> TEXT_CARD_HEADER7
                "header8" -> TEXT_CARD_HEADER8
                "footer2" -> TEXT_CARD_FOOTER2
                "footer3" -> TEXT_CARD_FOOTER3
                else -> UNKNOWN
            }
        }*/
        "banner" -> {
            when (dataType) {
                "Banner" -> BANNER
                else -> UNKNOWN
            }
        }
        "banner3" -> {
            when (dataType) {
                "Banner" -> BANNER3
                else -> UNKNOWN
            }
        }
        "videoSmallCard" -> {
            when (dataType) {
                "VideoBeanForClient" -> VIDEO_SMALL_CARD
                else -> UNKNOWN
            }
        }
        "briefCard" -> {
            when (dataType) {
                "TagBriefCard" -> TAG_BRIEFCARD
                "TopicBriefCard" -> TOPIC_BRIEFCARD
                else -> UNKNOWN
            }
        }
        "followCard" -> {
            when (dataType) {
                "FollowCard" -> FOLLOW_CARD
                else -> UNKNOWN
            }
        }
        "informationCard" -> {
            when (dataType) {
                "InformationCard" -> INFORMATION_CARD
                else -> UNKNOWN
            }
        }
        "ugcSelectedCardCollection" -> {
            when (dataType) {
                "ItemCollection" -> UGC_SELECTED_CARD_COLLECTION
                else -> UNKNOWN
            }
        }
        "autoPlayVideoAd" -> {
            when (dataType) {
                "AutoPlayVideoAdDetail" -> AUTO_PLAY_VIDEO_AD
                else -> UNKNOWN
            }
        }
        else -> UNKNOWN
    }

    private fun getTextCardType(type: String) = when (type) {
        "header4" -> TEXT_CARD_HEADER4
        "header5" -> TEXT_CARD_HEADER5
        "header7" -> TEXT_CARD_HEADER7
        "header8" -> TEXT_CARD_HEADER8
        "footer2" -> TEXT_CARD_FOOTER2
        "footer3" -> TEXT_CARD_FOOTER3
        else -> UNKNOWN
    }

    fun getItemViewType(item: Discovery.Item): Int {
        return if (item.type == "textCard") getTextCardType(item.data.type) else getItemViewType(item.type, item.data.dataType)
    }

    fun getItemViewType(item: HomePageRecommend.Item): Int {
        return if (item.type == "textCard") getTextCardType(item.data.type) else getItemViewType(item.type, item.data.dataType)
    }

    fun getItemViewType(item: Daily.Item): Int {
        return if (item.type == "textCard") getTextCardType(item.data.type) else getItemViewType(item.type, item.data.dataType)
    }

    fun getItemViewType(item: Follow.Item): Int {
        return if (item.type == "textCard") getTextCardType(item.data.type) else getItemViewType(item.type, item.data.dataType)
    }

    fun getItemViewType(item: VideoRelated.Item): Int {
        return if (item.type == "textCard") getTextCardType(item.data.type) else getItemViewType(item.type, item.data.dataType)
    }

    fun getItemViewType(item: VideoReplies.Item): Int {
        return if (item.type == "textCard") getTextCardType(item.data.type) else getItemViewType(item.type, item.data.dataType)
    }

}