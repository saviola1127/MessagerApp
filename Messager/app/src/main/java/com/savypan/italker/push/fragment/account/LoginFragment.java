package com.savypan.italker.push.fragment.account;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.savypan.italker.common.app.CommonFragment;
import com.savypan.italker.common.app.PresenterFragment;
import com.savypan.italker.factory.presenter.account.LoginContract;
import com.savypan.italker.factory.presenter.account.LoginPresenter;
import com.savypan.italker.push.R;
import com.savypan.italker.push.activities.MainActivity;

import net.qiujuer.genius.ui.widget.Button;
import net.qiujuer.genius.ui.widget.Loading;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends PresenterFragment<LoginContract.IPresenter>
implements LoginContract.IView {

    private IAccountTrigger accountTrigger;

    @BindView(R.id.edit_phone)
    EditText phone;

    @BindView(R.id.edit_password)
    EditText pwd;

    @BindView(R.id.loading)
    Loading myLoading;

    @BindView(R.id.btn_submit)
    Button submit;

    @Override
    protected LoginContract.IPresenter initPresenter() {
        return new LoginPresenter(this);
    }

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

    @OnClick(R.id.txt_go_register)
    void onShowRegister() {
        accountTrigger.triggerView();
    }

    @OnClick(R.id.btn_submit)
    void onSubmit() {

        String myPhone = phone.getText().toString();
        String myPwd = pwd.getText().toString();

        myPresenter.login(myPhone, myPwd);
    }

    @Override
    public void loginSuccess() {
        //login成功，账户已经登陆成功，我们需要进行跳转到activity界面
        MainActivity.show(getContext());
        getActivity().finish();
    }

    @Override
    public void showLoading() {
        super.showLoading();

        //正在等待中… 界面不可操作
        myLoading.start();
        //控件不可以输入
        phone.setEnabled(false);
        pwd.setEnabled(false);

        //按钮不能重复点击
        submit.setEnabled(false);
    }


    @Override
    public void showError(int str) {
        super.showError(str);

        myLoading.stop();
        //恢复控件可以输入
        phone.setEnabled(true);
        pwd.setEnabled(true);

        //按钮不能重复点击
        submit.setEnabled(true);
    }
}