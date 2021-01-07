
package com.library.base.swipe;

import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;

/**
 * 侧滑关闭基类
 */
public class SwipeBackActivity extends AppCompatActivity {

    /**
     * 侧滑关闭是否开启
     *
     * @return
     */
    protected boolean isSwipeEnable() {
        return true;
    }

    private SwipeBackLayout mSwipeBackLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isSwipeEnable()) {
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getWindow().getDecorView().setBackground(null);
            mSwipeBackLayout = new SwipeBackLayout(this);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (isSwipeEnable()) {
            //
            TypedArray a = getTheme().obtainStyledAttributes(new int[]{
                    android.R.attr.windowBackground
            });
            int background = a.getResourceId(0, 0);
            a.recycle();

            ViewGroup decor = (ViewGroup) getWindow().getDecorView();
            ViewGroup decorChild = (ViewGroup) decor.getChildAt(0);
            decorChild.setBackgroundResource(background);
            decor.removeView(decorChild);

            //给内容加一层外部容器SwipeBackLayout，拦截侧滑事件
            mSwipeBackLayout.addView(decorChild);
            mSwipeBackLayout.setContentView(decorChild);
            mSwipeBackLayout.setSwipeListener(new SwipeBackLayout.SwipeListener() {
                @Override
                public void onScrollStateChange(int state, float scrollPercent) {

                }

                @Override
                public void onEdgeTouch(int edgeFlag) {
                    TranslucentUtils.convertActivityToTranslucent(SwipeBackActivity.this);
                }

                @Override
                public void onScrollOverThreshold() {

                }

                @Override
                public void onContentViewSwipedBack() {
                    if (!isFinishing()) {
                        finish();
                        overridePendingTransition(0, 0);
                    }
                }
            });
            decor.addView(mSwipeBackLayout);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSwipeBackLayout != null) {
            mSwipeBackLayout.removeSwipeListener();
        }
    }

    @Override
    public <T extends View> T findViewById(int id) {
        View v = super.findViewById(id);
        if (v == null && mSwipeBackLayout != null)
            return mSwipeBackLayout.findViewById(id);
        return (T) v;
    }


}
