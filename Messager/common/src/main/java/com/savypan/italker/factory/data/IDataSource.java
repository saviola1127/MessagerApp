package com.savypan.italker.factory.data;

import android.support.annotation.StringRes;

/***
 * 数据源接口定义
 */
public interface IDataSource {

    public interface ICallback<T> extends SuccessCallback<T>, FailedCallback{

    }

    /***
     * 只关注成功的接口
     * @param <T>
     */
    interface SuccessCallback<T> {
        void onDataLoaded(T t);
    }

    /**
     * 只关注失败的接口
     * @param <T>
     */
    interface FailedCallback<T> {
        void onDataFailed(@StringRes int strId);
    }

    void dispose();
}
