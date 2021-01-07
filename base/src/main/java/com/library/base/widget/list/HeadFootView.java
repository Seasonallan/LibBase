package com.library.base.widget.list;

import com.library.base.widget.list.adapter.BaseViewHolder;

public abstract class HeadFootView {

    public int getType() {
        return getClass().getName().hashCode();
    }

    public abstract void onBindHolder(BaseViewHolder holder);

    public abstract int getLayoutId();
}