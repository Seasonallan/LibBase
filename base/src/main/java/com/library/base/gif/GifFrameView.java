package com.library.base.gif;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.View;

import com.library.base.gif.frame.GifDecoderOneByOne;
import com.library.base.gif.movie.FrameDecoder;


/**
 * 可解析大GIF图片，只缓存两帧bitmap
 *
 * @see GifFrameView
 * User: SeasonAllan(451360508@qq.com)
 * Time: 2017-12-12 18:37
 */
public class GifFrameView extends View {

    public String url;
    private int position;

    public GifFrameView(Context context) {
        super(context);
    }

    public GifFrameView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GifFrameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private GifDecoderOneByOne gifDecoder = null;


    public void setResource(int resourceId) {
        FrameDecoder frameDecoder = new FrameDecoder(getContext(), resourceId);
        Bitmap firstFrame = frameDecoder.getFrame();
        int width = getWidth();
        int frameWidth = width;
        if (firstFrame != null) {
            frameWidth = firstFrame.getWidth();
            if (!firstFrame.isRecycled()) {
                firstFrame.recycle();
            }
        }
        float scale = width * 1.0f / frameWidth;
        mMatrix.postScale(scale, scale);

        gifDecoder = new GifDecoderOneByOne();
        gifDecoder.setGifImage(resourceId);
        gifDecoder.start();
        requestLayout();

    }
    Matrix mMatrix = new Matrix();


    public void onRelease() {
        if (gifDecoder != null && gifDecoder.getState() != Thread.State.TERMINATED) {
            gifDecoder.release();
            gifDecoder.interrupt();
        }
        gifDecoder = null;
    }


    @Override
    public void onDraw(Canvas canvas) {
        if (gifDecoder == null) {
            return;
        }
        Bitmap gifFrame = gifDecoder.getFrame(position);
        if (gifFrame != null && gifFrame.isRecycled() == false) {
            canvas.drawBitmap(gifFrame, mMatrix, null);
            //canvas.drawBitmap(gifFrame, 0, 0, null);
        }
        invalidate();
    }



    public void setPosition(int i) {
        this.position = i;
    }
}
