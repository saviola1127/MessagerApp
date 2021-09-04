package com.savypan.italker.factory.data.helper;

import android.util.Log;

import com.raizlabs.android.dbflow.sql.language.OperatorGroup;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.savypan.italker.factory.Factory;
import com.savypan.italker.factory.model.api.RspModel;
import com.savypan.italker.factory.model.api.message.MessageCreationModel;
import com.savypan.italker.factory.model.card.MessageCard;
import com.savypan.italker.factory.model.db.Group;
import com.savypan.italker.factory.model.db.Group_Table;
import com.savypan.italker.factory.model.db.Message;
import com.savypan.italker.factory.model.db.Message_Table;
import com.savypan.italker.factory.network.IRemoteService;
import com.savypan.italker.factory.network.Network;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageHelper {
    // 从本地找Group
    public static Message findFromLocal(String msgId) {
        return SQLite.select()
                .from(Message.class)
                .where(Message_Table.id.eq(msgId))
                .querySingle();
    }

    public static void push(final MessageCreationModel model) {
        Factory.runOnAsync(new Runnable() {
            @Override
            public void run() {

                //成功状态：如果是一个已经发送过的消息，则不能重新发送
                //正在发送状态：如果是一个消息正在发送，则不能重新发送
                Message message = findFromLocal(model.getId());
                if (message != null && message.getStatus() != Message.STATUS_FAILED) {
                    return;
                }

                //如果是文件类型的 (语音，图片和文件，需要上传以后才发送)

                // 发送的时候需要通知界面更新状态 Card
                MessageCard card = model.buildCard();
                Factory.getMsgCenter().dispatch(card);

                //如果实在正常情况下，直接发送消息
                IRemoteService service = Network.remoteService();
                service.pushMessage(model).enqueue(new Callback<RspModel<MessageCard>>() {
                    @Override
                    public void onResponse(Call<RspModel<MessageCard>> call, Response<RspModel<MessageCard>> response) {
                        RspModel<MessageCard> rspModel = response.body();
                        if (rspModel != null && rspModel.success()) {
                            MessageCard rspCard = rspModel.getResult();
                            if (rspCard != null) {
                                Factory.getMsgCenter().dispatch(rspCard);
                            }
                        } else {
                            //失败流程，检查是否是账户异常
                            Factory.decodeResponseCode(rspModel, null);
                            onFailure(call, null);
                        }
                    }

                    @Override
                    public void onFailure(Call<RspModel<MessageCard>> call, Throwable throwable) {
                        //通知消息发送失败，触发界面刷新logic
                        card.setStatus(Message.STATUS_FAILED);
                        Factory.getMsgCenter().dispatch(card);
                    }
                });
            }
        });
    }


    /**
     * 查询一个消息，这个消息是一个群中的最后一条消息
     *
     * @param groupId 群Id
     * @return 群中聊天的最后一条消息
     */
    public static Message findLastWithGroup(String groupId) {
        return SQLite.select()
                .from(Message.class)
                .where(Message_Table.group_id.eq(groupId))
                .orderBy(Message_Table.createAt, false) // 倒序查询
                .querySingle();
    }

    /**
     * 查询一个消息，这个消息是和一个人的最后一条聊天消息
     *
     * @param userId UserId
     * @return 聊天的最后一条消息
     */
    public static Message findLastWithUser(String userId) {
        return SQLite.select()
                .from(Message.class)
                .where(OperatorGroup.clause()
                        .and(Message_Table.sender_id.eq(userId))
                        .and(Message_Table.group_id.isNull()))
                .or(Message_Table.receiver_id.eq(userId))
                .orderBy(Message_Table.createAt, false) // 倒序查询
                .querySingle();
    }
}
