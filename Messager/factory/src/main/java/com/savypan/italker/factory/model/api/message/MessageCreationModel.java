package com.savypan.italker.factory.model.api.message;


import com.savypan.italker.factory.model.card.MessageCard;
import com.savypan.italker.factory.model.db.Message;
import com.savypan.italker.factory.persistence.Account;

import java.util.Date;
import java.util.UUID;

public class MessageCreationModel {

    //客户端生成 UUID
    private String id;
    private String content;
    private String attach;

    // 当我们需要发送一个文件的时候?
    private MessageCard card;

    //消息类型
    private int type = Message.TYPE_STR;

    //接收者，可为空（群组）
    private String receiverId;

    // 接收者的类型:群/人
    private int receiverType = Message.RECEIVER_TYPE_NONE;

    private MessageCreationModel() {
        this.id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getAttach() {
        return attach;
    }

    public int getType() {
        return type;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public int getReceiverType() {
        return receiverType;
    }

    //返回一个message Card
    public MessageCard buildCard() {
        if (card == null) {
            MessageCard card = new MessageCard();
            card.setId(id);
            card.setContent(content);
            card.setAttach(attach);
            card.setType(type);
            card.setSenderId(Account.getUserId());

            //如果是群
            if (receiverType == Message.RECEIVER_TYPE_GROUP) {
                card.setGroupId(receiverId);
            } else {
                card.setReceiverId(receiverId);
            }

            card.setStatus(Message.STATUS_CREATED);
            card.setCreateAt(new Date());
            this.card = card;
        }

        return card;
    }

    public static class Builder{
        private MessageCreationModel model;

        public Builder() {
            this.model = new MessageCreationModel();
        }

        public Builder receiver(String receiverId, int receiverType) {
            model.receiverId = receiverId;
            model.receiverType = receiverType;
            return this;
        }

        //设置内容
        public Builder content(String content, int type) {
            this.model.content = content;
            this.model.type = type;
            return this;
        }

        //设置
        public Builder attach(String attach) {
            this.model.attach = attach;
            return this;
        }

        public MessageCreationModel build() {
            return this.model;
        }
    }


    /***
     * 根据消息构建Model
     * @param message
     * @return
     */
    public static MessageCreationModel buildwithMessage(Message message) {
        MessageCreationModel model = new MessageCreationModel();
        model.id = message.getId();
        model.content = message.getContent();
        model.type = message.getType();
        model.attach = message.getAttach();

        if (message.getReceiver() != null) {
            //则是给人发送消息
            model.receiverId = message.getReceiver().getId();
            model.receiverType = Message.RECEIVER_TYPE_NONE;
        } else {
            //群消息
            model.receiverId = message.getGroup().getId();
            model.receiverType = Message.RECEIVER_TYPE_GROUP;
        }

        return model;
    }
}
