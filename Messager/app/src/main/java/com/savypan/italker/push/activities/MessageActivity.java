package com.savypan.italker.push.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;

import com.savypan.italker.common.app.CommonActivity;
import com.savypan.italker.factory.model.IAuthor;
import com.savypan.italker.factory.model.db.Group;
import com.savypan.italker.factory.model.db.Message;
import com.savypan.italker.factory.model.db.Session;
import com.savypan.italker.push.R;
import com.savypan.italker.push.fragment.message.GroupChatFragment;
import com.savypan.italker.push.fragment.message.UserChatFragment;

public class MessageActivity extends CommonActivity {

    public static final String KEY_RECEIVER_ID = "KEY_RECEIVER_ID";
    public static final String KEY_RECEIVER_GROUP = "KEY_RECEIVER_GROUP";

    private String receiverId;
    private boolean isGroup;

    /***
     * 发起人聊天
     * @param context
     * @param author
     */
    public static void show(Context context, IAuthor author) {
        if (author == null || context == null
        || TextUtils.isEmpty(author.getId())) {
            return;
        }

        Intent intent = new Intent(context, MessageActivity.class);
        intent.putExtra(KEY_RECEIVER_ID, author.getId());
        intent.putExtra(KEY_RECEIVER_GROUP, false);
        context.startActivity(intent);
        //context.startActivity(new Intent(context, MessageActivity.class));
    }


    /***
     * 通过session发起聊天
     * @param context
     * @param session
     */
    public static void show(Context context, Session session) {
        if (session == null || context == null
                || TextUtils.isEmpty(session.getId())) {
            return;
        }

        Intent intent = new Intent(context, MessageActivity.class);
        intent.putExtra(KEY_RECEIVER_ID, session.getId());
        intent.putExtra(KEY_RECEIVER_GROUP, session.getReceiverType() == Message.RECEIVER_TYPE_GROUP);
        context.startActivity(intent);
    }


    /***
     * 发起群聊天
     * @param context
     * @param group
     */
    public static void show(Context context, Group group) {
        if (group == null || context == null
                || TextUtils.isEmpty(group.getId())) {
            return;
        }

        Intent intent = new Intent(context, MessageActivity.class);
        intent.putExtra(KEY_RECEIVER_ID, group.getId());
        intent.putExtra(KEY_RECEIVER_GROUP, true);
        context.startActivity(intent);
    }


    @Override
    protected boolean initArgs(Bundle bundle) {
        receiverId = bundle.getString(KEY_RECEIVER_ID);
        isGroup = bundle.getBoolean(KEY_RECEIVER_GROUP);
        return !TextUtils.isEmpty(receiverId);
    }


    @Override
    protected void initWidget() {
        super.initWidget();
        setTitle("");

        Fragment fragment;
        if (isGroup) {
            fragment = new GroupChatFragment();
        } else {
           fragment = new UserChatFragment();
        }

        //从Activity传递参数到Fragment中去
        Bundle bundle = new Bundle();
        bundle.putString(KEY_RECEIVER_ID, receiverId);
        fragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction().add(R.id.msg_container, fragment).commit();
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_message;
    }
}