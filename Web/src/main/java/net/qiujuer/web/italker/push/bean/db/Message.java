package net.qiujuer.web.italker.push.bean.db;

import net.qiujuer.web.italker.push.bean.api.message.MessageCreationModel;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created by savypan
 * On 2021/4/14 23:57
 */
@Entity
@Table(name = "TB_MESSAGE")
public class Message {

    public static final int RECEIVE_TYPE_NONE = 1; //人
    public static final int RECEIVE_TYPE_GROUP = 2; //群

    public static final int TYPE_STRING = 1;
    public static final int TYPE_PIC = 2;
    public static final int TYPE_FILE = 3;
    public static final int TYPE_AUDIO = 4;

    @Id
    @PrimaryKeyJoinColumn
    //这里不自动生成UUID，ID由代码端写入，由客户端负责生成
    //@GeneratedValue(generator = "uuid")
    //把uuid的生成器定义为uuid2，uuid2是常规的uuid生成器
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    //不允许为空,不允许为null
    @Column(updatable = false, nullable = false)
    private String id;

    //内容不允许为空，类型为text
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column
    private String attach;

    //消息类型
    @Column(nullable = false)
    private int type;

    //多个消息对应一个发送者
    @ManyToOne(optional = false)
    @JoinColumn(name = "senderId")
    private User sender;
    @Column(nullable = false, insertable = false, updatable = false)
    private String senderId;


    @ManyToOne
    @JoinColumn(name = "receiverId")
    private User receiver;
    @Column(nullable = false, insertable = false, updatable = false)
    private String receiverId;


    //一个群可以接受多个消息
    @ManyToOne
    @JoinColumn(name = "groupId")
    private Group group;
    @Column(insertable = false, updatable = false)
    private String groupId;


    @CreationTimestamp //定义为创建时间戳，创建时写入
    @Column(nullable = false)
    private LocalDateTime createAt = LocalDateTime.now();


    @CreationTimestamp //定义为创建时间戳，创建时写入
    @Column(nullable = false)
    private LocalDateTime updateAt = LocalDateTime.now();

    public Message() {

    }

    //一般情况下的点对点的消息构造函数
    public Message(User sender, User receiver, MessageCreationModel model) {
        this.id = model.getId();
        this.content = model.getContent();
        this.attach = model.getAttach();
        this.type = model.getType();

        this.sender = sender;
        this.receiver = receiver;
    }


    //发送给群的消息构造函数
    public Message(User sender, Group group, MessageCreationModel model) {
        this.id = model.getId();
        this.content = model.getContent();
        this.attach = model.getAttach();
        this.type = model.getType();

        this.sender = sender;
        this.group = group;
    }


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

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public LocalDateTime getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
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

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
}
