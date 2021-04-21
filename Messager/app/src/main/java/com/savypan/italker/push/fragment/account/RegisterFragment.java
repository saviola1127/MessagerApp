package com.savypan.italker.push.fragment.account;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.widget.EditText;

import com.savypan.italker.common.app.PresenterFragment;
import com.savypan.italker.factory.presenter.account.RegisterContract;
import com.savypan.italker.factory.presenter.account.RegisterPresenter;
import com.savypan.italker.push.R;
import com.savypan.italker.push.activities.MainActivity;

import net.qiujuer.genius.ui.widget.Button;
import net.qiujuer.genius.ui.widget.Loading;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 注册的界面
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends PresenterFragment<RegisterContract.IPresenter> implements RegisterContract.IView {

    private IAccountTrigger accountTrigger;

    @BindView(R.id.et_phone)
    EditText phone;

    @BindView(R.id.et_name)
    EditText name;

    @BindView(R.id.et_password)
    EditText pwd;

    @BindView(R.id.loading)
    Loading myLoading;

    @BindView(R.id.btn_submit)
    Button submit;

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
        //注册成功，账户已经登陆成功，我们需要进行跳转到activity界面
        MainActivity.show(getContext());
        getActivity().finish();
    }

    @OnClick(R.id.tv_gotologin)
    void onShowLogin() {
        accountTrigger.triggerView();
    }

    @OnClick(R.id.btn_submit)
    void onSubmit() {

        String myPhone = phone.getText().toString();
        String myName = name.getText().toString();
        String myPwd = pwd.getText().toString();

        myPresenter.register(myPhone, myName, myPwd);
    }


    @Override
    public void showLoading() {
        super.showLoading();

        //正在等待中… 界面不可操作
        myLoading.start();
        //控件不可以输入
        phone.setEnabled(false);
        name.setEnabled(false);
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
        name.setEnabled(true);
        pwd.setEnabled(true);

        //按钮不能重复点击
        submit.setEnabled(true);
    }
}