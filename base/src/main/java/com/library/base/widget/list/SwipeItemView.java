package com.library.base.widget.list;


import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.customview.widget.ViewDragHelper;

/**
 * Created by Administrator on 2017/3/14 0014.
 * E-Mail：543441727@qq.com
 */

public class SwipeItemView extends FrameLayout {

    ViewDragHelper viewDragHelper;

    ViewDragHelper.Callback callback;
    View dragView;
    View hideView; //隐藏在下面的view

    int maxLeftPointReverse; //左边所能到达的最左边的距离的绝对值


    boolean canSwipe = false; //是否可以滑动删除
    int mHorizontalDragRange; //水平可以拖动的距离最大范围
    float downX = 0, downY = 0;
    private int mInitX;
    private int mLeft;
    private int mInitTop;

    public SwipeItemView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public SwipeItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);

    }

    public SwipeItemView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);

    }

    public void setCanSwipe(boolean canSwipe) {
        this.canSwipe = canSwipe;
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() == 2) {
            hideView = getChildAt(0);
            dragView = getChildAt(1);
            hideView.post(new Runnable() {
                @Override
                public void run() {
                    mHorizontalDragRange = hideView.getWidth() + 16;
                }
            });
            dragView.addOnLayoutChangeListener(new OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    mInitX = (int) dragView.getX();
                    mInitTop = dragView.getTop();
                    maxLeftPointReverse = hideView.getWidth() + 16 - mInitX;
                    dragView.removeOnLayoutChangeListener(this);
                }
            });

        }
    }

    public void swipeToNormal() {
        dragView.post(new Runnable() {
            @Override
            public void run() {
                if (dragView.getX() != mInitX) {
                    boolean result = viewDragHelper.smoothSlideViewTo(dragView, mInitX, mInitTop);
                    ViewCompat.postInvalidateOnAnimation(SwipeItemView.this);
                }
            }
        });

    }

    public void toOpen() {
        dragView.addOnLayoutChangeListener(new OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (dragView.getX() != -maxLeftPointReverse) {
                    viewDragHelper.smoothSlideViewTo(dragView, -maxLeftPointReverse, mInitTop);
                }
                dragView.removeOnLayoutChangeListener(this);
            }
        });

    }

    public void toNormal() {
        dragView.addOnLayoutChangeListener(new OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (dragView.getX() != mInitX) {
                    viewDragHelper.smoothSlideViewTo(dragView, mInitX, mInitTop);

                }
                dragView.removeOnLayoutChangeListener(this);
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (canSwipe) {
            viewDragHelper.processTouchEvent(ev);
            return true;
        } else {
            return super.onTouchEvent(ev);
        }

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (SwipeItemView.activeItemView != null && SwipeItemView.activeItemView != this){
            SwipeItemView.activeItemView.swipeToNormal();
            SwipeItemView.activeItemView = null;
            return true;
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = ev.getRawX();
                downY = ev.getRawY();

                break;
            case MotionEvent.ACTION_MOVE:
                float disX = ev.getRawX() - downX;
                float disY = ev.getRawY() - downY;
                ViewParent viewGroup = getParent();

                if (Math.abs(disX) > Math.abs(disY)) {
                    viewGroup.requestDisallowInterceptTouchEvent(true);//水平滑动时请求recyclerView不要拦截事件
                } else {
                    viewGroup.requestDisallowInterceptTouchEvent(false);
                }
                break;

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                viewDragHelper.cancel();
                break;
        }
        if (canSwipe) {
            return viewDragHelper.shouldInterceptTouchEvent(ev);
        } else {
            return super.onInterceptTouchEvent(ev);
        }

    }


    private void init(Context context) {
        callback = new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return child == dragView;
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {

                mLeft = 0;
                if (left < 0 && Math.abs(left) > maxLeftPointReverse) { //限定向左最大到达位置
                    mLeft = -maxLeftPointReverse;
                    return mLeft;
                }
                if (left > mInitX) { //设置向右最大位置
                    mLeft = mInitX;
                    return mLeft;
                }
                mLeft = left;

                return mLeft;
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                return mInitTop;
            }

            @Override
            public void onEdgeTouched(int edgeFlags, int pointerId) {
                super.onEdgeTouched(edgeFlags, pointerId);
            }

            @Override
            public int getViewHorizontalDragRange(View child) {
                return mHorizontalDragRange;
            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                super.onViewReleased(releasedChild, xvel, yvel);
                if (Math.abs(mInitX - mLeft) >= mHorizontalDragRange / 3) {//open
                    if (viewDragHelper.settleCapturedViewAt(-maxLeftPointReverse, mInitTop)) {
                        ViewCompat.postInvalidateOnAnimation(SwipeItemView.this);
                    }
                    SwipeItemView.activeItemView = SwipeItemView.this;

                } else {//close
                    if (viewDragHelper.settleCapturedViewAt(mInitX, mInitTop)) {
                        ViewCompat.postInvalidateOnAnimation(SwipeItemView.this);
                    }

                    SwipeItemView.activeItemView = null;
                }
            }

        };

        viewDragHelper = ViewDragHelper.create(this, 1.0f, callback);


    }

    //暂时的解决方案，需要及时释放，后面改为在容器中缓存
    public static SwipeItemView activeItemView;


    @Override
    public void computeScroll() {
        if (viewDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }


}
