package com.savypan.italker.factory.presenter.contact;

import com.savypan.italker.factory.Factory;
import com.savypan.italker.factory.data.helper.UserHelper;
import com.savypan.italker.factory.model.db.User;
import com.savypan.italker.factory.persistence.Account;
import com.savypan.italker.factory.presenter.BasePresenter;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

public class PersonalPresenter extends BasePresenter<PersonalContract.IView> implements PersonalContract.IPresenter {

    private String userId;
    private User user;

    public PersonalPresenter(PersonalContract.IView view) {
        super(view);
    }

    @Override
    public void start() {
        super.start();

        //个人界面用户数据优先从网络获取
        Factory.runOnAsync(new Runnable() {
            @Override
            public void run() {

                PersonalContract.IView view = getView();
                if (view != null) {
                    String id = getView().getUserId();
                    User user = UserHelper.searchFromNetwork(id);
                    onLoaded(view, user);
                }
            }
        });
    }


    /***
     * 界面更新数据
     * @param view
     * @param user
     */
    private void onLoaded(final PersonalContract.IView view, final User user) {
        this.user = user;

        //是否是自己
        final boolean isSelf = user.getId().equalsIgnoreCase(Account.getUserId());
        //是否已经关注
        final boolean isFollowed = isSelf || user.isFollowed();
        //是否允许跟对方说话
        final boolean isAllowedSayHello = isFollowed && !isSelf;

        //刷新UI操作
        Run.onUiAsync(new Action() {
            @Override
            public void call() {

                final PersonalContract.IView view1 = getView();
                if (view1 == null) {
                    return;
                }

                view.onLoadingDone(user);
                view.updateFollowingStatus(isFollowed);
                view.updateHellowStatus(isAllowedSayHello);
            }
        });
    }


    @Override
    public User getUser() {
        return user;
    }

}
