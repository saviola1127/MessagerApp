package com.savypan.italker.common.app;

import android.app.Application;
import android.os.SystemClock;
import android.support.annotation.StringRes;
import android.widget.Toast;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.io.File;

public class CommonApplication extends android.app.Application {

    private static CommonApplication app;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
    }

    /***
     * get cached folder address
     * @return
     */
    public static File getCacheDirFile() {
        return app.getCacheDir();
    }

    /***
     * 获取头像的临时存储地址
     * @return
     */
    public static File getPortraitTmpFile() {
        //得到头像目录的缓存地址
        File dir = new File(getCacheDirFile(), "portrait");
        //创建所有的对应的文件夹
        dir.mkdirs();

        File[] files = dir.listFiles();
        if (files != null && files.length > 0) {
            for (File file : files) {
                file.delete();
            }
        }

        //返回一个当前时间戳的目录文件地址
        File path = new File(dir, SystemClock.uptimeMillis() + ".jpg");
        return path.getAbsoluteFile();
    }


    /***
     * 获取音乐文件的本地存储地址
     * @return
     */
    public static File getAudioTmpFile(boolean isTmp) {
        //得到头像目录的缓存地址
        File dir = new File(getCacheDirFile(), "audio");
        //创建所有的对应的文件夹
        dir.mkdirs();

        File[] files = dir.listFiles();
        if (files != null && files.length > 0) {
            for (File file : files) {
                file.delete();
            }
        }

        //aar
        File path = new File(getCacheDirFile(), isTmp? "tmp.mp3":SystemClock.uptimeMillis() + ".mp3");
        return path.getAbsoluteFile();
    }


    public static Application getInstance() {
        return app;
    }


    public static void showToast(final String msg) {
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                Toast.makeText(app, msg, Toast.LENGTH_LONG).show();
            }
        });

    }

    public static void showToast(@StringRes int msgId) {
        showToast(app.getString(msgId));
    }
}
