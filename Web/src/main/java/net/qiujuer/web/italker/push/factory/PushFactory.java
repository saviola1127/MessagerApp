package net.qiujuer.web.italker.push.factory;

import com.google.common.base.Strings;
import net.qiujuer.web.italker.push.bean.api.base.PushModel;
import net.qiujuer.web.italker.push.bean.card.MessageCard;
import net.qiujuer.web.italker.push.bean.db.*;
import net.qiujuer.web.italker.push.utils.Hib;
import net.qiujuer.web.italker.push.utils.PushDispatcher;
import net.qiujuer.web.italker.push.utils.TextUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

            Group group = message.getGroup();
            if (group == null) {
                //延迟加载可能出现null的情况，需要通过ID去得到群信息
                group = GroupFactory.findById(message.getGroupId());
            }

            if (group == null) {
                return;
            }

            Set<GroupMember> members = GroupFactory.getMembers(group);
            if (members == null || members.size() == 0) {
                return;
            }

            //过滤我自己
            members = members.stream()
                    .filter(groupMember -> !groupMember.getUserId().equalsIgnoreCase(sender.getId()))
                    .collect(Collectors.toSet());

            //一个历史记录列表
            List<PushHistory> histories = new ArrayList<>();
            addGroupMembersPushModel(dispatcher, //推送消息的发送者
                    histories, //数据库要存储的列表
                    members,  //所有的成员
                    entity, //要发送的数据
                    PushModel.ENTITY_TYPE_MESSAGE);  //发送类型


            Hib.query(session -> {
                for (PushHistory history : histories) {
                    session.saveOrUpdate(history);
                }
            });
        }

        //发送者真实提交数据发送
        dispatcher.submit();
    }


    /***
     * 给群成员构建一个消息，把消息存储到数据库的历史记录中
     * 每个人每条消息都是一个记录
     * @param dispatcher
     * @param histories
     * @param members
     * @param entity
     * @param entityTypeMessage
     */
    private static void addGroupMembersPushModel(PushDispatcher dispatcher,
                                                 List<PushHistory> histories,
                                                 Set<GroupMember> members,
                                                 String entity,
                                                 int entityTypeMessage) {

        for (GroupMember member : members) {
            //无需通过ID去找用户
            User receiver = member.getUser();
            if (receiver == null) {
                return;
            }

            PushHistory history = new PushHistory();
            history.setEntityType(entityTypeMessage); //普通消息类型
            history.setEntity(entity);
            history.setReceiver(receiver);
            history.setReceiverPushId(receiver.getPushId()); //当前设备的推送ID
            histories.add(history);

            // 构建自己的PushModel
            PushModel pushModel = new PushModel();
            // 每一条历史记录都是独立的
            pushModel.add(history.getEntityType(), history.getEntity());

            //把需要发送的数据丢给发送者
            dispatcher.add(receiver, pushModel);
        }



    }
}
