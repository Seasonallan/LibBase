package com.example.base;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.library.base.GlobalContext;
import com.library.base.LogConsole;
import com.library.base.adapt.Density;
import com.library.base.language.LanguageUtil;
import com.library.base.screen.InputMethodUtils;
import com.library.base.screen.ScreenTool;
import com.library.base.swipe.SwipeBackActivity;
import com.library.base.widget.bar.title.OnTitleBarListener;
import com.library.base.widget.bar.title.TitleBar;

/**
 * activity基类
 */
public abstract class BaseActivity extends SwipeBackActivity {

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LanguageUtil.setLocale(this);
    }


    /**
     * 复制文字
     *
     * @param data
     */
    public void copy(String data) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText(null, data);
        clipboard.setPrimaryClip(clipData);
    }

    /**
     * 底部菜单栏颜色
     *
     * @return
     */
    protected int getNavigationBarColor() {
        return R.color.colorPrimary;
    }


    /**
     * 设置状态栏颜色
     */
    public void setStatusBarColor(boolean dark) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decorView = getWindow().getDecorView();
            if (decorView != null) {
                int vis = decorView.getSystemUiVisibility();
                if (dark) {
                    vis |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                } else {
                    vis &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                }
                decorView.setSystemUiVisibility(vis);
            }
        }
    }


    /**
     * 弹出输入框键盘
     *
     * @param view
     */
    public void showSoftInputFromWindow(View view) {
        InputMethodUtils.show(this, view);
    }

    /**
     * 关闭输入法
     */
    public void hideSoftInputFromWindow() {
        InputMethodUtils.hide(this);
    }

    /**
     * 设置标题栏左侧文字
     *
     * @param titleId
     */
    public void setLeftTitle(int titleId) {
        titleBar.setLeftIcon(null);
        titleBar.setLeftTitle(titleId);
    }

    /**
     * 设置标题栏右侧文字
     *
     * @param titleId
     */
    public void setRightTitle(int titleId) {
        titleBar.setRightTitle(titleId);
    }

    /**
     * 设置标题栏右侧文字颜色
     *
     * @param titleId
     */
    public void setRightColor(ColorStateList titleId) {
        titleBar.setRightColor(titleId);
    }

    /**
     * 设置标题栏右侧图片
     *
     * @param iconId
     */
    public void setRightIcon(int iconId) {
        titleBar.setRightIcon(iconId);
    }

    /**
     * 设置标题栏左侧图片
     *
     * @param iconId
     */
    public void setLeftIcon(int iconId) {
        titleBar.setLeftIcon(iconId);
    }

    /**
     * 隐藏标题栏返回按钮
     */
    public void disableTopLeftView() {
        titleBar.getLeftView().setVisibility(View.GONE);
    }

    @Override
    public void setTitle(int titleId) {
        if (blueStyle()) {
            titleBar.setLeftTitle(titleId);
            return;
        }
        titleBar.setTitle(titleId);
    }

    /**
     * 设置标题栏中间的标题内容
     *
     * @param titleId
     */
    public void setTitleCenter(int titleId) {
        titleBar.setTitle(titleId);
    }

    /**
     * 设置标题栏标题
     * 左侧 或 中间
     *
     * @param title
     */
    public void setTitle(CharSequence title) {
        if (blueStyle()) {
            titleBar.setLeftTitle(title);
            return;
        }
        titleBar.setTitle(title);
    }

    protected TitleBar titleBar;


    /**
     * 是否使用沉浸式
     *
     * @return
     */
    protected boolean transparentBar() {
        //Android 8.0.0,level 26 HuaWei 问题 java.lang.IllegalStateException: Only fullscreen activities can request orientation
        if ("huawei".equalsIgnoreCase(Build.MANUFACTURER) && Build.VERSION.SDK_INT == 26) {
            return false;
        }
        return true;
    }

    /**
     * 资产蓝色风格
     *
     * @return
     */
    protected boolean blueStyle() {
        return true;
    }

    /**
     * bind layout resource file
     */
    protected abstract int getContentViewId();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GlobalContext.init(getApplication());
        Density.setDensity(getApplication(), 375);

        //开启语言上下文切换
        LanguageUtil.setLocale(this);
        //开启自动适配density修正
        Density.enableUIAdapt(this);
        //强制设置竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //设置顶部底部菜单透明
        ScreenTool.setTransparent(this, transparentBar(), getNavigationBarColor());

        if (getContentViewId() != 0) {
            setContentView(getContentViewId());
        }

        titleBar = findViewById(R.id.TitleBar);
        if (titleBar != null) {
            titleBar.setOnTitleBarListener(new OnTitleBarListener() {
                @Override
                public void onLeftClick(View v) {
                    onTopLeftViewClicked();
                }

                @Override
                public void onTitleClick(View v) {

                }

                @Override
                public void onRightClick(View v) {
                    onTopRightViewClicked();
                }
            });

            if (transparentBar()) {
                int statusBarHeight = ScreenTool.getStatusBarHeight(this);
                if (statusBarHeight > 0) {
                    ViewGroup.LayoutParams param = titleBar.getLayoutParams();
                    if (param instanceof RelativeLayout.LayoutParams) {
                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) param;
                        params.topMargin = statusBarHeight;
                    } else {
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) param;
                        params.topMargin = statusBarHeight;
                    }
                    titleBar.requestLayout();
                }
            }

        } else {
            titleBar = new TitleBar(this);
        }
        if (blueStyle()) {
            //titleBar.setBackgroundColor(getResources().getColor(R.color.blue_bg));
            //findViewById(R.id.top_earth).setVisibility(View.VISIBLE);
        }

        initViewsAndEvents();
    }
    /**
     * init views and events here
     */
    protected abstract void initViewsAndEvents();

    /**
     * 设置顶部空位置渗透高度
     *
     * @param view
     */
    protected void setTopBar(View view) {
        int statusBarHeight = ScreenTool.getStatusBarHeight(this);
        if (statusBarHeight > 0) {
            ViewGroup.LayoutParams param = view.getLayoutParams();
            if (param instanceof RelativeLayout.LayoutParams) {
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) param;
                params.height = statusBarHeight;
            } else {
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) param;
                params.height = statusBarHeight;
            }
            view.requestLayout();
        }
    }


    /**
     * 标题左侧按钮事件
     */
    protected void onTopLeftViewClicked() {
        finish();
    }

    /**
     * 标题右侧按钮事件
     */
    protected void onTopRightViewClicked() {
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogConsole.e("onResume: " + getClass().getName());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogConsole.e("onDestroy: " + getClass().getName());
    }

    /**
     * 启动页面
     *
     * @param clazz
     */
    public void startActivity(Class<?> clazz) {
        Intent intent = new Intent(this, clazz);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }


}
