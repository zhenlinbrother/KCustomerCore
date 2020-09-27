package com.lit.kcustomercore.view

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.lit.kcustomercore.R
import com.lit.kcustomercore.utils.TypeFaceUtil

/**
 * author       : linc
 * time         : 2020/9/25
 * desc         : 自定义textview
 * version      : 1.0.0
 */
class TypefaceTextView : AppCompatTextView {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        attrs.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.TypefaceTextView, 0,0)
            val typefaceType = typedArray.getInt(R.styleable.TypefaceTextView_typeface, 0)
            typeface = Companion.getTypeface(typefaceType)
            typedArray.recycle()
        }
    }

    companion object {

        /**
         * 根据字体类型，获取自定义字体
         * @param typefaceType Int?
         * @return (android.graphics.Typeface..android.graphics.Typeface?)
         */
        fun getTypeface(typefaceType: Int?) = when(typefaceType){
            TypeFaceUtil.FZLL_TYPEFACE -> TypeFaceUtil.getFzlLTypeface()
            TypeFaceUtil.FZDB1_TYPEFACE -> TypeFaceUtil.getFzdb1Typeface()
            TypeFaceUtil.FUTURA_TYPEFACE -> TypeFaceUtil.getFuturaTypeface()
            TypeFaceUtil.DIN_TYPEFACE -> TypeFaceUtil.getDinTypeface()
            TypeFaceUtil.LOBSTER_TYPEFACE -> TypeFaceUtil.getLobsterTypeface()
            else -> Typeface.DEFAULT
        }
    }
}