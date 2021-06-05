package com.savypan.italker.factory.data.user;

import com.savypan.italker.factory.data.IDataSource;
import com.savypan.italker.factory.model.db.User;

import java.util.List;

/***
 * 联系人数据源
 */
public interface ContactDataSource {
    //对数据进行加载的处理，加载成功以后返回的callback
    void load(IDataSource.SuccessCallback<List<User>> callback);

    // 销毁操作
    void dispose();
}
