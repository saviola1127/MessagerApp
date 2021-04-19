package net.qiujuer.web.italker.push.bean.db;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created by savypan
 * On 2021/4/16 21:09
 */
@Entity
@Table(name = "TB_GROUP")
public class Group {

    @Id
    @PrimaryKeyJoinColumn
    @GeneratedValue(generator = "uuid")
    //把uuid的生成器定义为uuid2，uuid2是常规的uuid生成器
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    //不允许为空,不允许为null
    @Column(updatable = false, nullable = false)
    private String id;


    //群名称
    @Column(nullable = false)
    private String name;


    //多个群可以被一个创建,且必须有一个创建者，加载方式为急加载
    @ManyToOne(optional = false, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "ownerId")
    private User owner;

    @Column(nullable = false, updatable = false, insertable = false)
    private String ownerId;

    //群描述
    @Column(nullable = false)
    private String description;


    @Column(nullable = false)
    private String picture;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
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
