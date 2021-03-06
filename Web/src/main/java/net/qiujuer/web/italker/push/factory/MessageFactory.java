package net.qiujuer.web.italker.push.factory;

import net.qiujuer.web.italker.push.bean.api.message.MessageCreationModel;
import net.qiujuer.web.italker.push.bean.db.Group;
import net.qiujuer.web.italker.push.bean.db.Message;
import net.qiujuer.web.italker.push.bean.db.User;
import net.qiujuer.web.italker.push.utils.Hib;

/**
 * 消息数据存储的类
 * Created by savypan
 * On 2021/6/13 19:44
 */
public class MessageFactory {

    public static Message findbyId(String id) {
        return Hib.query(session -> {
            return session.get(Message.class, id);
        });
    }

    //添加一条普通消息
    public static Message add(User sender, User receiver, MessageCreationModel model) {
        Message message = new Message(sender, receiver, model);

        return save(message);
    }


    //添加一条群消息
    public static Message add(User sender, Group group, MessageCreationModel model) {
        Message message = new Message(sender, group, model);

        return save(message);
    }


    private static Message save(Message message) {
        return Hib.query(session -> {
            session.save(message);

            //写入到数据库
            session.flush();

            // 紧接着从数据库中查询出来
            session.refresh(message);
            return message;
        });
    }
}
