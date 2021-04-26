package com.savypan.italker.factory.presenter.account;

import android.text.TextUtils;
import android.view.View;

import com.savypan.italker.common.Common;
import com.savypan.italker.factory.R;
import com.savypan.italker.factory.data.IDataSource;
import com.savypan.italker.factory.data.helper.AccountHelper;
import com.savypan.italker.factory.model.api.account.RegisterModel;
import com.savypan.italker.factory.persistence.Account;
import com.savypan.italker.factory.presenter.BasePresenter;
import com.savypan.italker.factory.model.db.User;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.util.regex.Pattern;

public class RegisterPresenter extends BasePresenter<RegisterContract.IView>
        implements RegisterContract.IPresenter, IDataSource.ICallback<User> {

    public RegisterPresenter(RegisterContract.IView view) {
        super(view);
    }

    @Override
    public void register(String phone, String name, String password) {
        //在start中默认启动了loading
        start();

        RegisterContract.IView view = getView();

        if (!checkMobile(phone, password, name)) {
            //提示用户
            view.showError(R.string.data_account_register_invalid_parameter_mobile);
        } else if (name.length() < 2) {
            //姓名需要大于两位
            view.showError(R.string.data_account_register_invalid_parameter_name);
        } else if (password.length() < 6) {
            //密码小于两位
            view.showError(R.string.data_account_register_invalid_parameter_password);
        } else {
            //进行网络请求
            RegisterModel model = new RegisterModel(phone, password, name, Account.getPushId());
            AccountHelper.register(model, this);
        }
    }

    @Override
    public boolean checkMobile(String phone, String password, String name) {
        //手机号不为空并且满足相应的格式
        return !TextUtils.isEmpty(phone)
                && Pattern.matches(Common.Constant.REGEX_MOBILE, phone)
                && !TextUtils.isEmpty(password)
                && !TextUtils.isEmpty(name);
    }

    @Override
    public void onDataLoaded(User user) {
        //当网络请求成功，得到了注册好的回送一个用户信息，告诉用户，注册成功
        RegisterContract.IView view = getView();
        if (view == null) {
            return;
        }

        //此时是从网络回来，并不能保证是在主线程
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                view.registerSuccess();
            }
        });
    }

    @Override
    public void onDataFailed(int strId) {
        //网络请求告知注册失败
        RegisterContract.IView view = getView();
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
