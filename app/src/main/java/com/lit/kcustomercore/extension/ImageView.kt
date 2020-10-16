package com.lit.kcustomercore.extension

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.lit.kcustomercore.R
import de.hdodenhof.circleimageview.CircleImageView
import jp.wasabeef.glide.transformations.RoundedCornersTransformation

/**
 * 加载图片 可以指定圆角弧度
 * @receiver ImageView
 * @param url String
 * @param round Float
 * @param cornerType CornerType
 */
fun ImageView.load(url: String, round: Float = 0f, cornerType: RoundedCornersTransformation.CornerType = RoundedCornersTransformation.CornerType.ALL) {
    if (round == 0f){
        Glide.with(this.context).load(url).into(this)
    } else {
        val option = RequestOptions
            .bitmapTransform(RoundedCornersTransformation(dp2px(round), 0, cornerType))
            .placeholder(R.drawable.shape_album_loading_bg)

        Glide.with(this.context).load(url).apply(option).into(this)
    }
}

fun CircleImageView.load(url: String, round: Float = 0f, cornerType: RoundedCornersTransformation.CornerType = RoundedCornersTransformation.CornerType.ALL) {
    if (round == 0f){
        Glide.with(this.context).load(url).into(this)
    } else {
        val option = RequestOptions
            .bitmapTransform(RoundedCornersTransformation(dp2px(round), 0, cornerType))
            .placeholder(R.drawable.shape_album_loading_bg)

        Glide.with(this.context).load(url).apply(option).into(this)
    }
}