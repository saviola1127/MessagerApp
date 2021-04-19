package com.savypan.italker.push.fragment.account;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.savypan.italker.common.app.CommonFragment;
import com.savypan.italker.push.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends CommonFragment {

    private IAccountTrigger accountTrigger;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //拿到activity的引用
        accountTrigger = (IAccountTrigger) context;
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_login;
    }


    @Override
    public void onResume() {
        super.onResume();

        //默认切换为注册界面
        accountTrigger.triggerView();
    }
}