package com.savypan.italker.push.fragment.message;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.savypan.italker.factory.model.db.Group;
import com.savypan.italker.factory.presenter.mesage.ChatContract;
import com.savypan.italker.push.R;

/**
 * 群聊天界面
 * Use the {@link GroupChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GroupChatFragment extends ChatFragment<Group>
implements ChatContract.IGroupView {

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_group_chat;
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {

    }

    @Override
    protected ChatContract.IPresenter initPresenter() {
        return null;
    }

    @Override
    public void onInit(Group group) {

    }
}