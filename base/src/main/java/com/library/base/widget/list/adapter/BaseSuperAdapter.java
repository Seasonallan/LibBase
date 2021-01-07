package com.library.base.widget.list.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.library.base.widget.list.HeadFootView;

import java.util.ArrayList;
import java.util.List;

/**
 * Base of QuickAdapter.
 * Created by 火龙裸 on 2020/3/13.
 */
public abstract class BaseSuperAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {
    protected Context mContext;
    protected int mLayoutResId;
    protected List<T> mList;
    protected IMultiItemViewType<T> mMultiItemViewType;
    protected LayoutInflater mLayoutInflater;

    List<HeadFootView> mHeadViewLayouts;
    List<HeadFootView> mFootViewLayouts;

    public BaseSuperAdapter(Context context, List<T> list, int layoutResId) {
        this.mContext = context;
        this.mList = list == null ? new ArrayList<T>() : new ArrayList<T>(list);
        this.mLayoutResId = layoutResId;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mHeadViewLayouts = new ArrayList();
        this.mFootViewLayouts = new ArrayList();
    }

    public BaseSuperAdapter(Context context, List<T> data, IMultiItemViewType<T> multiItemViewType) {
        this.mContext = context;
        this.mList = data == null ? new ArrayList<T>() : new ArrayList<T>(data);
        this.mMultiItemViewType = multiItemViewType;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mHeadViewLayouts = new ArrayList();
        this.mFootViewLayouts = new ArrayList();
    }

    public void addHeadView(HeadFootView headerViewLayout) {
        mHeadViewLayouts.add(headerViewLayout);
        notifyDataSetChanged();
    }


    public void addFootView(HeadFootView footView) {
        mFootViewLayouts.add(0, footView);
        notifyDataSetChanged();
    }

    public void removeFootView(HeadFootView footerView) {
        for (int i = 0; i < mFootViewLayouts.size(); i++) {
            if (mFootViewLayouts.get(i).getType() == footerView.getType()) {
                mFootViewLayouts.remove(i);
                break;
            }
        }
        notifyDataSetChanged();
    }


    @Override
    public int getItemViewType(int position) {

        for (int i = 0; i < mHeadViewLayouts.size(); i++) {
            if (i == position) {
                return mHeadViewLayouts.get(i).getType();
            }
        }

        for (int i = 0; i < mFootViewLayouts.size(); i++) {
            if (getItemCount() - 1 - i == position) {
                return mFootViewLayouts.get(i).getType();
            }
        }

        if (mMultiItemViewType != null) {
            return mMultiItemViewType.getItemViewType(position, getItem(position));
        }
        return 0;
    }

    public T getRealItem(int position) {
        return mList.get(position);
    }

    public T getItem(int position) {
        return mList.get(position - mHeadViewLayouts.size());
    }

    @Override
    public int getItemCount() {
        return (mList == null ? 0 : mList.size() ) + mHeadViewLayouts.size() + mFootViewLayouts.size();
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        for (int i = 0; i < mHeadViewLayouts.size(); i++) {
            HeadFootView holder = mHeadViewLayouts.get(i);
            if (holder.getType() == viewType) {
                return new BaseViewHolder(mLayoutInflater.inflate(holder.getLayoutId(), parent, false));
            }
        }

        for (int i = 0; i < mFootViewLayouts.size(); i++) {
            HeadFootView holder = mFootViewLayouts.get(i);
            if (holder.getType() == viewType) {
                return new BaseViewHolder(mLayoutInflater.inflate(holder.getLayoutId(), parent, false));
            }
        }
        return onCreate(parent, viewType);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        for (int i = 0; i < mHeadViewLayouts.size(); i++) {
            HeadFootView headHolder = mHeadViewLayouts.get(i);
            if (headHolder.getType() == viewType) {
                headHolder.onBindHolder(holder);
                return;
            }
        }

        for (int i = 0; i < mFootViewLayouts.size(); i++) {
            HeadFootView headHolder = mFootViewLayouts.get(i);
            if (headHolder.getType() == viewType) {
                headHolder.onBindHolder(holder);
                return;
            }
        }

        onBind(getItemViewType(position), holder, position, getItem(position));
    }

    public abstract BaseViewHolder onCreate(ViewGroup parent, int viewType);

    /**
     * Abstract method for binding view and data.
     *
     * @param viewType {@link #getItemViewType}
     * @param holder   ViewHolder
     * @param position position
     * @param item     data
     */
    public abstract void onBind(int viewType, BaseViewHolder holder, int position, T item);

    public void add(T item) {
        add(item, true);
    }


    public void addAnimation(T item) {
        if (mList == null) {
            mList = new ArrayList<T>();
        }
        mList.add(item);
        notifyItemInserted(mList.size());
    }

    public void addAnimation(List<T> item) {
        if (mList == null) {
            mList = new ArrayList<T>();
        }
        mList.addAll(item);
        notifyItemInserted(mList.size());
    }

    public void removeAnimation(T item) {
        int position = mList.indexOf(item);
        mList.remove(item);
        notifyItemRemoved(position);
    }

    public void removeAnimation(int position) {
        mList.remove(position);
        notifyItemRemoved(position);
    }

    public void add(T item, boolean isChanged) {
        if (mList == null) {
            mList = new ArrayList<T>();
        }
        mList.add(item);
        if (isChanged) {
            notifyDataSetChanged();
        }
    }

    public void add(int index, T item) {
        mList.add(index, item);
        notifyDataSetChanged();
    }

    public void refresh(List<T> items){
        if (mList == null) {
            mList = new ArrayList<T>();
        }
        mList.clear();
        mList.addAll(items);
        notifyDataSetChanged();
    }

    public void addAll(List<T> items) {
        if (items == null || items.size() == 0) {
            return;
        }
        if (mList == null) {
            mList = new ArrayList<T>();
        }
        mList.addAll(items);
        notifyDataSetChanged();
    }

    public void addAll(int position, List<T> items) {
        if (items == null || items.size() == 0) {
            return;
        }
        if (mList == null) {
            mList = new ArrayList<T>();
        }
        mList.addAll(position, items);
        notifyDataSetChanged();
    }

    public void remove(T item) {
        mList.remove(item);
        notifyDataSetChanged();
    }

    public void remove(int index) {
        mList.remove(index);
        notifyDataSetChanged();
    }

    public void remove(int index, boolean isChange) {
        mList.remove(index);
        if (isChange) {
            notifyDataSetChanged();
        }
    }

    public void set(T oldItem, T newItem) {
        set(mList.indexOf(oldItem), newItem);
    }

    public void set(int index, T item) {
        mList.set(index, item);
        notifyDataSetChanged();
    }

    public void replaceAll(List<T> items) {
        mList.clear();
        addAll(items);
    }

    public boolean contains(T item) {
        return mList.contains(item);
    }

    public void clear() {
        mList.clear();
        notifyDataSetChanged();
    }

    public List<T> getAllData() {
        return mList;
    }

    public boolean isEmpty(){
        return mList == null || mList.size() == 0;
    }


}
