package com.library.base.widget.toast;

import android.provider.Settings;

import androidx.recyclerview.R;

import com.library.base.GlobalContext;

public class ToastSimple
{

    /**
     * 只显示文字内容的Toast
     *
     * @param text    文字内容
     * @param time    时间，单位为s
     * @return
     */
    public static void show(CharSequence text, double time)
    {
        if (time < 0){//小于0强制弹窗，不校验值时间
            recordTime = -1;
        }
        showToast(text);
    }

    static ToastViewNormal mToast;
    static long recordTime;
    static CharSequence recordContent;



    public static void showToast(CharSequence content) {
        if (GlobalContext.app() == null){
            return;
        }
        if (content == recordContent) {
            if (System.currentTimeMillis() - recordTime < 1000) {
                return;
            }
        }
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = new ToastViewNormal(GlobalContext.app());
        mToast.setContent(content);
        mToast.setBgColor(R.drawable.toast_bg_gray, 0xffffffff);
        mToast.show();
        recordContent = content;
        recordTime = System.currentTimeMillis();
    }

    public static void showYellow(String string, int i) {
        if (GlobalContext.app() == null){
            return;
        }
        if (string == recordContent) {
            if (System.currentTimeMillis() - recordTime < 1000) {
                return;
            }
        }
        if (mToast == null) {
            mToast = new ToastViewNormal(GlobalContext.app());
        }
        mToast.setContent(string);
        mToast.setBgColor(R.drawable.toast_bg_yellow, 0xffFCBB4C);
        mToast.show();
        recordContent = string;
        recordTime = System.currentTimeMillis();
    }

    public static void showBlue(String string, int i) {
        if (GlobalContext.app() == null){
            return;
        }
        if (string == recordContent) {
            if (System.currentTimeMillis() - recordTime < 1000) {
                return;
            }
        }
        if (mToast == null) {
            mToast = new ToastViewNormal(GlobalContext.app());
        }
        mToast.setContent(string);
        mToast.setBgColor(R.drawable.toast_bg_blue, 0xff1A8CEF);
        mToast.show();
        recordContent = string;
        recordTime = System.currentTimeMillis();
    }
}