package com.savypan.italker.factory.presenter.contact;

import com.savypan.italker.factory.data.IDataSource;
import com.savypan.italker.factory.data.helper.UserHelper;
import com.savypan.italker.factory.model.card.UserCard;
import com.savypan.italker.factory.presenter.BasePresenter;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

public class FollowingPresenter extends BasePresenter<FollowingContract.IView>
implements FollowingContract.IPresenter, IDataSource.ICallback<UserCard> {

    public FollowingPresenter(FollowingContract.IView view){
        super(view);
    }


    @Override
    public void follow(String id) {
        start();

        UserHelper.follow(id, this);
    }

    @Override
    public void onDataLoaded(UserCard card) {
        FollowingContract.IView view = getView();
        if (view != null) {
            Run.onUiAsync(new Action() {
                @Override
                public void call() {
                    view.onFollowSuccess(card);
                }
            });
        }
    }

    @Override
    public void onDataFailed(int strId) {
        FollowingContract.IView view = getView();
        if (view != null) {
            Run.onUiAsync(new Action() {
                @Override
                public void call() {
                    view.showError(strId);
                }
            });
        }
    }
}
