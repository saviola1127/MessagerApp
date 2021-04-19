package com.savypan.italker.factory.presenter.account;

import android.support.annotation.StringRes;

import com.savypan.italker.factory.presenter.BaseContract;

public interface RegisterContract {
    interface IView extends BaseContract.IView<IPresenter> {
        //注册成功
        void registerSuccess();
    }

    interface IPresenter extends BaseContract.IPresenter {
        //注册成功
        void register(String phone, String name, String password);
        //检查手机号是否正确
        boolean checkMobile(String phone);
    }
}
