package com.savypan.italker.factory.presenter;

import android.support.annotation.StringRes;

import com.savypan.italker.common.widget.recycler.RecyclerAdapter;

import java.util.List;

/***
 * MVP模式中公共的契约
 */
public interface BaseContract {
    interface IView<T extends IPresenter> {
        //显示一个字符串错误
        void showError(@StringRes int str);
        //显示进度条
        void showLoading();

        void setPresenter(T presenter);
    }

    //basic presenter职责
    interface IPresenter {
        //共用的开始方法
        void start();
        //公用的销毁方法
        void destroy();
    }

    interface IRecyclerView<ViewModel, T extends IPresenter> extends IView<T> {
        //获取适配器，然后自己自住地进行刷新
        RecyclerAdapter<ViewModel> getRecyclerViewAdapter();

        //当数据有更改了的时候触发
        void onAdapterDataChanged();
    }
}
