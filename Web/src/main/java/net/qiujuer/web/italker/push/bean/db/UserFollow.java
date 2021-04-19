package net.qiujuer.web.italker.push.bean.db;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created by savypan
 * On 2021/4/13 23:40
 */
@Entity
@Table(name = "TB_USER_FLOW")
public class UserFollow {

    //primary key
    @Id
    @PrimaryKeyJoinColumn
    @GeneratedValue(generator = "uuid")
    //把uuid的生成器定义为uuid2，uuid2是常规的uuid生成器
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    //不允许为空,不允许为null
    @Column(updatable = false, nullable = false)
    private String id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "originId") //定义关联的表字段名为originId, 对应的是User.id
    private User origin; //定义发起人

    @Column(nullable = false, updatable = false, insertable = false)
    private String originId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "targetId") //定义关联的表字段名为targetId, 对应的是User.id
    private User target; //定义关注的目标

    @Column(nullable = false, updatable = false, insertable = false)
    private String targetId;


    @Column
    private String alias; //对target备注的别名

    @CreationTimestamp //定义为创建时间戳，创建时写入
    @Column(nullable = false)
    private LocalDateTime createAt = LocalDateTime.now();


    @CreationTimestamp //定义为创建时间戳，创建时写入
    @Column(nullable = false)
    private LocalDateTime updateAt = LocalDateTime.now();


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getOrigin() {
        return origin;
    }

    public void setOrigin(User origin) {
        this.origin = origin;
    }

    public String getOriginId() {
        return originId;
    }

    public void setOriginId(String originId) {
        this.originId = originId;
    }

    public User getTarget() {
        return target;
    }

    public void setTarget(User target) {
        this.target = target;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
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
}
