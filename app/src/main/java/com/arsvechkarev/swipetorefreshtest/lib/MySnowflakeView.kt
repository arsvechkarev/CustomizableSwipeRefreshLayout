package com.arsvechkarev.swipetorefreshtest.lib

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.View
import android.view.animation.Animation
import androidx.annotation.Keep
import com.arsvechkarev.swipetorefresh.BaseProgressView
import com.arsvechkarev.swipetorefreshtest.R

@Keep
class MySnowflakeView(context: Context) : BaseProgressView(context) {

    private val mainCircleColor = Color.parseColor("#c91830")
    private val mainCircleTopColor = Color.parseColor("#eda909")

    private val mainCirclePaint = Paint()
    private val mainCircleTopPaint = Paint()
    private val mainCircleTopRectF = RectF()

    private var particlesAnimatingState = ParticlesAnimatingState.NONE
    private var spreadingAnimatingCoefficient = 0f

    private val spreadingAnimator = ValueAnimator()

    private val snowflakeDrawable: Drawable = BitmapDrawable(
        resources,
        BitmapFactory.decodeResource(resources, R.drawable.snowflake)
    )

    init {
        snowflakeDrawable.colorFilter = PorterDuffColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)
        mainCirclePaint.color = mainCircleColor
        mainCircleTopPaint.color = mainCircleTopColor
        spreadingAnimator.addUpdateListener { animation ->
            spreadingAnimatingCoefficient = animation.animatedValue as Float
            invalidate()
        }
    }

    override fun onStartAnimatingToIdlePosition() {
        particlesAnimatingState = ParticlesAnimatingState.SPREADING
        if (!spreadingAnimator.isRunning) {
            spreadingAnimator.setFloatValues(spreadingAnimatingCoefficient, 1f)
            spreadingAnimator.start()
        }
    }

    override fun onEndAnimatingToIdlePosition() {
        particlesAnimatingState = ParticlesAnimatingState.NONE
        invalidate()
        if (spreadingAnimator.isRunning) {
            spreadingAnimatingCoefficient = 0f
            spreadingAnimator.cancel()
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        val circleTopWidth = w / 6
        val circleTopHeight = h / 8
        mainCircleTopRectF.set(
            w / 2f - circleTopWidth, -(circleTopHeight / 2f),
            w / 2f + circleTopWidth, circleTopHeight / 2f
        )
        val offset = w / 6
        snowflakeDrawable.setBounds(offset, offset, w - offset, h - offset)
    }

    override fun onDraw(canvas: Canvas) {
        val x = width / 2f
        val y = height / 2f
        val radius = width / 2f
        canvas.drawCircle(x, y, radius, mainCirclePaint)
        snowflakeDrawable.draw(canvas)
        canvas.drawRoundRect(mainCircleTopRectF, 10f, 10f, mainCircleTopPaint)
        if (particlesAnimatingState == ParticlesAnimatingState.SPREADING) {
            drawParticles(canvas)
        }
    }

    private fun drawParticles(canvas: Canvas) {
    }

    enum class ParticlesAnimatingState {
        NONE, SPREADING
    }
}