package com.savypan.italker.factory.data;

import java.util.List;

/***
 * 基础的数据库数据源接口定义
 * @param <T>
 */
public interface DBDataSource<T> extends IDataSource {
    //一个基本的数据源加载方法, 一般回调到presenter
    void load(SuccessCallback<List<T>> callback);
}
