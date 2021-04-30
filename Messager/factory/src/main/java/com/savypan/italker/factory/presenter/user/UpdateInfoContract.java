package com.savypan.italker.factory.presenter.user;

import com.savypan.italker.factory.presenter.BaseContract;

public interface UpdateInfoContract {
    interface IView extends BaseContract.IView<IPresenter> {
        void updateSuccess();
    }


    interface IPresenter extends BaseContract.IPresenter {
        void update(String photo, String desc, boolean isMan);
    }
}
