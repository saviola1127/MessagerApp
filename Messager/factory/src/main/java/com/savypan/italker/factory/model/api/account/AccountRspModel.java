package com.savypan.italker.factory.model.api.account;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.savypan.italker.factory.model.db.User;

public class AccountRspModel {

    @SerializedName("user")
    @Expose
    private User user;

    @SerializedName("account")
    @Expose
    private String account;

    @SerializedName("token")
    @Expose
    private String token; //token after user finish registration

    @SerializedName("isBound")
    @Expose
    private boolean isBound; //if tag is already bound to device pushId

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isBound() {
        return isBound;
    }

    public void setBound(boolean bound) {
        isBound = bound;
    }

    @Override
    public String toString() {
        return "AccountRspModel{" +
                "user=" + user +
                ", account='" + account + '\'' +
                ", token='" + token + '\'' +
                ", isBound=" + isBound +
                '}';
    }
}
