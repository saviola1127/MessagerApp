package com.savypan.italker.push.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;
import com.savypan.italker.common.app.CommonActivity;
import com.savypan.italker.push.R;
import com.savypan.italker.push.fragment.account.IAccountTrigger;
import com.savypan.italker.push.fragment.account.LoginFragment;
import com.savypan.italker.push.fragment.account.RegisterFragment;

import net.qiujuer.genius.ui.compat.UiCompat;

import butterknife.BindView;

public class AccountActivity extends CommonActivity implements IAccountTrigger {

    private Fragment fragment;
    private Fragment loginFragment;
    private Fragment registerFragment;

    @BindView(R.id.im_bg)
    ImageView bg;

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

        fragment = loginFragment = new LoginFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, fragment)
                .commit();

        //初始化背影
        Glide.with(this)
                .load(R.drawable.bg_src_tianjin)
                .centerCrop()
                //.into(bg);/*
                .into(new ViewTarget<ImageView, GlideDrawable>(bg) {

                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        //拿到glide的drawable
                        Drawable drawable = resource.getCurrent();
                        //使用适配类进行包装
                        drawable = DrawableCompat.wrap(drawable);
                        drawable.setColorFilter(UiCompat.getColor(getResources(), R.color.colorAccent), PorterDuff.Mode.SCREEN);

                        this.view.setImageDrawable(drawable);
                    }
                });//*/
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        fragment.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void triggerView() {
        Fragment current;
        if (fragment == loginFragment) {
            if (registerFragment == null) {
                registerFragment = new RegisterFragment();
            }

            current = registerFragment;
        } else {
            //默认情况下已经赋值，无需判断为空的状态
            current = loginFragment;
        }

        //重新赋值以后当前正在显示的fragment
        fragment = current;
        //切换显示
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, current)
                .commit();
    }
}