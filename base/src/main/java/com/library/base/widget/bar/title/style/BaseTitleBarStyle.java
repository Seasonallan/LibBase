package com.library.base.widget.bar.title.style;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;

import com.library.base.widget.bar.title.ITitleBarStyle;


/**
 * author : Android 轮子哥
 * github : https://github.com/getActivity/TitleBar
 * time   : 2018/11/27
 * desc   : 默认主题样式基类
 */
public abstract class BaseTitleBarStyle implements ITitleBarStyle {

    private Context mContext;

    public BaseTitleBarStyle(Context context) {
        mContext = context;
    }

    protected Context getContext() {
        return mContext;
    }

    protected Drawable getDrawable(int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return getContext().getResources().getDrawable(id, null);
        } else {
            return getContext().getResources().getDrawable(id);
        }
    }

    public int getColor(int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return getContext().getResources().getColor(id, null);
        } else {
            return getContext().getResources().getColor(id);
        }
    }

    @Override
    public int getTitleBarHeight() {
        return 0;
    }

    @Override
    public float getLeftSize() {
        return 16;
    }

    @Override
    public float getTitleSize() {
        return 16;
    }

    @Override
    public float getRightSize() {
        return 16;
    }
}