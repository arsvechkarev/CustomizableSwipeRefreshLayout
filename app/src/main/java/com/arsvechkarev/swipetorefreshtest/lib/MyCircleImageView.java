/*
 * Copyright 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.arsvechkarev.swipetorefreshtest.lib;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.arsvechkarev.swipetorefreshtest.R;

/**
 * Private class created to work around issues with AnimationListeners being
 * called before the animation is actually complete and support shadows on older
 * platforms.
 */
class MyCircleImageView extends View {

    private final int mainCircleColor = Color.parseColor("#c91830");
    private final int mainCircleTopColor = Color.parseColor("#eda909");

    private Animation.AnimationListener mListener;

    private final Paint mainCirclePaint = new Paint();
    private final Paint mainCircleTopPaint = new Paint();
    private final RectF mainCircleTopRectF = new RectF();

    private ParticlesAnimatingState particlesAnimatingState = ParticlesAnimatingState.NONE;
    private float spreadingAnimatingCoefficient = 0f;

    private final ValueAnimator spreadingAnimator = new ValueAnimator();

    public MyCircleImageView(@NonNull Context context) {
        super(context);
        init();
    }

    public MyCircleImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyCircleImageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private final Drawable snowflakeDrawable = new BitmapDrawable(getResources(),
            BitmapFactory.decodeResource(getResources(), R.drawable.snowflake));


    private void init() {
        snowflakeDrawable.setColorFilter(new PorterDuffColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP));
        mainCirclePaint.setColor(mainCircleColor);
        mainCircleTopPaint.setColor(mainCircleTopColor);
        spreadingAnimator.addUpdateListener(animation -> {
            spreadingAnimatingCoefficient = (float) animation.getAnimatedValue();
            invalidate();
        });
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        int circleTopWidth = w / 6;
        int circleTopHeight = h / 8;
        mainCircleTopRectF.set(
                w / 2f - circleTopWidth,
                -(circleTopHeight / 2f),
                w / 2f + circleTopWidth,
                circleTopHeight / 2f
        );
        int offset = w / 6;
        snowflakeDrawable.setBounds(offset, offset, w - offset, h - offset);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float x = getWidth() / 2f;
        float y = getHeight() / 2f;
        float radius = getWidth() / 2f;
        canvas.drawCircle(x, y, radius, mainCirclePaint);
        snowflakeDrawable.draw(canvas);
        canvas.drawRoundRect(mainCircleTopRectF, 10f, 10f, mainCircleTopPaint);
        switch (particlesAnimatingState) {
            case NONE:
                // Draw nothing
                break;
            case SPREADING:
                drawParticles(canvas);
                break;
        }
    }

    private void drawParticles(Canvas canvas) {
        float x2 = getWidth() / 2f - ((getWidth() / 2f) * spreadingAnimatingCoefficient);
        canvas.drawCircle(x2, getHeight(), 50f, mainCircleTopPaint);
    }

    public void setAnimationListener(Animation.AnimationListener listener) {
        mListener = listener;
    }

    @Override
    public void onAnimationStart() {
        super.onAnimationStart();
        if (mListener != null) {
            mListener.onAnimationStart(getAnimation());
        }
    }

    @Override
    public void onAnimationEnd() {
        super.onAnimationEnd();
        if (mListener != null) {
            mListener.onAnimationEnd(getAnimation());
        }
    }

    public void onStartAnimatingToIdlePosition() {
        particlesAnimatingState = ParticlesAnimatingState.SPREADING;
        if (!spreadingAnimator.isRunning()) {
            spreadingAnimator.setFloatValues(spreadingAnimatingCoefficient, 1f);
            spreadingAnimator.start();
        }
    }

    public void onEndAnimatingToIdlePosition() {
        particlesAnimatingState = ParticlesAnimatingState.NONE;
        invalidate();
        if (spreadingAnimator.isRunning()) {
            spreadingAnimatingCoefficient = 0f;
            spreadingAnimator.cancel();
        }
    }

    public enum ParticlesAnimatingState {
        NONE,
        SPREADING
    }
}
