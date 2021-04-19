package com.savypan.italker.push.fragment.account;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.savypan.italker.common.app.CommonFragment;
import com.savypan.italker.common.app.PresenterFragment;
import com.savypan.italker.factory.presenter.account.RegisterContract;
import com.savypan.italker.factory.presenter.account.RegisterPresenter;
import com.savypan.italker.push.R;

/**
 * 注册的界面
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends PresenterFragment<RegisterContract.IPresenter> implements RegisterContract.IView {

    private IAccountTrigger accountTrigger;

    @Override
    protected RegisterContract.IPresenter initPresenter() {
        return new RegisterPresenter(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //拿到activity的引用
        accountTrigger = (IAccountTrigger) context;
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_register;
    }

    @Override
    public void registerSuccess() {

    }
}