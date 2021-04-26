package com.savypan.italker.push;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.Property;
import android.view.View;

import com.savypan.italker.common.app.CommonActivity;
import com.savypan.italker.factory.persistence.Account;
import com.savypan.italker.push.activities.AccountActivity;
import com.savypan.italker.push.activities.MainActivity;
import com.savypan.italker.push.fragment.assist.PermFragment;

import net.qiujuer.genius.res.Resource;
import net.qiujuer.genius.ui.compat.UiCompat;

public class LaunchActivity extends CommonActivity {

    //Drawable
    private ColorDrawable drawableBkg;
    private String TAG = LaunchActivity.class.getSimpleName();

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_launch;
    }

    @Override
    protected void initWidget() {
        super.initWidget();

        //拿到根布局
        View root = findViewById(R.id.activity_launch);
        //获取颜色
        int color = UiCompat.getColor(getResources(), R.color.colorPrimary);
        ColorDrawable drawable = new ColorDrawable(color);
        //设置背景
        root.setBackground(drawable);
        drawableBkg = drawable;
    }

    @Override
    protected void initData() {
        super.initData();

        //动画进入到50%的时候等待pushId获取
        startAnimation(0.5f, new Runnable() {
            @Override
            public void run() {
                Log.e(TAG, "initData - start waiting for pushID");
                waitForPushId();
            }
        });
    }

    /***
     * 等待个推的框架对我们的pushId设置好value
     */
    private void waitForPushId() {

        if (Account.isLogin()) {
            Log.e(TAG, "waitforpushId - starting");
            if (Account.isBound()) {
                Log.e(TAG, "waitforpushId - isBound");
                skip();
                return;
            }
        } else {
            //没有登录的情况下不允许绑定pushID
            Log.e(TAG, "waitforpushId - not login...");
            if (!TextUtils.isEmpty(Account.getPushId())) {
                Log.e(TAG, "waitforpushId - getPushId now");
                skip();
                return;
            }
        }

        getWindow().getDecorView().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.e(TAG, "waitforpushId - continue waiting");
                waitForPushId();
            }
         }, 500);
    }


    //在跳转之前把剩下的50%完成
    private void skip() {
        startAnimation(1f, new Runnable() {
            @Override
            public void run() {
                jumpOut();
            }
        });
    }


    private void jumpOut() {

        if (PermFragment.hasAllPerm(this, getSupportFragmentManager())) {

            if (Account.isLogin()) {
                Log.e(TAG, "jumpOut - already login...");
                MainActivity.show(this);
            } else {
                Log.e(TAG, "jumpOut - not login...");
                AccountActivity.show(this);
            }

            finish();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }



    /***
     * 给背景设置一个动画，动画的结束时触发
     * @param endProgress
     * @param endCallback
     */
    private void startAnimation(float endingProgress, Runnable endCallback) {
        //获取一个结束的颜色
        int finalColor = Resource.Color.WHITE;
        ArgbEvaluator argbEvaluator = new ArgbEvaluator();

        // 计算当前进度颜色
        int endColor = (int) argbEvaluator.evaluate(endingProgress, drawableBkg.getColor(), finalColor);

        //构建一个属性动画
        ValueAnimator valueAnimator = ObjectAnimator.ofObject(this, property, argbEvaluator, endColor);
        valueAnimator.setDuration(1500);
        valueAnimator.setIntValues(drawableBkg.getColor(), endColor);
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                //结束时触发
                endCallback.run();
            }
        });
        valueAnimator.start();
    }


    private Property<LaunchActivity, Object> property = new Property<LaunchActivity, Object>(Object.class, "color") {
        @Override
        public Object get(LaunchActivity object) {
            return object.drawableBkg.getColor();
        }

        @Override
        public void set(LaunchActivity object, Object value) {
            object.drawableBkg.setColor((Integer) value);
        }
    };
}