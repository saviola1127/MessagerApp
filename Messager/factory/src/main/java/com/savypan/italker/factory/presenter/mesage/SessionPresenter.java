package com.savypan.italker.factory.presenter.mesage;

import android.support.v7.util.DiffUtil;

import com.savypan.italker.factory.data.helper.SessionHelper;
import com.savypan.italker.factory.data.message.SessionDataSource;
import com.savypan.italker.factory.data.message.SessionRepository;
import com.savypan.italker.factory.model.db.Session;
import com.savypan.italker.factory.presenter.BaseSourcePresenter;
import com.savypan.italker.factory.utils.DiffUiDataCallback;

import java.util.List;

/***
 * 最近聊天列表的presenter
 */
public class SessionPresenter extends BaseSourcePresenter<Session, Session, SessionDataSource, SessionContract.IView>
implements SessionContract.IPresenter{
    public SessionPresenter(SessionContract.IView view) {
        super(view, new SessionRepository());
    }


    @Override
    public void start() {
        super.start();

        //SessionHelper.refreshSessions();
    }

    @Override
    public void onDataLoaded(List<Session> sessions) {
        SessionContract.IView view = getView();
        if (view == null) {
            return;
        }

        //差异对比
        List<Session> oldSessions = view.getRecyclerViewAdapter().getItems();
        DiffUtil.Callback callback = new DiffUiDataCallback<>(oldSessions, sessions);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);

        //刷新界面
        refreshDataWithDiff(result, sessions);
    }
}
