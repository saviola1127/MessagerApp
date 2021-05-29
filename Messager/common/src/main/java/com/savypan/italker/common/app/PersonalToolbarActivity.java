package com.savypan.italker.common.app;

import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import com.savypan.italker.common.R;
import com.savypan.italker.factory.presenter.BaseContract;

public abstract class PersonalToolbarActivity<Presenter extends BaseContract.IPresenter> extends ToolbarActivity
implements BaseContract.IView<Presenter>{

    protected Presenter myPresenter;
    protected abstract Presenter initPresenter();


    @Override
    protected void initBeforeAll() {
        super.initBeforeAll();
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
    protected void onDestroy() {
        super.onDestroy();
        if (myPresenter != null) {
            myPresenter.destroy();
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

    protected void hideLoading() {
        if (mPlaceHolderView != null) {
            mPlaceHolderView.triggerOk();
        }
    }
}
