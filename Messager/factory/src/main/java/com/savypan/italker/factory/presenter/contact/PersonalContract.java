package com.savypan.italker.factory.presenter.contact;

import com.savypan.italker.factory.model.IAuthor;
import com.savypan.italker.factory.model.db.User;
import com.savypan.italker.factory.presenter.BaseContract;
import com.savypan.italker.factory.presenter.BasePresenter;

public interface PersonalContract {
    interface IPresenter extends BaseContract.IPresenter {
        User getUser(); //获取用户信息
    }

    interface IView extends BaseContract.IView<IPresenter> {
        String getUserId();
        void onLoadingDone(User user); //加载数据完成
        void updateHellowStatus(boolean isAllowed);
        void updateFollowingStatus(boolean isFollowing);
    }
}
