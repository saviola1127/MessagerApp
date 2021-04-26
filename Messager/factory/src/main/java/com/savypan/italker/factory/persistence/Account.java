package com.savypan.italker.factory.persistence;


import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.savypan.italker.factory.Factory;
import com.savypan.italker.factory.model.api.account.AccountRspModel;
import com.savypan.italker.factory.model.db.User;
import com.savypan.italker.factory.model.db.User_Table;

public class Account {

    private static final String KEY_PUSH_ID = "KEY_PUSH_ID";
    private static final String KEY_IS_BOUND = "KEY_IS_BOUND";
    private static final String KEY_TOKEN = "KEY_TOKEN";
    private static final String KEY_USER_ID = "KEY_USER_ID";
    private static final String KEY_ACCOUNT = "KEY_ACCOUNT";
    private static final String TAG = Account.class.getSimpleName();

    private static boolean isBound = false;
    private static String token; //登录状态的token
    private static String userId;
    private static String account;

    //设备的推送ID
    private static String pushId;

    //存储到xml
    private static void save(Context context) {
        //获取数据持久化的sp
        SharedPreferences sp = context.getSharedPreferences(Account.class.getName(), Context.MODE_PRIVATE);
        sp.edit()
                .putString(KEY_PUSH_ID, pushId)
                .putBoolean(KEY_IS_BOUND, isBound)
                .putString(KEY_ACCOUNT, account)
                .putString(KEY_USER_ID, userId)
                .putString(KEY_TOKEN, token)
                .apply();

        Log.e(TAG, "saved - pushId as " + pushId);
        Log.e(TAG, "saved - isBound as " + isBound);
        Log.e(TAG, "saved - account as " + account);
        Log.e(TAG, "saved - userId as " + userId);
        Log.e(TAG, "saved - token as " + token);
    }


    public static void load(Context context) {
        SharedPreferences sp = context.getSharedPreferences(Account.class.getName(), Context.MODE_PRIVATE);

        pushId = sp.getString(KEY_PUSH_ID, "");
        isBound = sp.getBoolean(KEY_IS_BOUND, false);
        account = sp.getString(KEY_ACCOUNT, "");
        userId = sp.getString(KEY_USER_ID, "");
        token = sp.getString(KEY_TOKEN, "");

        Log.e(TAG, "loaded - pushId as " + pushId);
        Log.e(TAG, "loaded - isBound as " + isBound);
        Log.e(TAG, "loaded - account as " + account);
        Log.e(TAG, "loaded - userId as " + userId);
        Log.e(TAG, "loaded - token as " + token);
    }


    /***
     * 是否已经完善了用户信息
     * @return
     */
    public static boolean isCompleted() {
        //TODO
        return isLogin();
    }

    /***
     * 获取推送Id
     * @return
     */
    public static String getPushId() {
        return pushId;
    }

    /***
     * 设置并存储设备的ID
     * @param pushId
     */
    public static void setPushId(String pushId) {
        Account.pushId = pushId;
        Account.save(Factory.getApplication());
    }


    public static boolean isLogin() {
        return !TextUtils.isEmpty(userId) && !TextUtils.isEmpty(token);
    }


    /***
     * 是否已经绑定到了服务器
     * @return
     */
    public static boolean isBound() {
        return isBound;
    }


    public static void setBound(boolean isBound) {
        Account.isBound = isBound;
        Account.save(Factory.getApplication());
    }

    /***
     * 保存我自己的信息到XML中
     * @param
     */
    public static void login(AccountRspModel model) {
        //存储用户的token,userId 方便从数据库中查询我的信息
        token = model.getToken();
        account = model.getAccount();
        userId = model.getUser().getId();
        save(Factory.getApplication());
    }


    public static User getUser() {

        return TextUtils.isEmpty(userId)? new User():
                SQLite.select()
                        .from(User.class)
                        .where(User_Table.id.eq(userId))
                .querySingle();
    }
}
