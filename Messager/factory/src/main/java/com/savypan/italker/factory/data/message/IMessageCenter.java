package com.savypan.italker.factory.data.message;

import com.savypan.italker.factory.model.card.MessageCard;

/***
 * 消息卡片的消费
 */
public interface IMessageCenter {
    void dispatch(MessageCard... cards);
}
