package com.savypan.italker.factory.presenter.search;

import com.savypan.italker.factory.data.IDataSource;
import com.savypan.italker.factory.data.helper.UserHelper;
import com.savypan.italker.factory.model.card.UserCard;
import com.savypan.italker.factory.presenter.BasePresenter;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.util.List;

import retrofit2.Call;

/**
 * 搜索人的实现
 */
public class SearchUserPresenter extends BasePresenter<SearchContract.IUserView>
        implements SearchContract.IPresenter, IDataSource.ICallback<List<UserCard>> {

    private Call call;

    public SearchUserPresenter(SearchContract.IUserView view) {
        super(view);
    }

    @Override
    public void search(String content) {
        start();

        if (call != null && !call.isCanceled()) {
            //如果有上一次的请求并且没有取消，则调用取消操作
            call.cancel();
        }

        call = UserHelper.search(content, this);
    }

    @Override
    public void onDataFailed(int strId) {
        SearchContract.IUserView view = getView();
        if (view != null) {
            Run.onUiAsync(new Action() {
                @Override
                public void call() {
                    view.showError(strId);
                }
            });
        }
    }

    @Override
    public void onDataLoaded(List<UserCard> userCards) {
        //正常返回的数据处理
        SearchContract.IUserView view = getView();
        if (view != null) {
            Run.onUiAsync(new Action() {
                @Override
                public void call() {
                    view.onSearchDone(userCards);
                }
            });
        }
    }
}
