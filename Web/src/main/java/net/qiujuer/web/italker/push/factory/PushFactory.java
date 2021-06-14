package net.qiujuer.web.italker.push.factory;

import com.google.common.base.Strings;
import net.qiujuer.web.italker.push.bean.api.base.PushModel;
import net.qiujuer.web.italker.push.bean.card.MessageCard;
import net.qiujuer.web.italker.push.bean.db.Message;
import net.qiujuer.web.italker.push.bean.db.PushHistory;
import net.qiujuer.web.italker.push.bean.db.User;
import net.qiujuer.web.italker.push.utils.Hib;
import net.qiujuer.web.italker.push.utils.PushDispatcher;
import net.qiujuer.web.italker.push.utils.TextUtil;

/**
 * 消息存储与处理的工具类
 * Created by savypan
 * On 2021/6/13 20:49
 */
public class PushFactory {

    //发送一条消息 并在当前的发送历史记录中存储记录
    public static void pushNewMessage(User sender, Message message) {
        if (sender == null || message == null) {
            return;
        }

        //构建消息卡片用于发送
        MessageCard card = new MessageCard(message);
        //即将推送的字符串
        String entity = TextUtil.toJson(card);

        //发送者
        PushDispatcher dispatcher = new PushDispatcher();

        if (message.getGroup() == null || Strings.isNullOrEmpty(message.getGroupId())) {
            //这是一个发送给人的消息

            User receiver = UserFactory.findById(message.getReceiverId());
            if (receiver == null) {
                return;
            }

            //存储历史记录表格字段
            PushHistory history = new PushHistory();
            history.setEntityType(PushModel.ENTITY_TYPE_MESSAGE); //普通消息类型
            history.setEntity(entity);
            history.setReceiver(receiver);
            history.setReceiverPushId(receiver.getPushId()); //当前设备的推送ID

            // 构建自己的PushModel
            PushModel pushModel = new PushModel();
            // 每一条历史记录都是独立的
            pushModel.add(history.getEntityType(), history.getEntity());

            //把需要发送的数据丢给发送者
            dispatcher.add(receiver, pushModel);

            // 保存到数据库
            Hib.query(session -> {
                session.save(history);
            });

        } else {

        }

        //发送者真实提交数据发送
        dispatcher.submit();
    }
}
