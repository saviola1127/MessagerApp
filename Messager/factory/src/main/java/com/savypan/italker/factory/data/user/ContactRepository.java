package com.savypan.italker.factory.data.user;

import android.support.annotation.NonNull;
import android.util.Log;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;
import com.savypan.italker.factory.data.IDataSource;
import com.savypan.italker.factory.data.helper.DBHelper;
import com.savypan.italker.factory.model.db.User;
import com.savypan.italker.factory.model.db.User_Table;
import com.savypan.italker.factory.persistence.Account;
import java.util.LinkedList;
import java.util.List;

/***
 * 联系人仓库
 */
public class ContactRepository implements ContactDataSource,
        QueryTransaction.QueryResultListCallback,
        DBHelper.IChangedListener<User> {

    private IDataSource.SuccessCallback callback;
    private final List<User> users = new LinkedList<>();
    private static final String TAG = ContactRepository.class.getSimpleName();

    @Override
    public void load(IDataSource.SuccessCallback<List<User>> callback) {
        this.callback = callback;
        // 对数据辅助工具类添加一个数据更新的监听
        DBHelper.addChangedListener(User.class, this);

        SQLite.select()
                .from(User.class)
                .where(User_Table.isFollowed.eq(true))
                .and((User_Table.id.notEq(Account.getUserId())))
                .orderBy(User_Table.name, true)
                .limit(100)
                .async()
                .queryListResultCallback(this).execute();
    }

    @Override
    public void dispose() {
        this.callback = null;
        DBHelper.removeChangedListener(User.class, this);
        users.clear();
    }

    @Override
    public void onListQueryResult(QueryTransaction transaction, @NonNull List tResult) {
        //debug list

        if (tResult.size() == 0) {
            users.clear();
            notifyDataChange();
            return;
        }

        //users.addAll(tResult);

        // 转变为一个数组
        User[] users = (User[]) tResult.toArray(new User[0]);
        onDataSaved(users);
    }

    @Override
    public void onDataSaved(User... list) {
        boolean isChanged = false;
        // 当数据库数据变更的操作
        for (User user : list) {
            // 是我关注的用户同时不是我自己
            if (isQualifiedUser(user)) {
                insertOrUpdate(user);
                isChanged = true;
            }
        }

        if (isChanged == true) {
            notifyDataChange();
        }
    }

    @Override
    public void onDataDeleted(User... list) {
        // 当数据库数据删除的操作
        boolean isRemoved = false;
        for (User user : list) {
            boolean remove = users.remove(user);
            if (remove == true) {
                isRemoved = true;
            }
        }

        if (isRemoved == true) {
            notifyDataChange();
        }
    }


    private boolean isQualifiedUser(User user) {
        return user.isFollowed() == true && !user.getId().equals(Account.getUser().getId());
    }


    //刷新数据，让View这边来调用
    private void notifyDataChange() {
        if (callback != null) {
            callback.onDataLoaded(users);
        }
    }


    private void insertOrUpdate(User user) {
        int index = indexOf(user);
        if (index >= 0) {
            // 替换操作
            replace(user, index);
        } else {
            // 插入操作
            insert(user);
        }
    }


    private void insert(User user) {
        users.add(user);
    }


    private void replace(User user, int index) {
        users.remove(index);
        users.add(index, user);
    }


    private int indexOf(User user) {
        int index = -1;
        for (User user1 : users) {
            index++;
            if (user1.isSame(user)) {
                return index;
            }
        }
        return -1;
    }
}
