package com.library.base.widget.list.adapter;

/**
 * Interface for multiple types.
 * Created by 火龙裸 on 2017/8/21.
 */
public interface IMultiItemViewType<T>
{
    /**
     * ItemView type
     *
     * @param position current position of ViewHolder
     * @param t        model item
     * @return viewType
     */
    int getItemViewType(int position, T t);

    /**
     * Layout Res
     *
     * @param viewType {@link #getItemViewType(int, Object)}
     */
    int getLayoutId(int viewType);
}