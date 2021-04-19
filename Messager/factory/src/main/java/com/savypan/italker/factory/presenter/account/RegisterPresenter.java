package com.savypan.italker.factory.presenter.account;

import com.savypan.italker.factory.presenter.BasePresenter;

public class RegisterPresenter extends BasePresenter<RegisterContract.IView>
        implements RegisterContract.IPresenter {

    public RegisterPresenter(RegisterContract.IView view) {
        super(view);
    }

    @Override
    public void register(String phone, String name, String password) {

    }

    @Override
    public boolean checkMobile(String phone) {
        return false;
    }

}
