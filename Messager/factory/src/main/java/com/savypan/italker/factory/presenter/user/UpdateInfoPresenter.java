package com.savypan.italker.factory.presenter.user;

import android.text.TextUtils;
import android.util.Log;

import com.savypan.italker.factory.Factory;
import com.savypan.italker.factory.R;
import com.savypan.italker.factory.data.IDataSource;
import com.savypan.italker.factory.data.helper.UserHelper;
import com.savypan.italker.factory.model.api.user.UserUpdateModel;
import com.savypan.italker.factory.model.db.User;
import com.savypan.italker.factory.network.UploadHelper;
import com.savypan.italker.factory.presenter.BaseContract;
import com.savypan.italker.factory.presenter.BasePresenter;
import com.savypan.italker.factory.presenter.account.LoginContract;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

public class UpdateInfoPresenter extends BasePresenter<UpdateInfoContract.IView>
implements UpdateInfoContract.IPresenter, IDataSource.ICallback {
    public UpdateInfoPresenter(UpdateInfoContract.IView view) {
        super(view);
    }

    @Override
    public void start() {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void update(String photo, String desc, boolean isMan) {
        start();

        UpdateInfoContract.IView view = getView();

        if (TextUtils.isEmpty(photo) || TextUtils.isEmpty(desc)) {
            view.showError(R.string.data_account_update_invalid_parameter);
        } else {
            //上传头像
            Factory.runOnAsync(new Runnable() {
                @Override
                public void run() {
                    String url = UploadHelper.uploadPortrait(photo);
                    Log.i("SAVY", "OSS Url:" + url);

                    if (TextUtils.isEmpty(url)) {
                        //上传失败
                        view.showError(R.string.data_upload_error);
                    } else {
                        UserUpdateModel model = new UserUpdateModel("", url, desc, isMan? User.SEX_MAN :User.SEX_WOMAN);
                        UserHelper.update(model, UpdateInfoPresenter.this);
                    }
                }
            });
        }
    }

    @Override
    public void onDataLoaded(Object o) {
        UpdateInfoContract.IView view = getView();
        if (view == null) {
            return;
        }
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                view.updateSuccess();
            }
        });
    }

    @Override
    public void onDataFailed(int strId) {
        UpdateInfoContract.IView view = getView();
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
