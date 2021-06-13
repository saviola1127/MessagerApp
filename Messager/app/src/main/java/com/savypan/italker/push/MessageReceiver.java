package com.savypan.italker.push;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.igexin.sdk.PushConsts;
import com.savypan.italker.factory.Factory;
import com.savypan.italker.factory.data.helper.AccountHelper;
import com.savypan.italker.factory.persistence.Account;

/***
 * 个推消息接收器
 */
public class MessageReceiver extends BroadcastReceiver {

    private static final String TAG = MessageReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null) {
            return;
        }

        Bundle bundle = intent.getExtras();

        //判断当前消息的意图
        switch (bundle.getInt(PushConsts.CMD_ACTION)){
            case PushConsts.GET_CLIENTID: {
                Log.i(TAG, "GET_CLIENTID:" + bundle.toString());
                //当Id初始化的时候
                //获取设备id
                onClientIdInit(bundle.getString("clientid"));

                break;
            }

            case PushConsts.GET_MSG_DATA: {
                //常规消息的送达
                byte[] payload = bundle.getByteArray("payload");
                if (payload != null) {
                    String message = new String(payload);
                    Log.e(TAG, "GET_MSG_DATA:" + message);
                    onMessageArrived(message);
                }
                break;
            }
            default:
                Log.i(TAG, "Other:" + bundle.toString());
                break;
        }
    }


    /***
     * 当ID初始化的时候
     * @param cid
     */
    private void onClientIdInit(String cid) {
        Account.setPushId(cid); //设备ID

        if (Account.isLogin()) {
            //设备登录进行一次pushid绑定
            //没有登录不允许绑定
            AccountHelper.bindPushId(null);
        }
    }


    /***
     * 消息送达的时候
     * @param message
     */
    private void onMessageArrived(String message) {
        Factory.dispatchPush(message);
    }
}
