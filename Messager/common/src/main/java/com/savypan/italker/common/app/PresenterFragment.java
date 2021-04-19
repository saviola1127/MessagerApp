package com.savypan.italker.common.app;

import android.content.Context;

import com.savypan.italker.factory.presenter.BaseContract;

public abstract class PresenterFragment<Presenter extends BaseContract.IPresenter> extends CommonFragment
implements BaseContract.IView<Presenter> {

    protected Presenter myPresenter;

    protected abstract Presenter initPresenter();


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        initPresenter();
    }

    @Override
    public void showError(int str) {
        CommonApplication.showToast(str);
    }

    @Override
    public void showLoading() {
        //TODO
    }

    @Override
    public void setPresenter(Presenter presenter) {
        myPresenter = presenter;
    }
}
