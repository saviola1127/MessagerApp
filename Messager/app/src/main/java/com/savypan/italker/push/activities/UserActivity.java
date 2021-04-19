package com.savypan.italker.push.activities;

import android.support.v4.app.Fragment;

import com.savypan.italker.common.app.CommonActivity;
import com.savypan.italker.push.R;
import com.savypan.italker.push.fragment.user.UpdateInfoFragment;

public class UserActivity extends CommonActivity {

    private Fragment fragment;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_user;
    }

    @Override
    protected void initWidget() {
        super.initWidget();

        fragment = new UpdateInfoFragment();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, fragment)
                .commit();
    }
}