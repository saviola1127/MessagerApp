package com.savypan.italker.factory.presenter.contact;

import com.savypan.italker.factory.model.card.UserCard;
import com.savypan.italker.factory.presenter.BaseContract;

public interface FollowingContract {
    interface IPresenter extends BaseContract.IPresenter {
        void follow(String id);
    }

    interface IView extends BaseContract.IView<IPresenter> {
        void onFollowSuccess(UserCard card);
    }
}
