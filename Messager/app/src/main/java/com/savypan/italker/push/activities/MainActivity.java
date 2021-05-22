package com.savypan.italker.push.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnticipateInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;
import com.savypan.italker.common.app.CommonActivity;
import com.savypan.italker.common.widget.PortraitView;
import com.savypan.italker.factory.persistence.Account;
import com.savypan.italker.push.R;
import com.savypan.italker.push.fragment.assist.PermFragment;
import com.savypan.italker.push.fragment.home.ActiveFragment;
import com.savypan.italker.push.fragment.home.ContactFragment;
import com.savypan.italker.push.fragment.home.GroupFragment;
import com.savypan.italker.push.helper.NavHelper;

import net.qiujuer.genius.ui.Ui;

import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends CommonActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener,
        NavHelper.OnTabChangedListener<Integer> {

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

    @BindView(R.id.btn_add)
    FloatingActionButton btnAdd;


    private NavHelper<Integer> navHelper;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }


    public static void show(Context context) {
        context.startActivity(new Intent(context, MainActivity.class));
    }


    @Override
    protected void initData() {
        super.initData();

        Menu menu = navigationView.getMenu();
        menu.performIdentifierAction(R.id.action_home, 0);

        portraitView.setup(Glide.with(this), Account.getUser());
    }


    @Override
    protected boolean initArgs(Bundle bundle) {
        if (Account.isCompleted()) {
            return super.initArgs(bundle);
        } else {
            UserActivity.show(this);
            return false;
        }
    }

    @Override
    protected void initWidget() {
        super.initWidget();

        //verifyStoragePermissions(this);

        navHelper = new NavHelper<>(this, R.id.lay_content, getSupportFragmentManager(), this);
        navHelper.add(R.id.action_home,
                        new NavHelper.Tab<>(ActiveFragment.class, R.string.title_home))
                .add(R.id.action_group,
                        new NavHelper.Tab<>(GroupFragment.class, R.string.title_group))
                .add(R.id.action_contact,
                        new NavHelper.Tab<>(ContactFragment.class, R.string.title_contact));

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

        //PermFragment.hasAllPerm(this, getSupportFragmentManager());
    }


    @OnClick(R.id.iv_search)
    void onSearchMenuClick(){
        int type = Objects.equals(navHelper.getCurrentTab().extra, R.string.title_group)? SearchActivity.TYPE_GROUP:SearchActivity.TYPE_USER;
        SearchActivity.show(this, type);
    }

    @OnClick(R.id.btn_add)
    void onGroupAdd() {

        //浮动按钮判断当前界面是群还是联系人
        //如果是群，则打开群界面
        if (Objects.equals(navHelper.getCurrentTab().extra, R.string.title_group)) {
            //TODO 打开群界面
        } else {
            //如果是人，就进入联系人界面；
            SearchActivity.show(this, SearchActivity.TYPE_USER);
        }
        AccountActivity.show(this);
    }


    @OnClick(R.id.icon_port)
    void onPortraitClick() {
        PersonalActivity.show(this, Account.getUserId());
    }


    /***
     * handling menu switch and fragment commit
     * @param menuItem
     * @return
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Log.e("SAVY", "menuItem =>" + menuItem.toString());
        return navHelper.performClickMenu(menuItem.getItemId());
    }


    @Override
    public void onTabChanged(NavHelper.Tab<Integer> newTab, NavHelper.Tab<Integer> oldTab) {
        textView.setText(newTab.extra);

        float transY = 0;
        float rotation = 0;
        if (newTab.extra.equals(R.string.title_home)) {
            transY = Ui.dipToPx(getResources(), 76);
        } else {
            if (newTab.extra.equals(R.string.title_group)) {
                btnAdd.setImageResource(R.drawable.ic_group_add);
                rotation = -360;
            } else {
                btnAdd.setImageResource(R.drawable.ic_contact_add);
                rotation = 360;
            }
        }

        btnAdd.animate()
                .rotation(rotation)
                .translationY(transY)
                .setInterpolator(new AnticipateInterpolator(1))
                .setDuration(480)
                .start();
    }

}