package com.savypan.italker.common.widget.recycler;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.savypan.italker.common.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class RecyclerAdapter<T>
        extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder<T>>
        implements View.OnClickListener, View.OnLongClickListener, AdapterCallback<T> {

    private final List<T> mData;
    //private AdapterCallback<T> mListener;
    private AdapterListener<T> mClickListenter;


    public RecyclerAdapter(List<T> data, AdapterListener<T> listener) {
        mData = data;
        mClickListenter = listener;
    }

    public RecyclerAdapter(AdapterListener<T> listener) {
        this(new ArrayList<>(), listener);
    }

    public RecyclerAdapter() {
        this(null);
    }

    /***
     *
     * @param viewGroup parent view group
     * @param viewType, contracted as xml layout id
     * @return
     */
    @NonNull
    @Override
    public ViewHolder<T> onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View root = layoutInflater.inflate(viewType, viewGroup, false);

        //calling party needs to consider how to build this
        ViewHolder<T> holder = onCreateViewHolder(root, viewType);

        //set view tag as viewHolder, bind both
        root.setTag(R.id.tag_recycler_holder, holder);

        //set event click handling
        root.setOnClickListener(this);
        root.setOnLongClickListener(this);

        holder.mUnbinder = ButterKnife.bind(holder, root);
        holder.callback = this;
        return holder;
    }

    protected abstract ViewHolder<T> onCreateViewHolder(View view, int viewType);


    //rewrite this function to define new for us
    //after rewriting, the return is all xml layout id
    @Override
    public int getItemViewType(int position) {
        return getItemViewType(position, mData.get(position));
    }

    /***
     *
     * @param position
     * @param data
     * @return
     */
    protected abstract int getItemViewType(int position, T data);


    @Override
    public void onBindViewHolder(@NonNull ViewHolder<T> holder, int pos) {
        //get the data which needs to be bound
        T data = mData.get(pos);
        holder.bind(data);
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }


    /***
     * 返回整个数据集合
     * @return
     */
    public List<T> getItems() {
        return mData;
    }



    public void addData(T data) {
        mData.add(data);
        //notifyDataSetChanged();
        notifyItemInserted(mData.size() - 1);
    }


    public void addDataSet(T... dataList) {
        if (dataList != null && dataList.length > 0) {
            int startingPos = mData.size();
            Collections.addAll(mData, dataList);
            notifyItemRangeInserted(startingPos, dataList.length);
        }
    }

    public void add(Collection<T> dataList) {
        if (dataList != null && dataList.size() > 0) {
            int startingPos = mData.size();
            mData.addAll(dataList);
            notifyItemRangeInserted(startingPos, dataList.size());
        }
    }


    public void replace(Collection<T> dataList) {
        mData.clear();
        if (dataList == null || dataList.size() == 0) {
            return;
        } else {
            mData.addAll(dataList);
            notifyDataSetChanged();
        }
    }


    public void clear() {
        mData.clear();
        notifyDataSetChanged();
    }


    @Override
    public void update(T data, ViewHolder<T> holder) {
        int pos = holder.getAdapterPosition();
        if (pos >= 0) {
            mData.remove(pos);
            mData.add(pos, data);
            notifyItemChanged(pos);
        }
    }

    @Override
    public void onClick(View v) {
        ViewHolder viewHolder = (ViewHolder) v.getTag(R.id.tag_recycler_holder);
        if (mClickListenter != null) {
            int pos = viewHolder.getAdapterPosition();
            mClickListenter.onItemClick(viewHolder, mData.get(pos));
        }
    }

    @Override
    public boolean onLongClick(View v) {
        ViewHolder viewHolder = (ViewHolder) v.getTag(R.id.tag_recycler_holder);
        if (mClickListenter != null) {
            int pos = viewHolder.getAdapterPosition();
            mClickListenter.onItemLongClick(viewHolder, mData.get(pos));
            return true;
        }

        return false;
    }


    public void setListener(AdapterListener adapterListener) {
        mClickListenter = adapterListener;
    }

    /***
     * self defined click listener interface
     * @param <T>
     */
    public interface AdapterListener<T> {
        void onItemClick(RecyclerAdapter.ViewHolder viewHolder, T data);
        void onItemLongClick(RecyclerAdapter.ViewHolder viewHolder, T data);
    }


    /***
     * self defined ViewHolder
     * @param <T>
     */
    public static abstract class ViewHolder<T> extends RecyclerView.ViewHolder {

        private Unbinder mUnbinder;
        protected T mData;
        private AdapterCallback<T> callback;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        /***
         * used for data binding trigger other ops like refresh UI etc...
         * @param data
         */
        void bind(T data) {
            mData = data;
            onBind(data);
        }

        /***
         * callback when binding data triggered,
         * @param data
         */
        protected abstract void onBind(T data);

        public void updateData(T data) {
            if (callback != null) {
                callback.update(data, this);
            }
        }
    }

    public static abstract class AdapterListenerImpl<T> implements AdapterListener<T> {

        @Override
        public void onItemClick(ViewHolder viewHolder, T data) {

        }

        @Override
        public void onItemLongClick(ViewHolder viewHolder, T data) {

        }
    }
}
