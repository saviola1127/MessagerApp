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
import com.savypan.italker.push.fragment.user.UpdateInfoFragment;

import net.qiujuer.genius.ui.compat.UiCompat;

import butterknife.BindView;

public class UserActivity extends CommonActivity {

    private Fragment fragment;

    @BindView(R.id.im_bkg)
    ImageView bkg;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_user;
    }


    public static void show(Context context) {
        context.startActivity(new Intent(context, UserActivity.class));
    }

    @Override
    protected void initWidget() {
        super.initWidget();

        fragment = new UpdateInfoFragment();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.layout_container, fragment)
                .commit();

        // 初始化背景
        Glide.with(this)
                .load(R.drawable.bg_src_tianjin)
                .centerCrop() //居中剪切
                .into(new ViewTarget<ImageView, GlideDrawable>(bkg) {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        // 拿到glide的Drawable
                        Drawable drawable = resource.getCurrent();
                        // 使用适配类进行包装
                        drawable = DrawableCompat.wrap(drawable);
                        drawable.setColorFilter(UiCompat.getColor(getResources(), R.color.colorAccent),
                                PorterDuff.Mode.SCREEN); // 设置着色的效果和颜色，蒙板模式
                        // 设置给ImageView
                        this.view.setImageDrawable(drawable);
                    }
                });
    }

    // Activity中收到剪切图片成功的回调
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        fragment.onActivityResult(requestCode, resultCode, data);
    }
}