package com.arsvechkarev.swipetorefresh

import android.content.Context
import android.view.View
import android.view.animation.Animation

/**
 * This is a base implementation for a progress view that will be used in
 * [CustomizableSwipeRefreshLayout]. You should extend this class and implement custom
 * drawing/animations (e.g. overriding **onDraw()**, **[onStartAnimatingToIdlePosition]**,
 * [onEndAnimatingToIdlePosition] and so on), and then you should provide
 * [CustomizableSwipeRefreshLayout] with your implementation's full class name. Also, your subclass
 * should have a constructor that accepts [Context], this constructor will be called through
 * reflection.
 *
 * @see CustomizableSwipeRefreshLayout
 */
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

    /**
     * Method that is called when user releases finger and progress view starts being animated to
     * idle position. Idle position is position when progress view is just spinning, but not
     * moving
     *
     * @see onEndAnimatingToIdlePosition
     */
    abstract fun onStartAnimatingToIdlePosition()

    /**
     * Method that is called when view is finally in idle position
     *
     * @see onStartAnimatingToIdlePosition
     */
    abstract fun onEndAnimatingToIdlePosition()
}