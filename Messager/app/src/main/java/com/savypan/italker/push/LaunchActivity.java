package com.savypan.italker.push;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.savypan.italker.common.app.CommonActivity;
import com.savypan.italker.push.activities.MainActivity;
import com.savypan.italker.push.fragment.assist.PermFragment;

public class LaunchActivity extends CommonActivity {

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_launch;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (PermFragment.hasAllPerm(this, getSupportFragmentManager())) {
            MainActivity.show(this);
            finish();
        }
    }
}