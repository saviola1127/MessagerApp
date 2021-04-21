package com.savypan.italker.factory;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.savypan.italker.common.app.CommonApplication;

import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Factory {

    private static final Factory factory;
    private final Executor executor;
    private final Gson gson;

    static {
        factory = new Factory();
    }

    private Factory() {
        executor = Executors.newFixedThreadPool(4);
        gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateDeserializer())
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
                //TODO 设置一个过滤器，数据库级别的Model不进行转换
                //.setExclusionStrategies()
                .create();
    }

    public static Application getApplication() {
        return CommonApplication.getInstance();
    }

    public static void runOnAsync(Runnable runnable) {
        factory.executor.execute(runnable);
    }

    public static Gson getGson() {
        return factory.gson;
    }
}
