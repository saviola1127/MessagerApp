package com.savypan.italker.factory.data.message;

import com.savypan.italker.factory.data.DBDataSource;
import com.savypan.italker.factory.model.db.Message;

/***
 * 消息的数据源定义
 * 它的实现是MessageRepository, 关注的对象是Message table
 */
public interface MessageDataSource extends DBDataSource<Message> {
}
