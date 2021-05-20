package com.savypan.italker.factory.presenter.contact;

import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.util.Log;

import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.CursorResult;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ITransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;
import com.savypan.italker.factory.data.IDataSource;
import com.savypan.italker.factory.data.helper.UserHelper;
import com.savypan.italker.factory.model.card.UserCard;
import com.savypan.italker.factory.model.db.AppDatabase;
import com.savypan.italker.factory.model.db.User;
import com.savypan.italker.factory.model.db.User_Table;
import com.savypan.italker.factory.persistence.Account;
import com.savypan.italker.factory.presenter.BasePresenter;
import com.savypan.italker.factory.utils.DiffUiDataCallback;

import java.util.ArrayList;
import java.util.List;

public class ContactPresenter extends BasePresenter<ContactContract.IView>
        implements ContactContract.IPresenter {

    private static final String TAG = ContactContract.class.getSimpleName();

    public ContactPresenter(ContactContract.IView view) {
        super(view);
    }


    @Override
    public void start() {
        super.start();

        //TODO 加载本地数据 - 从数据库中去查询并且加载
        SQLite.select()
                .from(User.class)
                .where(User_Table.isFollowed.eq(true))
                .and((User_Table.id.notEq(Account.getUserId())))
                .orderBy(User_Table.name, true)
                .limit(100)
                .async()
                .queryListResultCallback(new QueryTransaction.QueryResultListCallback<User>() {
                    @Override
                    public void onListQueryResult(QueryTransaction transaction, @NonNull List<User> userList) {

                        for (User user : userList) {
                            Log.e(TAG, "user name from local DB is :" + user.getName());
                        }

                        getView().getRecyclerViewAdapter().replace(userList);
                        getView().onAdapterDataChanged();
                    }
                }).execute();

        // 家在网络数据
        UserHelper.refreshContacts(new IDataSource.ICallback<List<UserCard>>() {
            @Override
            public void onDataFailed(int strId) {
                //网络失败，因为本地有数据，不管这个错误
            }

            @Override
            public void onDataLoaded(List<UserCard> userCards) {

                List<User> users = new ArrayList<>();
                for (UserCard userCard : userCards) {
                    users.add(userCard.build());
                }

                //数据加载成功 保存usercard到数据库
                DatabaseDefinition definition = FlowManager.getDatabase(AppDatabase.class);
                definition.beginTransactionAsync(new ITransaction() {
                    @Override
                    public void execute(DatabaseWrapper databaseWrapper) {
                        for (User user : users) {
                            Log.e(TAG, "user name from network search is :" + user.getName());
                        }
                        FlowManager.getModelAdapter(User.class).saveAll(users);
                    }
                }).build().execute();

                //网络上的数据是新的数据，需要直接刷新到界面
                //getView().getRecyclerViewAdapter().replace(users);

                diff(users, getView().getRecyclerViewAdapter().getItems());
            }
        });

        //TODO 问题，
        // 1 关注后，虽然存储了数据库，但是没有刷新联系人；
        // 2 如果刷新数库，或者从网络刷新，最终刷新的是全局刷新
        // 3 本地刷新和网络刷新，在添加到界面的时候会有可能冲突; 导致数据显示异常
        // 4 如何识别已经在数据库中已经有这样的数据了
    }

    private void diff(List<User> newList, List<User> oldList) {

        //进行数据对比
        DiffUtil.Callback callback = new DiffUiDataCallback<User>(oldList, newList);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);

        //对比完成以后进行数据的赋值
        getView().getRecyclerViewAdapter().replace(newList);

        //尝试刷新界面
        result.dispatchUpdatesTo(getView().getRecyclerViewAdapter());
        getView().onAdapterDataChanged();
    }
}
