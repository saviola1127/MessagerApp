package com.savypan.italker.factory.presenter.account;

import android.support.annotation.StringRes;

import com.savypan.italker.factory.presenter.BaseContract;

public interface LoginContract {
    interface IView extends BaseContract.IView<IPresenter> {
        //login成功
        void loginSuccess();
        //显示一个字符串错误
        void showError(@StringRes int str);
        //显示进度条
        void showLoading();

        //void setPresenter(Login.IPresenter presenter);
    }

    interface IPresenter extends BaseContract.IPresenter {
        //注册成功
        void login(String phone, String password);
        //检查手机号是否正确
        boolean checkMobile(String phone);
    }
}
