package com.savypan.italker.factory.data.message;

import android.support.annotation.NonNull;

import com.raizlabs.android.dbflow.sql.language.OperatorGroup;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;
import com.savypan.italker.factory.data.BaseDBRepository;
import com.savypan.italker.factory.model.db.Message;
import com.savypan.italker.factory.model.db.Message_Table;

import java.util.Collections;
import java.util.List;

/***
 * 跟某人聊天的时候的聊天记录列表
 * 关注的内容要么是我发给这个人的，要么是这个人发给我的
 */
public class MessageRepository extends BaseDBRepository<Message>
        implements MessageDataSource {

    private String receiverId; //聊天的对象Id

    public MessageRepository(String receiverId) {
        super();
        this.receiverId = receiverId;
    }


    @Override
    public void load(SuccessCallback<List<Message>> callback) {
        super.load(callback);
        SQLite.select()
                .from(Message.class)
                .where(OperatorGroup.clause().and(Message_Table.sender_id.eq(receiverId))
                        .and(Message_Table.group_id.isNull()))
                .or(Message_Table.receiver_id.eq(receiverId))
                .orderBy(Message_Table.createAt, false)
                .limit(30)
                .async()
                .queryListResultCallback(this)
                .execute();
    }


    @Override
    protected boolean isQualifiedUser(Message data) {
        //处理合理接收者的判断逻辑
        // 1. 非群聊消息的情况下，发送消息给我的人是我发送消息的接收者
        // 2. 如果消息的接收者不为空，那一定是发送给某个人的，这个人只能是我或者某个人
        // 3. 如果这个"某个人"就是receiverId，那么就是我需要关注的信息
        return (receiverId.equalsIgnoreCase(data.getSender().getId())
                && data.getGroup() == null)
                ||
                (data.getReceiver() != null
                && data.getReceiver().getId().equalsIgnoreCase(receiverId));
    }


    @Override
    public void onListQueryResult(QueryTransaction transaction, @NonNull List<Message> tResult) {
        //倒序操作
        Collections.reverse(tResult);
        super.onListQueryResult(transaction, tResult);
    }
}
