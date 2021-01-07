package com.library.base.widget.toast;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.R;


public class ToastView {
    private Toast mToast;
    private View mView;
    private TextView mToastContentView;
    private ImageView mToastImageView;

    @SuppressLint("InflateParams")
    public ToastView(Context context) {
        mToast = new Toast(context);
        mView = LayoutInflater.from(context).inflate(R.layout.inc_common_toast, null);

        mToastContentView = mView.findViewById(R.id.toast_tv_desc);
        mToastImageView = mView.findViewById(R.id.toast_iv);

        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.setView(mView);
    }


    /**
     * 显示提示弹窗
     *
     * @param id
     * @param txt
     */
    public void setContent(int id, int txt) {
        mView.setVisibility(View.VISIBLE);
        mToastImageView.setVisibility(View.VISIBLE);
        mToastImageView.setImageResource(id);
        mToastContentView.setText(txt);

    }

    /**
     * 显示提示弹窗
     *
     * @param id
     * @param txt
     */
    public void setContent(int id, String txt) {
        mView.setVisibility(View.VISIBLE);
        mToastImageView.setVisibility(View.VISIBLE);
        mToastImageView.setImageResource(id);
        mToastContentView.setText(txt);

    }

    /**
     * 显示
     */
    public void show(){
        mToast.show();
    }

}
