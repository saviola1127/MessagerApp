package net.qiujuer.web.italker.push.service;

import net.qiujuer.web.italker.push.bean.api.base.ResponseModel;
import net.qiujuer.web.italker.push.bean.api.message.MessageCreationModel;
import net.qiujuer.web.italker.push.bean.card.MessageCard;
import net.qiujuer.web.italker.push.bean.db.Message;
import net.qiujuer.web.italker.push.bean.db.User;
import net.qiujuer.web.italker.push.factory.MessageFactory;
import net.qiujuer.web.italker.push.factory.PushFactory;
import net.qiujuer.web.italker.push.factory.UserFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * 消息发送的入口
 * Created by savypan
 * On 2021/6/13 19:44
 */
@Path("/msg")
public class MessageService extends BaseService{

    @POST //发送一条消息到服务器
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<MessageCard> pushMessage(MessageCreationModel model) {

        if (!MessageCreationModel.check(model)) {
            return ResponseModel.buildParameterError();
        }

        User self = getSelf();

        //查询是否已经在数据中有记录
        Message message = MessageFactory.findbyId(model.getId());
        if (message != null) {
            //正常返回
            return ResponseModel.buildOk(new MessageCard(message));
        }

        if (model.getReceiverType() == Message.RECEIVE_TYPE_GROUP) {
            return pushToGroup(self, model);
        } else {
            return pushToSingleUser(self, model);
        }
    }


    //发送到人
    private ResponseModel<MessageCard> pushToSingleUser(User sender, MessageCreationModel model) {
        User receiver = UserFactory.findById(model.getReceiverId());
        if (receiver == null) {
            //没有找到接收消息的人
            return ResponseModel.buildNotFoundUserError("Can't find Receiver!!");
        }

        if (receiver.getId().equalsIgnoreCase(sender.getId())) {
            //发送者不能是同一个人
            return ResponseModel.buildCreateError(ResponseModel.ERROR_CREATE_MESSAGE);
        }

        Message message = MessageFactory.add(sender, receiver, model);

        return buildAndPushResponse(sender, message);
    }


    //发送到群
    private ResponseModel<MessageCard> pushToGroup(User sender, MessageCreationModel model) {
        //TODO

        return buildAndPushResponse(sender, null);
    }


    private ResponseModel<MessageCard> buildAndPushResponse(User sender, Message message) {
        if (message == null) {
            //存储数据库失败
            return ResponseModel.buildCreateError(ResponseModel.ERROR_CREATE_MESSAGE);
        }

        //推送消息
        PushFactory.pushNewMessage(sender, message);

        //返回数据结果
        return ResponseModel.buildOk(new MessageCard(message));
    }

}
