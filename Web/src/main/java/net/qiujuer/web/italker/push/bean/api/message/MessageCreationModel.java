package net.qiujuer.web.italker.push.bean.api.message;

import com.google.common.base.Strings;
import com.google.gson.annotations.Expose;
import net.qiujuer.web.italker.push.bean.db.Message;

/**
 * API 请求的Model格式
 * Created by savypan
 * On 2021/6/13 19:45
 */
public class MessageCreationModel {

    //客户端生成 UUID
    @Expose
    private String id;

    @Expose
    private String content;

    @Expose
    private String attach;

    //消息类型
    @Expose
    private int type;

    //发送者的ID不能为空
    @Expose
    private String senderId;

    //接收者，可为空（群组）
    @Expose
    private String receiverId;

    // 接收者的类型:群/人
    @Expose
    private int receiverType = Message.RECEIVE_TYPE_NONE;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public int getReceiverType() {
        return receiverType;
    }

    public void setReceiverType(int receiverType) {
        this.receiverType = receiverType;
    }


    public static boolean check(MessageCreationModel model) {
        // Model 不允许为null，
        // 并且只需要具有一个及其以上的参数即可
        return model != null && !(Strings.isNullOrEmpty(model.id)
                || Strings.isNullOrEmpty(model.senderId)
                || Strings.isNullOrEmpty(model.content)
                || Strings.isNullOrEmpty(model.receiverId))
                && (model.receiverType == Message.RECEIVE_TYPE_NONE
                || model.receiverType == Message.RECEIVE_TYPE_GROUP)
                && (model.type == Message.TYPE_STRING
                || model.type == Message.TYPE_AUDIO
                || model.type == Message.TYPE_FILE
                || model.type == Message.TYPE_PIC);
    }
}
