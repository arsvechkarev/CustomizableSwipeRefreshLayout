package com.arsvechkarev.swipetorefresh

import android.content.Context
import android.view.View
import android.view.animation.Animation

abstract class BaseProgressView(context: Context) : View(context) {

    private var listener: Animation.AnimationListener? = null

    fun setAnimationListener(listener: Animation.AnimationListener) {
        this.listener = listener
    }

    override fun onAnimationStart() {
        super.onAnimationStart()
        listener?.onAnimationStart(animation)
    }

    override fun onAnimationEnd() {
        super.onAnimationEnd()
        listener?.onAnimationEnd(animation)
    }

    abstract fun onStartAnimatingToIdlePosition()

    abstract fun onEndAnimatingToIdlePosition()
}