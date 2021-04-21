package com.savypan.italker.factory.model.db;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class User {

    public static final int SEX_MAN = 1;
    public static final int SEX_WOMAN = 2;

    // 主键
    //@PrimaryKey
    @SerializedName("id")
    @Expose
    private String id;

    //@Column
    @SerializedName("name")
    @Expose
    private String name;
    //@Column
    @SerializedName("phone")
    @Expose
    private String phone;
    //@Column
    @SerializedName("portrait")
    @Expose
    private String portrait;
    //@Column
    @SerializedName("des")
    @Expose
    private String des;
    //@Column
    @SerializedName("sex")
    @Expose
    private int sex = 0;

    @SerializedName("modifyAt")
    @Expose
    private String modifyAt;
    @SerializedName("followers")
    @Expose
    private int followers;
    @SerializedName("followings")
    @Expose
    private int followings;
    @SerializedName("isFollowed")
    @Expose
    private boolean isFollowed;
}
