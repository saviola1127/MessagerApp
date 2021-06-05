package com.savypan.italker.factory.data.user;

import android.text.TextUtils;

import com.savypan.italker.factory.data.helper.DBHelper;
import com.savypan.italker.factory.model.card.UserCard;
import com.savypan.italker.factory.model.db.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class UserDispatcher implements IUserCenter {


    private static IUserCenter instance;

    //单线程池：处理卡片一个个的消息序列
    private final Executor executor = Executors.newSingleThreadExecutor();

    public static IUserCenter getInstance() {
        if (instance == null) {
            synchronized (UserDispatcher.class) {
                if (instance == null) {
                    instance = new UserDispatcher();
                }
            }
        }
        return instance;
    }

    @Override
    public void dispatch(UserCard... cards) {
        if (cards == null || cards.length == 0) {
            return;
        }

        executor.execute(new UserCardHandler(cards));
    }


    /***
     * 线程调度的执行类
     */
    private class UserCardHandler implements Runnable {

        private final UserCard[] cards;

        private UserCardHandler(UserCard[] cards) {
            this.cards = cards;
        }

        @Override
        public void run() {
            List<User> users = new ArrayList<>();
            for (UserCard card : cards) {
                if (card == null || TextUtils.isEmpty(card.getId())) {
                    continue;
                }
                users.add(card.build());
            }

            //异步保存数据库，并且分发通知，异步操作
            DBHelper.save(User.class, users.toArray(new User[0]));
        }
    }
}
