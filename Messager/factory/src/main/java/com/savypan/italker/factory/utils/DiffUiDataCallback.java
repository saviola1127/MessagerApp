package com.savypan.italker.factory.utils;

import android.support.v7.util.DiffUtil;

import java.util.List;

public class DiffUiDataCallback<T extends DiffUiDataCallback.UiDataDiffer> extends DiffUtil.Callback {

    //Diff操作是一个耗时的操作?
    private List<T> oldList, newList;

    public DiffUiDataCallback(List<T> oldList, List<T> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        //旧的数据
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        //新的数据
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPos, int newItemPos) {
        //两个类是否就是一个东西，比如id相等的user
        T oldItem = oldList.get(oldItemPos);
        T newItem = newList.get(newItemPos);
        return newItem.isSame(oldItem);
    }

    @Override
    public boolean areContentsTheSame(int oldItemPos, int newItemPos) {
        //进一步判断是佛同一个东西的内容是否有不同
        T oldItem = oldList.get(oldItemPos);
        T newItem = newList.get(newItemPos);
        return newItem.isUiContentSame(oldItem);
    }


    //进行比较的数据类型
    public interface UiDataDiffer<T> {
        //传递一个旧的数据，问你是否和你表示的是否是同一个数据
        boolean isSame(T old);
        //和旧的数据对比，内容是否相同
        boolean isUiContentSame(T old);
    }
}
