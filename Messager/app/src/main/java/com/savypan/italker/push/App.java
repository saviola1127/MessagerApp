package com.savypan.italker.push;

import android.app.Activity;
import android.os.Bundle;

import com.igexin.sdk.PushManager;
import com.savypan.italker.common.app.CommonApplication;
import com.savypan.italker.factory.Factory;

public class App extends CommonApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        Factory.setup();

        //PushManager.getInstance().initialize(this, AppPushService.class);
        // 注册生命周期
        registerActivityLifecycleCallbacks(new PushInitializeLifecycle());
    }


    /**
     * 个推服务在部分手机上极易容易回收，可放Resumed中唤起
     */
    private class PushInitializeLifecycle implements ActivityLifecycleCallbacks {

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {
            // 推送进行初始化
            PushManager.getInstance().initialize(App.this, AppPushService.class);
            // 推送注册消息接收服务
            PushManager.getInstance().registerPushIntentService(App.this, AppMessageReceiverService.class);
        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {

        }
    }
}
