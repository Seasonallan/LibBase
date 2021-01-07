package com.library.base.widget.toast;

import android.content.Context;

import androidx.recyclerview.R;

import com.library.base.GlobalContext;

public class ToastUtil {
    static ToastView mToast;

    public static void showSuccess(Context context, int contentId) {
        if (context == null){
            return;
        }
        if (true){
            ToastSimple.showToast(context.getResources().getString(contentId));
            return;
        }
        showToast(context, R.mipmap.icon_success, contentId);
    }

    public static void showError(Context context, int contentId) {
        if (context == null){
            return;
        }
        if (true){
            ToastSimple.showToast(context.getResources().getString(contentId));
            return;
        }
        showToast(context, R.mipmap.icon_toast_error, contentId);
    }

    public static void showError(String content) {
        if (true){
            ToastSimple.showToast(content);
            return;
        }
        showToast(R.mipmap.icon_toast_error, content);
    }

    static long recordTime;
    static String recordContent;

    public static void showToast(Context context, int icon, int contentId) {
        showToast(icon, context.getResources().getString(contentId));
    }


    public static void showToast(int icon, String content) {
        if (content.equals(recordContent)) {
            if (System.currentTimeMillis() - recordTime < 1000) {
                return;
            }
        }
        if (mToast == null) {
            mToast = new ToastView(GlobalContext.app());
        }
        mToast.setContent(icon, content);
        mToast.show();
        recordContent = content;
        recordTime = System.currentTimeMillis();
    }

}