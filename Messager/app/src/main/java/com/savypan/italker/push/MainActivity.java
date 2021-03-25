package com.savypan.italker.push;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;
import com.savypan.italker.common.app.CommonActivity;
import com.savypan.italker.common.widget.PortraitView;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends CommonActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.appBar)
    View appBar;

    @BindView(R.id.icon_port)
    PortraitView portraitView;

    @BindView(R.id.txt_title)
    TextView textView;

    @BindView(R.id.lay_content)
    FrameLayout layout;

    @BindView(R.id.navigation)
    BottomNavigationView navigationView;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }


    @Override
    protected void initData() {
        super.initData();

    }


    @Override
    protected void initWidget() {
        super.initWidget();

        navigationView.setOnNavigationItemSelectedListener(this);

        Glide.with(this)
                .load(R.drawable.bg_src_morning)
                .centerCrop()
                .into(new ViewTarget<View, GlideDrawable>(appBar) {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        this.view.setBackground(resource.getCurrent());
                    }
                });
    }


    @OnClick(R.id.iv_search)
    void onSearchMenuClick(){

    }

    @OnClick(R.id.btn_add)
    void onGroupAdd() {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        textView.setText(menuItem.getTitle());
        return true;
    }
}