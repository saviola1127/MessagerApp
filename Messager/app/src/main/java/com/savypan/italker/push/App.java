package com.savypan.italker.push;

import com.igexin.sdk.PushManager;
import com.savypan.italker.common.app.CommonApplication;
import com.savypan.italker.factory.Factory;

public class App extends CommonApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        Factory.setup();

        PushManager.getInstance().initialize(this, AppPushService.class);
    }
}
