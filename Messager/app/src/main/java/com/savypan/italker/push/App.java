package com.savypan.italker.push;

import android.security.NetworkSecurityPolicy;

import com.savypan.italker.common.app.CommonApplication;

public class App extends CommonApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        //NetworkSecurityPolicy.getInstance().isCleartextTrafficPermitted("http://192.168.50.72:8080");
    }
}
