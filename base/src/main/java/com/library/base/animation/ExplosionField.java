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

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.DecelerateInterpolator;

import java.util.ArrayList;
import java.util.List;


public class ExplosionField extends View {

    private List<ExplosionAnimator> mExplosions = new ArrayList<>();
    private int[] mExpandInset = {Integer.MIN_VALUE, Integer.MIN_VALUE};

    public ExplosionField(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (ExplosionAnimator explosion : mExplosions) {
            explosion.draw(canvas);
        }
    }

    public void setDestinationPosition(int dx, int dy) {
        mExpandInset[0] = dx;
        mExpandInset[1] = dy;
    }

    public void explode(View view, Runnable runnable) {
        Rect r = new Rect();
        view.getGlobalVisibleRect(r);

        ExplosionAnimator explosion = new ExplosionAnimator(this, Utils.createBitmapFromView(view), r){
            @Override
            protected int getDestinationX() {
                return mExpandInset[0];
            }

            @Override
            protected int getDestinationY() {
                return mExpandInset[1];
            }
        };
        explosion.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mExplosions.remove(animation);
            }
        });
        if (runnable != null) {
            view.postDelayed(runnable, 1000);
        }
        explosion.setInterpolator(new DecelerateInterpolator());
        explosion.setDuration(getDuration());
        mExplosions.add(explosion);
        explosion.start();
    }

    protected int getDuration() {
        return 1600;
    }

    public void clear() {
        mExplosions.clear();
        invalidate();
    }

    public static ExplosionField attach2Window(Activity activity) {
        return attach2Window(activity, 1600);
    }

    public static ExplosionField attach2Window(Activity activity, final int duration) {
        ViewGroup rootView = activity.findViewById(Window.ID_ANDROID_CONTENT);
        ExplosionField explosionField = new ExplosionField(activity) {
            @Override
            protected int getDuration() {
                return duration;
            }
        };
        rootView.addView(explosionField, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return explosionField;
    }

}
