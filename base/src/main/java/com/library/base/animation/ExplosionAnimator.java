/*
 * Copyright (C) 2015 tyrantgit
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
package com.library.base.animation;

import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import java.util.Random;

public class ExplosionAnimator extends ValueAnimator {

    private static final float V = Utils.dp2Px(2);
    private Paint mPaint;
    private Particle[] mParticles;
    private Rect mBound;
    private View mContainer;

    public ExplosionAnimator(View container, Bitmap bitmap, Rect bound) {
        mPaint = new Paint();
        mBound = new Rect(bound);
        mParticles = new Particle[partLen * partLen];
        int w = bitmap.getWidth() / (partLen);
        int h = bitmap.getHeight() / (partLen);
        for (int i = 0; i < partLen; i++) {
            for (int j = 0; j < partLen; j++) {
                mParticles[(i * partLen) + j] = generateParticle(bitmap.getPixel(j * w, i * h), i, j);
            }
        }
        mContainer = container;
        setFloatValues(0f, 1);
    }


    int partLen = 15;

    private Particle generateParticle(int color, int i, int j) {
        Particle particle = new Particle();
        if (isDestinationValid()) {
            particle.color = color;
            particle.radius = V;
            particle.baseCx = mBound.left + mBound.width() * i / partLen;
            particle.baseCy = mBound.top + mBound.height() * j / partLen;
            particle.baseCx = mBound.left + mBound.width() * i / partLen;
            particle.baseCy = mBound.top + mBound.height() * j / partLen;
            particle.centerX = mBound.left + mBound.width() / 2;
            particle.centerY = mBound.top + mBound.height() / 2;
            particle.cx = mBound.left + mBound.width() / 2;
            particle.cy = mBound.top + mBound.height() / 2;
            particle.delay = new Random().nextFloat() / 1;
            particle.alpha = 0f;
        } else {
            particle.color = color;
            particle.radius = V;
            if (new Random().nextFloat() < 0.2f) {
                particle.baseRadius = V + ((X - V) * new Random().nextFloat());
            } else {
                particle.baseRadius = W + ((V - W) * new Random().nextFloat());
            }
            float nextFloat = new Random().nextFloat();
            particle.top = mBound.height() * ((0.18f * new Random().nextFloat()) + 0.2f);
            particle.top = nextFloat < 0.2f ? particle.top : particle.top + ((particle.top * 0.2f) * new Random().nextFloat());
            particle.bottom = (mBound.height() * (new Random().nextFloat() - 0.5f)) * 1.8f;
            float f = nextFloat < 0.2f ? particle.bottom : nextFloat < 0.8f ? particle.bottom * 0.6f : particle.bottom * 0.3f;
            particle.bottom = f;
            particle.mag = 4.0f * particle.top / particle.bottom;
            particle.neg = (-particle.mag) / particle.bottom;
            f = mBound.centerX() + (Y * (new Random().nextFloat() - 0.5f));
            particle.baseCx = f;
            particle.cx = f;
            f = mBound.centerY() + (Y * (new Random().nextFloat() - 0.5f));
            particle.baseCy = f;
            particle.cy = f;
            particle.life = END_VALUE / 10 * new Random().nextFloat();
            particle.overflow = 0.4f * new Random().nextFloat();
            particle.alpha = 1f;
        }
        return particle;
    }

    public boolean draw(Canvas canvas) {
        if (!isStarted()) {
            return false;
        }
        for (Particle particle : mParticles) {
            if (isDestinationValid()) {
                particle.advance((float) getAnimatedValue());
            } else {
                particle.advance2((float) getAnimatedValue());
            }
            if (particle.alpha > 0f) {
                mPaint.setColor(particle.color);
                mPaint.setAlpha((int) (Color.alpha(particle.color) * particle.alpha));
                canvas.drawCircle(particle.cx, particle.cy, particle.radius, mPaint);
            }
        }
        mContainer.invalidate();
        return true;
    }

    @Override
    public void start() {
        super.start();
        mContainer.invalidate(mBound);
    }


    protected boolean isDestinationValid() {
        return getDestinationX() != Integer.MIN_VALUE && getDestinationY() != Integer.MIN_VALUE;
    }

    protected int getDestinationX() {
        return Integer.MIN_VALUE;
    }

    protected int getDestinationY() {
        return Integer.MIN_VALUE;
    }

    private static final float END_VALUE = 1.4f;
    private static final float X = Utils.dp2Px(5);
    private static final float Y = Utils.dp2Px(20);
    private static final float W = Utils.dp2Px(1);

    private class Particle {
        float alpha;
        int color;
        float cx;
        float cy;
        float radius;
        float baseCx;
        float baseCy;
        float delay;

        float centerX;
        float centerY;


        float baseRadius;
        float top;
        float bottom;
        float mag;
        float neg;
        float life;
        float overflow;

        public void advance2(float factor) {
            float f = 0f;
            float normalization = factor / END_VALUE;
            if (normalization < life || normalization > 1f - overflow) {
                alpha = 0f;
                return;
            }
            normalization = (normalization - life) / (1f - life - overflow);
            float f2 = normalization * END_VALUE;
            if (normalization >= 0.7f) {
                f = (normalization - 0.7f) / 0.3f;
            }
            alpha = 1f - f;
            f = bottom * f2;
            cx = baseCx + f;
            cy = (float) (baseCy - this.neg * Math.pow(f, 2.0)) - f * mag;
            radius = V + (baseRadius - V) * f2;
        }


        public void advance(float factor) {
            if (factor < delay) {
                alpha = factor / delay;

                cx = centerX + (baseCx - centerX) * alpha;
                cy = centerY + (baseCy - centerY) * alpha;
                return;
            }
            factor = (factor - delay) / (1 - delay);
            alpha = 1f - factor;
            cx = baseCx + (getDestinationX() - baseCx) * factor;
            cy = baseCy + (getDestinationY() - baseCy) * factor + factor * (float) (Math.sin(1 * 2 * (float) Math.PI / 360.0f + 2 * Math.PI * factor));
            cy = baseCy + (getDestinationY() - baseCy) * factor;
        }
    }
}
