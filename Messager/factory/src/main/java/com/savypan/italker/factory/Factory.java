package com.savypan.italker.factory;

import android.app.Application;
import android.support.annotation.StringRes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.savypan.italker.common.app.CommonApplication;
import com.savypan.italker.factory.data.IDataSource;
import com.savypan.italker.factory.model.api.RspModel;
import com.savypan.italker.factory.persistence.Account;
import com.savypan.italker.factory.utils.DBFlowExclusionStrategy;

import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.sql.DataSource;

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
                .setExclusionStrategies(new DBFlowExclusionStrategy())
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

    /***
     * 进行错误数据的解析 把网络返回的code值进行统一的规划 并返回为一个String资源
     * @param model
     * @param callback
     */
    public static void decodeResponseCode(RspModel model, IDataSource.FailedCallback callback) {
        if (model == null)
            return;

        // 进行Code区分
        switch (model.getCode()) {
            case RspModel.SUCCEED:
                return;
            case RspModel.ERROR_SERVICE:
                decodeRspCode(R.string.data_rsp_error_service, callback);
                break;
            case RspModel.ERROR_NOT_FOUND_USER:
                decodeRspCode(R.string.data_rsp_error_not_found_user, callback);
                break;
            case RspModel.ERROR_NOT_FOUND_GROUP:
                decodeRspCode(R.string.data_rsp_error_not_found_group, callback);
                break;
            case RspModel.ERROR_NOT_FOUND_GROUP_MEMBER:
                decodeRspCode(R.string.data_rsp_error_not_found_group_member, callback);
                break;
            case RspModel.ERROR_CREATE_USER:
                decodeRspCode(R.string.data_rsp_error_create_user, callback);
                break;
            case RspModel.ERROR_CREATE_GROUP:
                decodeRspCode(R.string.data_rsp_error_create_group, callback);
                break;
            case RspModel.ERROR_CREATE_MESSAGE:
                decodeRspCode(R.string.data_rsp_error_create_message, callback);
                break;
            case RspModel.ERROR_PARAMETERS:
                decodeRspCode(R.string.data_rsp_error_parameters, callback);
                break;
            case RspModel.ERROR_PARAMETERS_EXIST_ACCOUNT:
                decodeRspCode(R.string.data_rsp_error_parameters_exist_account, callback);
                break;
            case RspModel.ERROR_PARAMETERS_EXIST_NAME:
                decodeRspCode(R.string.data_rsp_error_parameters_exist_name, callback);
                break;
            case RspModel.ERROR_ACCOUNT_TOKEN:
                CommonApplication.showToast(R.string.data_rsp_error_account_token);
                factory.logout();
                break;
            case RspModel.ERROR_ACCOUNT_LOGIN:
                decodeRspCode(R.string.data_rsp_error_account_login, callback);
                break;
            case RspModel.ERROR_ACCOUNT_REGISTER:
                decodeRspCode(R.string.data_rsp_error_account_register, callback);
                break;
            case RspModel.ERROR_ACCOUNT_NO_PERMISSION:
                decodeRspCode(R.string.data_rsp_error_account_no_permission, callback);
                break;
            case RspModel.ERROR_UNKNOWN:
            default:
                decodeRspCode(R.string.data_rsp_error_unknown, callback);
                break;
        }
    }

    private static void decodeRspCode(@StringRes final int resId,
                                      final IDataSource.FailedCallback callback) {
        if (callback != null)
            callback.onDataFailed(resId);
    }

    /**
     * 收到账户退出的消息需要进行账户退出重新登录
     */
    private void logout() {

    }

    /***
     * 处理推送接收到的消息
     * @param message
     */
    public static void dispatchPush (String message) {
        //TODO
    }


    /***
     * Factory中的初始化
     */
    public static void setup() {
        //数据库初始化
        FlowManager.init(new FlowConfig.Builder(getApplication()).openDatabasesOnInit(true).build());
        Account.load(getApplication());
    }
}
