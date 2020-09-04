package com.lit.krecyclerview.widget

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.lit.krecyclerview.R
import java.util.*
/**
 * <菊花 loading> <功能详细描述>
 *
 * @author linc
 * @version 2020/8/31
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
class BallSpinFadeLoader @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    View(context, attrs, defStyleAttr) {
    private var mPaint: Paint? = null
    private var mCenterX = 0f
    private var mCenterY = 0f
    private var mRadius = 0f

    @Volatile
    var isLoading = false
        private set
    var scaleFloats = floatArrayOf(
        SCALE,
        SCALE,
        SCALE,
        SCALE,
        SCALE,
        SCALE,
        SCALE,
        SCALE
    )
    var alphas = intArrayOf(
        ALPHA,
        ALPHA,
        ALPHA,
        ALPHA,
        ALPHA,
        ALPHA,
        ALPHA,
        ALPHA
    )
    private val scaleAnimList: MutableList<ValueAnimator> =
        ArrayList()
    private val alphaAnimList: MutableList<ValueAnimator> =
        ArrayList()

    private fun init(context: Context) {
        mPaint = Paint()
        mPaint!!.isAntiAlias = true
        mPaint!!.style = Paint.Style.FILL
        mPaint!!.strokeWidth = dip2px(1f).toFloat()
        mPaint!!.color = ContextCompat.getColor(context, R.color.l_recycler_balling_color)
        val delays = intArrayOf(0, 120, 240, 360, 480, 600, 720, 780, 840)
        for (i in 0..7) {
            val scaleAnim = ValueAnimator.ofFloat(1f, 0.4f, 1f)
            scaleAnim.duration = 1000
            scaleAnim.repeatMode = ValueAnimator.RESTART
            scaleAnim.repeatCount = ValueAnimator.INFINITE
            scaleAnim.startDelay = delays[i].toLong()
            scaleAnimList.add(scaleAnim)
            val alphaAnim = ValueAnimator.ofInt(255, 77, 255)
            alphaAnim.duration = 1000
            alphaAnim.repeatMode = ValueAnimator.RESTART
            alphaAnim.repeatCount = ValueAnimator.INFINITE
            alphaAnim.startDelay = delays[i].toLong()
            alphaAnimList.add(alphaAnim)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthSpecMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightSpecMode = MeasureSpec.getMode(heightMeasureSpec)
        val widthSpecSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSpecSize = MeasureSpec.getSize(heightMeasureSpec)

        //处理 wrap_content问题
        val defaultDimension = dip2px(30f)
        if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(defaultDimension, defaultDimension)
        } else if (widthSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(defaultDimension, heightSpecSize)
        } else if (heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSpecSize, defaultDimension)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mRadius = w / 10.toFloat()
        mCenterX = (w shr 1.toFloat().toInt()).toFloat()
        mCenterY = (h shr 1.toFloat().toInt()).toFloat()
    }

    fun dip2px(dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        for (i in alphas.indices) {
            canvas.save()
            canvas.translate(
                mCenterX + ((width shr 1) - mRadius) * Math.cos(
                    Math.toRadians(
                        i * 45.toDouble()
                    )
                ).toFloat(),
                mCenterY + ((width shr 1) - mRadius) * Math.sin(
                    Math.toRadians(
                        i * 45.toDouble()
                    )
                ).toFloat()
            )
            canvas.scale(scaleFloats[i], scaleFloats[i])
            mPaint!!.alpha = alphas[i]
            canvas.drawCircle(0f, 0f, mRadius, mPaint!!)
            canvas.restore()
        }
    }

    fun startAnimator() {
        for (i in scaleAnimList.indices) {
            val valueAnimator = scaleAnimList[i]
            valueAnimator.addUpdateListener { animation ->
                scaleFloats[i] = animation.animatedValue as Float
                postInvalidate()
            }
            valueAnimator.start()
        }
        for (i in alphaAnimList.indices) {
            val valueAnimator = alphaAnimList[i]
            valueAnimator.addUpdateListener { animation ->
                alphas[i] = animation.animatedValue as Int
                postInvalidate()
            }
            valueAnimator.start()
        }
        isLoading = true
    }

    fun stopAnimator() {
        if (isLoading) {
            for (valueAnimator in scaleAnimList) {
                valueAnimator.removeAllUpdateListeners()
                valueAnimator.cancel()
            }
            for (valueAnimator in alphaAnimList) {
                valueAnimator.removeAllUpdateListeners()
                valueAnimator.cancel()
            }
        }
        isLoading = false
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        startAnimator()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stopAnimator()
    }

    companion object {
        const val SCALE = 1.0f
        const val ALPHA = 255
    }

    init {
        init(context)
    }
}
