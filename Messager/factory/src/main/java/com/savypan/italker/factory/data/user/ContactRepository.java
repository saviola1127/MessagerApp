package com.savypan.italker.factory.data.user;

import android.support.annotation.NonNull;
import android.util.Log;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;
import com.savypan.italker.factory.data.BaseDBRepository;
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
public class ContactRepository extends BaseDBRepository<User> implements ContactDataSource {

    private IDataSource.SuccessCallback callback;
    private final List<User> users = new LinkedList<>();
    private static final String TAG = ContactRepository.class.getSimpleName();

    @Override
    public void load(IDataSource.SuccessCallback<List<User>> callback) {
        super.load(callback);
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

    // 过滤方法来识别哪些关注对象需要触发


    @Override
    protected boolean isQualifiedUser(User data) {
        return data.isFollowed() == true && !data.getId().equals(Account.getUser().getId());
    }

}
