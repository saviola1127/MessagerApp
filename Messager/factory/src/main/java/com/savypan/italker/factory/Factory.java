package com.savypan.italker.factory;

import android.app.Application;
import android.support.annotation.StringRes;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.savypan.italker.common.app.CommonApplication;
import com.savypan.italker.factory.data.IDataSource;
import com.savypan.italker.factory.data.group.GroupDispatcher;
import com.savypan.italker.factory.data.group.IGroupCenter;
import com.savypan.italker.factory.data.message.IMessageCenter;
import com.savypan.italker.factory.data.message.MessageDispatcher;
import com.savypan.italker.factory.data.user.IUserCenter;
import com.savypan.italker.factory.data.user.UserDispatcher;
import com.savypan.italker.factory.model.api.PushModel;
import com.savypan.italker.factory.model.api.RspModel;
import com.savypan.italker.factory.model.card.GroupCard;
import com.savypan.italker.factory.model.card.GroupMemberCard;
import com.savypan.italker.factory.model.card.MessageCard;
import com.savypan.italker.factory.model.card.UserCard;
import com.savypan.italker.factory.model.db.GroupMember;
import com.savypan.italker.factory.persistence.Account;
import com.savypan.italker.factory.utils.DBFlowExclusionStrategy;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.sql.DataSource;

public class Factory {

    private static final Factory factory;
    private final Executor executor;
    private final Gson gson;
    private static final String TAG = Factory.class.getSimpleName();

    static {
        factory = new Factory();
    }

    private Factory() {
        executor = Executors.newFixedThreadPool(4);
        gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateDeserializer())
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
                //TODO ??????????????????????????????????????????Model???????????????
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
     * ??????????????????????????? ??????????????????code???????????????????????? ??????????????????String??????
     * @param model
     * @param callback
     */
    public static void decodeResponseCode(RspModel model, IDataSource.FailedCallback callback) {
        if (model == null)
            return;

        // ??????Code??????
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
     * ???????????????????????????????????????????????????????????????
     */
    private void logout() {

    }

    /***
     * ??????????????????????????????
     * @param message
     */
    public static void dispatchPush (String message) {
        //????????????????????????
        if (!Account.isLogin()) {
            return;
        }

        PushModel model = PushModel.decode(message);
        if (model == null) {
            return;
        }

        //Log.e(TAG, "Model.toString() =>" + model.toString());

        //???????????????????????????
        for (PushModel.Entity entity : model.getEntities())
        {
            Log.e(TAG, "dispatchPush-Entity :" + entity.toString());

            switch (entity.type) {
                case PushModel.ENTITY_TYPE_LOGOUT:
                    factory.logout();
                    return; //logout????????????????????????????????????

                case PushModel.ENTITY_TYPE_MESSAGE:
                    MessageCard card = getGson().fromJson(entity.content, MessageCard.class);
                    getMsgCenter().dispatch(card);
                    break;

                case PushModel.ENTITY_TYPE_ADD_FRIEND:
                    UserCard card1 = getGson().fromJson(entity.content, UserCard.class);
                    getUserCenter().dispatch(card1);
                    break;

                case PushModel.ENTITY_TYPE_ADD_GROUP:
                    GroupCard card2 = getGson().fromJson(entity.content, GroupCard.class);
                    getGroupCenter().dispatch(card2);
                    break;

                case PushModel.ENTITY_TYPE_ADD_GROUP_MEMBERS:
                case PushModel.ENTITY_TYPE_MODIFY_GROUP_MEMBERS:
                    Type type = new TypeToken<List<GroupMemberCard>>(){}.getType();

                    List<GroupMemberCard> cardList = getGson().fromJson(entity.content, type);
                    getGroupCenter().dispatch(cardList.toArray(new GroupMemberCard[0]));
                    break;

                case PushModel.ENTITY_TYPE_EXIT_GROUP_MEMBERS:
                    //TODO
                    break;
            }
        }
    }


    /***
     * Factory???????????????
     */
    public static void setup() {
        //??????????????????
        FlowManager.init(new FlowConfig.Builder(getApplication()).openDatabasesOnInit(true).build());
        Account.load(getApplication());
    }

    /***
     * ???????????????????????????????????????
     * @return ???????????????
     */
    public static IUserCenter getUserCenter() {
        return UserDispatcher.getInstance();
    }

    /***
     * ????????????????????????????????????
     * @return
     */
    public static IMessageCenter getMsgCenter() {
        return MessageDispatcher.getInstance();
    }


    /***
     * ????????????????????????????????????
     * @return
     */
    public static IGroupCenter getGroupCenter() {
        return GroupDispatcher.getInstance();
    }
}
