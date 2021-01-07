package com.library.base.gif;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.util.AttributeSet;
import android.view.View;


/**
 * @see CustomGifView
 * User: SeasonAllan(451360508@qq.com)
 * Time: 2017-12-12 18:37
 */
public class CustomGifView extends View {


    public CustomGifView(Context context) {
        super(context);
    }

    public CustomGifView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomGifView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    float scale = 1.0f;

    public void setResource(int resourceId) {
        mMovie = Movie.decodeStream(getResources().openRawResource(resourceId));
        int width = getMeasuredWidth();
        scale = width * 1.0f / mMovie.width();

        duration = mMovie.duration();
        if (duration == 0) {
            duration = 1000;
        }
        invalidate();
    }

    private int duration;
    private Movie mMovie;
    private long mMovieStart = -1;

    public void onDraw(Canvas canvas) {
        long now = System.currentTimeMillis();

        if (mMovieStart <= 0) { // first time
            mMovieStart = now;
        }
        if (mMovie != null) {
            if ((now - mMovieStart)/ duration >= 1){
                mMovieStart = now;
                playTime --;
                if (playTime <= 0){
                    if (listener != null){
                        listener.onClick(this);
                        listener = null;
                    }
                }
            }
            int relTime = (int) ((now - mMovieStart) % duration);
            mMovie.setTime(relTime);
            canvas.save();
            canvas.scale(scale, scale);
            mMovie.draw(canvas, 0, 0);
            canvas.restore();
            if (playTime > 0){
                invalidate();
            }
        }
    }


    private int playTime = Integer.MAX_VALUE;
    private OnClickListener listener;
    public void setPlayTime(int time, OnClickListener listener){
        this.playTime = time;
        this.listener = listener;
    }

    public void clearListener(){
        listener = null;
        playTime = 0;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mMovie = null;
    }


}
