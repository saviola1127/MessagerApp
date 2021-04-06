package com.savypan.italker.push.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.savypan.italker.common.app.CommonActivity;
import com.savypan.italker.push.R;
import com.savypan.italker.push.fragment.account.UpdateInfoFragment;
import com.yalantis.ucrop.UCrop;

public class AccountActivity extends CommonActivity {

    private Fragment fragment;

    public static void show(Context context){
        context.startActivity(new Intent(
                context, AccountActivity.class));
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_account;
    }

    @Override
    protected void initWidget() {
        super.initWidget();

        fragment = new UpdateInfoFragment();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, fragment)
                .commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        fragment.onActivityResult(requestCode, resultCode, data);
    }
}