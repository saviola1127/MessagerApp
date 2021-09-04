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
        if (mPlaceHolderView != null) {
            mPlaceHolderView.triggerError(str);
        } else {
            CommonApplication.showToast(str);
        }

    }

    @Override
    public void showLoading() {
        if (mPlaceHolderView != null) {
            mPlaceHolderView.triggerLoading();
        }
    }

    @Override
    public void setPresenter(Presenter presenter) {
        myPresenter = presenter;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        if (myPresenter != null) {
            myPresenter.destroy();
        }
    }
}
