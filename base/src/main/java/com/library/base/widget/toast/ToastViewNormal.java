package com.library.base.widget.toast;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.R;


public class ToastViewNormal {
    private Toast mToast;
    private View mView;
    private TextView mToastContentView;

    @SuppressLint("InflateParams")
    public ToastViewNormal(Context context) {
        mToast = new Toast(context);
        mView = LayoutInflater.from(context).inflate(R.layout.inc_common_toast_normal, null);

        mToastContentView = mView.findViewById(R.id.toast_tv_desc);

        mToast.setGravity(Gravity.CENTER | Gravity.BOTTOM, 0, 0);
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.setView(mView);
    }


    /**
     * 显示提示弹窗
     *
     * @param txt
     */
    public void setContent(int txt) {
        mToastContentView.setText(txt);

    }

    public void setBgColor(int bgId, int color) {
        mToastContentView.setBackgroundResource(bgId);
        mToastContentView.setTextColor(color);
    }

    /**
     * 显示提示弹窗
     *
     * @param txt
     */
    public void setContent(CharSequence txt) {
        mToastContentView.setText(txt);

    }


    /**
     * 显示
     */
    public void show() {
        mToast.show();
    }

    public void cancel() {
        mToast.cancel();
    }
}
