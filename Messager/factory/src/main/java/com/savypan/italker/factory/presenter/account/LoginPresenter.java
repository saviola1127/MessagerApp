package com.savypan.italker.factory.presenter.account;

import com.savypan.italker.factory.presenter.BasePresenter;

public class LoginPresenter extends BasePresenter<LoginContract.IView> implements LoginContract.IPresenter {

    public LoginPresenter(LoginContract.IView view) {
        super(view);
    }

    @Override
    public void login(String phone, String password) {

    }

    @Override
    public boolean checkMobile(String phone) {
        return false;
    }
}
