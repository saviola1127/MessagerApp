package com.savypan.italker.push.fragment.message;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.savypan.italker.push.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GroupChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GroupChatFragment extends ChatFragment {

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_group_chat;
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {

    }
}