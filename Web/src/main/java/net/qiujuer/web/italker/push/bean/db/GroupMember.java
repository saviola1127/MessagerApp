package net.qiujuer.web.italker.push.bean.db;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created by savypan
 * On 2021/4/16 21:29
 */
@Entity
@Table(name = "TB_GROUP_MEMBER")
public class GroupMember {

    public static final int PERMISSION_TYPE_NONE = 0; // 默认权限，普通成员
    public static final int PERMISSION_TYPE_ADMIN = 1;  // 管理员
    public static final int PERMISSION_TYPE_ADMIN_SU = 100; // 创建者

    public static final int NOTIFY_LEVEL_INVALID = -1; // 默认不接收消息
    public static final int NOTIFY_LEVEL_NONE = 0; // 默认通知级别
    public static final int NOTIFY_LEVEL_CLOSE = 1; // 接收消息不提示

    @Id
    @PrimaryKeyJoinColumn
    @GeneratedValue(generator = "uuid")
    //把uuid的生成器定义为uuid2，uuid2是常规的uuid生成器
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    //不允许为空,不允许为null
    @Column(updatable = false, nullable = false)
    private String id;

    @Column
    private String alias; //对target备注的别名


    @Column(nullable = false)
    private int notifyLevel;

    // 成员的权限类型
    @Column(nullable = false)
    private int permissionType = PERMISSION_TYPE_NONE;

    @CreationTimestamp //定义为创建时间戳，创建时写入
    @Column(nullable = false)
    private LocalDateTime createAt = LocalDateTime.now();


    @CreationTimestamp //定义为创建时间戳，创建时写入
    @Column(nullable = false)
    private LocalDateTime updateAt = LocalDateTime.now();


    // 成员信息对应的用户信息
    @JoinColumn(name = "userId")
    @ManyToOne(optional = false, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private User user;
    @Column(nullable = false, updatable = false, insertable = false)
    private String userId;


    // 成员信息对应的群信息
    @JoinColumn(name = "groupId")
    @ManyToOne(optional = false, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Group group;
    @Column(nullable = false, updatable = false, insertable = false)
    private String groupId;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public int getNotifyLevel() {
        return notifyLevel;
    }

    public void setNotifyLevel(int notifyLevel) {
        this.notifyLevel = notifyLevel;
    }

    public int getPermissionType() {
        return permissionType;
    }

    public void setPermissionType(int permissionType) {
        this.permissionType = permissionType;
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

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


}
