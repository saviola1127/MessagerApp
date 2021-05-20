package com.savypan.italker.factory.model.db;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.savypan.italker.factory.model.IAuthor;
import com.savypan.italker.factory.utils.DiffUiDataCallback;

import java.util.Date;
import java.util.Objects;

@Table(database = AppDatabase.class)
public class User extends BaseModel implements IAuthor, DiffUiDataCallback.UiDataDiffer<User> {

    public static final int SEX_MAN = 1;
    public static final int SEX_WOMAN = 2;

    // 主键
    @PrimaryKey
    private String id;

    @Column
    private String name;

    @Column
    private String phone;

    @Column
    private String portrait;

    @Column
    private String des;

    @Column
    private int sex = 0;

    @Column
    private Date modifyAt;

    @Column
    private int followers;

    @Column
    private int followings;

    @Column
    private boolean isFollowed;

    @Column
    private String alias;

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

    public Date getModifyAt() {
        return modifyAt;
    }

    public void setModifyAt(Date modifyAt) {
        this.modifyAt = modifyAt;
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

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, phone, portrait, des, sex, modifyAt, followers, followings, isFollowed, alias);
    }

    @Override
    public boolean isSame(User old) {
        return Objects.equals(id, old.getId());
    }

    @Override
    public boolean isUiContentSame(User old) {
        //主要判断头像，名字等
        return this == old || ( Objects.equals(name, old.getName())
                        && Objects.equals(portrait, old.getPortrait())
                        && Objects.equals(sex, old.getSex())
                        && Objects.equals(isFollowed, old.isFollowed()));
    }
}
