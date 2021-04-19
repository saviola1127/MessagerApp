package net.qiujuer.web.italker.push.bean.card;

import com.google.gson.annotations.Expose;
import net.qiujuer.web.italker.push.bean.db.User;

import java.time.LocalDateTime;

/**
 * Created by savypan
 * On 2021/4/17 00:45
 */
public class UserCard {
    //primary key
    @Expose
    private String id;

    @Expose
    private String name;

    @Expose
    private String phone;

    @Expose
    private String portrait;

    @Expose
    private String des;

    @Expose
    private int sex = 0;

    //用户信息最后的更新时间
    @Expose
    private LocalDateTime modifyAt = LocalDateTime.now();

    //关注我的人数
    @Expose
    private int followers;

    //我关注的人数
    @Expose
    private int followings;

    //当前我有没有关注过这个人
    @Expose
    private boolean isFollowed;


    public UserCard(final User user) {
        this(user, false);
    }

    public UserCard(final User user, boolean isFollowed) {

        this.isFollowed = isFollowed;

        this.id = user.getId();
        this.name = user.getName();
        this.phone = user.getPhone();
        this.portrait = user.getPortrait();
        this.des = user.getDescription();
        this.sex = user.getSex();
        this.modifyAt = user.getUpdateAt();

        //TODO 得到关注人和粉丝的数量
        //user.getFollowers().size() 会报错 因为没有session
    }

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

    public LocalDateTime getModifyAt() {
        return modifyAt;
    }

    public void setModifyAt(LocalDateTime modifyAt) {
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

    public boolean getIsFollowed() {
        return isFollowed;
    }

    public void setIsFollowed(boolean isFollowed) {
        this.isFollowed = isFollowed;
    }
}
