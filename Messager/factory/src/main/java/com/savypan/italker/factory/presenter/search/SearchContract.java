package com.savypan.italker.factory.presenter.search;

import com.savypan.italker.factory.model.card.GroupCard;
import com.savypan.italker.factory.model.card.UserCard;
import com.savypan.italker.factory.presenter.BaseContract;

import java.util.List;

public interface SearchContract {
    interface IPresenter extends BaseContract.IPresenter {
        void search(String content);
    }

    interface IUserView extends BaseContract.IView<IPresenter> {
        void onSearchDone(List<UserCard> userCards);
    }

    interface IGroupView extends BaseContract.IView<IPresenter> {
        void onSearchDone(List<GroupCard> groupCards);
    }
}
