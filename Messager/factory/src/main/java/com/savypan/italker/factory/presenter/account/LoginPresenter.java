package com.savypan.italker.factory.presenter.account;

import android.text.TextUtils;

import com.savypan.italker.common.Common;
import com.savypan.italker.factory.R;
import com.savypan.italker.factory.data.IDataSource;
import com.savypan.italker.factory.data.helper.AccountHelper;
import com.savypan.italker.factory.model.api.account.LoginModel;
import com.savypan.italker.factory.model.db.User;
import com.savypan.italker.factory.persistence.Account;
import com.savypan.italker.factory.presenter.BasePresenter;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.util.regex.Pattern;

public class LoginPresenter extends BasePresenter<LoginContract.IView>
        implements LoginContract.IPresenter, IDataSource.ICallback<User> {

    public LoginPresenter(LoginContract.IView view) {
        super(view);
    }

    @Override
    public void login(String phone, String password) {
        start();

        LoginContract.IView view = getView();

        if (!checkMobile(phone, password)) {
            //提示用户
            view.showError(R.string.data_account_register_invalid_parameter_mobile);
        } else if (password.length() < 6) {
            //密码小于两位
            view.showError(R.string.data_account_register_invalid_parameter_password);
        } else {
            //进行网络请求
            LoginModel model = new LoginModel(phone, password, Account.getPushId());
            AccountHelper.login(model, this);
        }
    }

    @Override
    public boolean checkMobile(String phone, String password) {
        //手机号不为空并且满足相应的格式
        return !TextUtils.isEmpty(phone)
                && Pattern.matches(Common.Constant.REGEX_MOBILE, phone)
                && !TextUtils.isEmpty(password);
    }

    @Override
    public void onDataLoaded(User user) {
        LoginContract.IView view = getView();
        if (view == null) {
            return;
        }
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                view.loginSuccess();
            }
        });

    }

    @Override
    public void onDataFailed(int strId) {
        //网络请求告知注册失败
        LoginContract.IView view = getView();
        if (view == null) {
            return;
        }

        //此时是从网络回来，并不能保证是在主线程
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                view.showError(strId);
            }
        });
    }
}
