package com.savypan.italker.factory.data.user;

import com.savypan.italker.factory.model.card.UserCard;

/***
 * 用户中心的基本定义
 */
public interface IUserCenter {
    //分发处理一堆卡片的信息并更新到数据库
    void dispatch(UserCard... cards);
}
