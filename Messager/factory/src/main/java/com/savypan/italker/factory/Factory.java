package com.savypan.italker.factory;

import android.app.Application;

import com.savypan.italker.common.app.CommonApplication;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Factory {

    private static final Factory factory;
    private final Executor executor;

    static {
        factory = new Factory();
    }

    private Factory() {
        executor = Executors.newFixedThreadPool(4);
    }

    public static Application getApplication() {
        return CommonApplication.getInstance();
    }

    public static void runOnAsync(Runnable runnable) {
        factory.executor.execute(runnable);
    }
}
