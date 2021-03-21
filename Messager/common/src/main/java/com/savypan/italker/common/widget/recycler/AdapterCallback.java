package com.savypan.italker.common.widget.recycler;

import android.support.v7.widget.RecyclerView;

public interface AdapterCallback<T> {
    void update(T data, RecyclerAdapter.ViewHolder<T> holder);
}
