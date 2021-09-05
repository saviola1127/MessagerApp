package com.savypan.italker.factory.data.message;

import android.support.annotation.NonNull;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;
import com.savypan.italker.factory.data.BaseDBRepository;
import com.savypan.italker.factory.model.db.Session;
import com.savypan.italker.factory.model.db.Session_Table;

import java.util.Collections;
import java.util.List;

/***
 * 最近聊天列表仓库，是对sessionDataSource的实现
 */
public class SessionRepository extends BaseDBRepository<Session> implements SessionDataSource {
    @Override
    protected boolean isQualifiedUser(Session data) {
        //所有的聊天记录都展示，默然为true
        return true;
    }


    @Override
    public void load(SuccessCallback<List<Session>> callback) {
        super.load(callback);

        //数据库查询获取session记录
        SQLite.select()
                .from(Session.class)
                .orderBy(Session_Table.modifyAt, false)
                .limit(100) //假设100为上限获取数据
                .async()
                .queryListResultCallback(this)
                .execute();
    }


    @Override
    protected void insert(Session data) {
        //复写方法，让新的数据永远加到数据集的第一个位置
        dataSet.addFirst(data);
    }


    @Override
    public void onListQueryResult(QueryTransaction transaction, @NonNull List<Session> tResult) {
        //复写该方法，进行一次反转
        Collections.reverse(tResult);
        super.onListQueryResult(transaction, tResult);
    }
}
