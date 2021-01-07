package com.library.base.widget.list;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.R;
import androidx.recyclerview.widget.RecyclerView;

import com.library.base.widget.list.adapter.BaseSuperAdapter;
import com.library.base.widget.list.adapter.BaseViewHolder;

/**
 * 下拉加载更多RecyclerView
 */
public class LoadRecyclerView extends RecyclerView {

    public LoadRecyclerView(Context context) {
        this(context, null);
    }

    public LoadRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private RecyclerViewScrollListener rvScrollListener;
    private FooterLayout mFooterLayout;
    // 加载更多
    public void enableLoadingMorePlugin(RecyclerViewScrollListener.OnLoadListener onLoadListener) {
        if (mFooterLayout != null){
            return;
        }
        this.rvScrollListener = new RecyclerViewScrollListener();
        this.addOnScrollListener(rvScrollListener);

        this.mFooterLayout = new FooterLayout(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mFooterLayout.canLoadingMore()) {
                    if (rvScrollListener != null){
                        rvScrollListener.loadingMore();
                    }
                }
            }
        });
        rvScrollListener.setOnLoadListener(onLoadListener);

        Adapter adapter = getAdapter();
        if (adapter instanceof BaseSuperAdapter && mFooterLayout != null) {
            ((BaseSuperAdapter) adapter).addFootView(mFooterLayout);
        }
    }


    public void error() {
        setLoading(true);
        if (mFooterLayout != null) {
            mFooterLayout.error();
            if (getAdapter() != null){
                getAdapter().notifyDataSetChanged();
            }
        }
    }

    // 设置是否加载更多
    public void setLoading(boolean bool) {
        if (mFooterLayout != null) {
            mFooterLayout.normal(true);
            if (getAdapter() != null){
                getAdapter().notifyDataSetChanged();
            }
        }
        if (rvScrollListener != null)
            rvScrollListener.setLoadingMore(bool);
    }

    // 设置是否加载更多
    public void setNoMore() {
        setLoading(true);
        if (mFooterLayout != null) {
            if (getAdapter() instanceof BaseSuperAdapter) {
                ((BaseSuperAdapter) getAdapter()).removeFootView(mFooterLayout);
            }
        }
    }



    public static class FooterLayout extends HeadFootView {

        @Override
        public int getType() {
            return 1023;
        }

        @Override
        public void onBindHolder(BaseViewHolder holder) {
            FooterViewHolder footerViewHolder = new FooterViewHolder(holder.getItemView(), listener);
            footerViewHolder.onBindViewHolder(mFooterStatus);
        }

        @Override
        public int getLayoutId() {
            return R.layout.common_footer;
        }

        public static final int NORMAL = 0x00;
        public static final int NO_MORE = 0x01;
        public static final int ERROR = 0x02;
        public static final int AUTO_LOADING = 0x03;

        OnClickListener listener;

        FooterLayout(OnClickListener listener) {
            this.listener = listener;
        }

        int mFooterStatus = AUTO_LOADING;

        public boolean canAutoLoadingMore() {
            return mFooterStatus == AUTO_LOADING;
        }

        public boolean canLoadingMore() {
            return mFooterStatus == NORMAL || mFooterStatus == ERROR;
        }

        public void setFooterEnabled(boolean isAuto) {
            if (isAuto) {
                mFooterStatus = AUTO_LOADING;
            } else {
                mFooterStatus = NORMAL;
            }
        }

        public void normal(boolean isAuto) {
            if (isAuto) {
                mFooterStatus = AUTO_LOADING;
            } else {
                mFooterStatus = NORMAL;
            }
        }

        public void error() {
            mFooterStatus = ERROR;
        }

        public void noMore() {
            mFooterStatus = NO_MORE;
        }


        static class FooterViewHolder extends ViewHolder {

            public void onBindViewHolder(int status) {
                switch (status) {
                    case NORMAL:
                        normal();
                        break;
                    case AUTO_LOADING:
                        loading();
                        break;
                    case NO_MORE:
                        noMore();
                        break;
                    case ERROR:
                        error();
                        break;
                }
            }


            public final ProgressBar headerProgress;
            public final TextView headerText;

            public FooterViewHolder(View view, OnClickListener listener) {
                super(view);
                headerText = view.findViewById(R.id.pull_to_refresh_footer_text);
                headerProgress = view
                        .findViewById(R.id.pull_to_refresh_footer_progress);
                if (listener != null) {
                    itemView.setOnClickListener(listener);
                }
            }

            public void loading() {
                headerText.setText(R.string.footer_loading);
                headerProgress.setVisibility(VISIBLE);
            }

            public void noMore() {
                headerText.setText(R.string.footer_complete);
                headerProgress.setVisibility(GONE);
            }

            public void normal() {
                headerText.setText(R.string.footer_click);
                headerProgress.setVisibility(GONE);
            }

            public void error() {
                headerText.setText(R.string.footer_error);
                headerProgress.setVisibility(GONE);
            }


        }
    }
}