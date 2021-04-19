package com.savypan.italker.factory.presenter;

import android.support.annotation.StringRes;

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

    interface IPresenter {
        //共用的开始方法
        void start();
        //公用的销毁方法
        void destroy();
    }
}
