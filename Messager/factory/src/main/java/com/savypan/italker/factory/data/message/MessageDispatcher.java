package com.savypan.italker.factory.data.message;

import android.text.TextUtils;

import com.savypan.italker.factory.data.helper.DBHelper;
import com.savypan.italker.factory.data.helper.GroupHelper;
import com.savypan.italker.factory.data.helper.MessageHelper;
import com.savypan.italker.factory.data.helper.UserHelper;
import com.savypan.italker.factory.data.user.UserDispatcher;
import com.savypan.italker.factory.model.card.MessageCard;
import com.savypan.italker.factory.model.db.Group;
import com.savypan.italker.factory.model.db.Message;
import com.savypan.italker.factory.model.db.User;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MessageDispatcher implements IMessageCenter {

    private static IMessageCenter instance;

    //单线程池：处理卡片一个个的消息序列
    private final Executor executor = Executors.newSingleThreadExecutor();

    public static IMessageCenter getInstance() {
        if (instance == null) {
            synchronized (UserDispatcher.class) {
                if (instance == null) {
                    instance = new MessageDispatcher();
                }
            }
        }
        return instance;
    }


    @Override
    public void dispatch(MessageCard... cards) {
        if (cards == null || cards.length == 0) {
            return;
        }

        executor.execute(new MsgCardHandler(cards));
    }


    private class MsgCardHandler implements Runnable {

        private final MessageCard[] cards;

        private MsgCardHandler(MessageCard[] cards) {
            this.cards = cards;
        }

        @Override
        public void run() {
            List<Message> messages = new ArrayList<>();
            for (MessageCard card : cards) {
                //消息卡片基础信息过滤，错误卡片直接过滤
                if (card == null || TextUtils.isEmpty(card.getSenderId())
                        || TextUtils.isEmpty(card.getId())
                        || TextUtils.isEmpty(card.getReceiverId())
                        || TextUtils.isEmpty(card.getGroupId()))
                {
                    continue;
                }

                //消息卡片有可能是推送产生的，也有可能是自己发起的
                //推送的消息代表服务器端一定有这个消息记录的，我们可以查询到，本地有可能有，也有可能没有
                //如果是自己发起的消息，服务器端是没有记录的，先存储本地，再通过网络发送消息出去
                // 发送消息流程：写消息 - 存储本地 - 发送网络 - 网络返回 - 刷新本地状态
                Message message = MessageHelper.findFromLocal(card.getId());
                if (message != null) {

                    // 消息本身在发送以后就不再变化了，如果收到了消息而且本地有这个消息，
                    // 同时本地显示已经完成的话，则不必处理，
                    // 因为此时的回来的消息和本地一定是相同的

                    //如果本地消息显示 已经完成不作处理
                    if (message.getStatus() == Message.STATUS_DONE) {
                        continue;
                    }
                    //如果服务端显示完成，则更新时间
                    if (card.getStatus() == Message.STATUS_DONE) {
                        //代表网络发送成功，此时需要修改时间为服务器的时间
                        message.setCreateAt(card.getCreateAt());

                        //如果没有进入判断，则代表这个消息是发送失败了，
                        // 则重新进行数据库更新
                    }

                    //更新一些会变化的内容
                    message.setContent(card.getContent());
                    message.setAttach(card.getAttach());
                    message.setStatus(card.getStatus());

                } else {
                    //没有找到本地消息，初次在数据库存储
                    User sender = UserHelper.searchFromLocal(card.getSenderId());
                    User receiver = null;
                    Group group = null;

                    if (!TextUtils.isEmpty(card.getReceiverId())) {
                        receiver = UserHelper.searchFromLocal(card.getReceiverId());
                    } else if (!TextUtils.isEmpty(card.getGroupId())) {
                        group = GroupHelper.findFromLocal(card.getGroupId());
                    }
                    if (receiver == null && group == null) {
                        continue;
                    }

                    message = card.build(sender, receiver, group);
                }
                messages.add(message);
            }
            if (messages.size() > 0) {
                DBHelper.save(Message.class, messages.toArray(new Message[0]));
            }
        }
    }
}
