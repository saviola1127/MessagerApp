package com.savypan.italker.factory.model.card;

import com.google.gson.annotations.Expose;
import com.savypan.italker.factory.model.IAuthor;
import com.savypan.italker.factory.model.db.User;
import com.savypan.italker.factory.persistence.Account;

import java.time.LocalDateTime;
import java.util.Date;

public class UserCard implements IAuthor {

    private String id;
    private String name;
    private String phone;
    private String portrait;
    private String des;
    private int sex = 0;

    private int followers;
    private int followings;
    private boolean isFollowed;

    private Date modifyAt;

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public int getFollowings() {
        return followings;
    }

    public void setFollowings(int followings) {
        this.followings = followings;
    }

    public boolean isFollowed() {
        return isFollowed;
    }

    public void setFollowed(boolean followed) {
        isFollowed = followed;
    }

    public Date getModifyAt() {
        return modifyAt;
    }

    public void setModifyAt(Date modifyAt) {
        this.modifyAt = modifyAt;
    }

    //不能被GSON框架解析使用
    private transient User user;

    public User build() {
        if(user == null) {
            User user = new User();
            user.setId(id);
            user.setName(name);
            user.setPortrait(portrait);
            user.setPhone(phone);
            user.setDes(des);
            user.setFollowed(isFollowed);
            user.setFollowers(followers);
            user.setFollowings(followings);
            user.setModifyAt(modifyAt);
            this.user = user;
        }
        return user;
    }
}
