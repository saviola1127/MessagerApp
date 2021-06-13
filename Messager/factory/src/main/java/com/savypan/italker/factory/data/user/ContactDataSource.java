package com.savypan.italker.factory.data.user;

import com.savypan.italker.factory.data.DBDataSource;
import com.savypan.italker.factory.data.IDataSource;
import com.savypan.italker.factory.model.db.User;

import java.util.List;

/***
 * 联系人数据源
 */
public interface ContactDataSource extends DBDataSource<User> {
    // 销毁操作
    //void dispose();
}
