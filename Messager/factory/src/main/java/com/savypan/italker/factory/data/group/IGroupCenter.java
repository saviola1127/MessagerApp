package com.savypan.italker.factory.data.group;

import com.savypan.italker.factory.model.card.GroupCard;
import com.savypan.italker.factory.model.card.GroupMemberCard;

/***
 * 群中心的接口定义
 */
public interface IGroupCenter {
    //群卡片的处理
    void dispatch(GroupCard... cards);

    //群成员的处理
    void dispatch(GroupMemberCard... cards);
}
