package com.savypan.italker.push.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.savypan.italker.common.app.CommonActivity;
import com.savypan.italker.factory.model.IAuthor;
import com.savypan.italker.push.R;

public class MessageActivity extends CommonActivity {


    public static void show(Context context, IAuthor author) {

    }


    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_message;
    }
}