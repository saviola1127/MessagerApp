package com.savypan.italker.factory.data.helper;

import android.text.TextUtils;
import android.util.Log;

import com.savypan.italker.factory.Factory;
import com.savypan.italker.factory.R;
import com.savypan.italker.factory.data.IDataSource;
import com.savypan.italker.factory.model.api.RspModel;
import com.savypan.italker.factory.model.api.account.AccountRspModel;
import com.savypan.italker.factory.model.api.account.LoginModel;
import com.savypan.italker.factory.model.api.account.RegisterModel;
import com.savypan.italker.factory.model.db.User;
import com.savypan.italker.factory.network.IRemoteService;
import com.savypan.italker.factory.network.Network;
import com.savypan.italker.factory.persistence.Account;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountHelper {

    private static final String TAG = AccountHelper.class.getSimpleName();

    public static void register(RegisterModel model, IDataSource.ICallback<User> callback) {

        IRemoteService service = Network.remoteService();
        Call<RspModel<AccountRspModel>> call = service.accountRegister(model);
        //异步的请求
        call.enqueue(new AccountRspCallback(callback));
    }


    public static void login(LoginModel model, IDataSource.ICallback<User> callback) {

        IRemoteService service = Network.remoteService();
        Call<RspModel<AccountRspModel>> call = service.accountLogin(model);
        //异步的请求
        call.enqueue(new AccountRspCallback(callback));
    }



    public static void bindPushId(IDataSource.ICallback<User> callback) {
        String pushId = Account.getPushId();
        //Log.e(TAG, "start bindPushId now... and pushId =>" + pushId);
        if (TextUtils.isEmpty(pushId)) {
            return;
        }

        IRemoteService service = Network.remoteService();
        Call<RspModel<AccountRspModel>> call = service.accountBind(pushId);
        call.enqueue(new AccountRspCallback(callback));
    }



    private static class AccountRspCallback implements Callback<RspModel<AccountRspModel>> {

        private IDataSource.ICallback<User> callback;

        public AccountRspCallback(IDataSource.ICallback<User> callback) {
            this.callback = callback;
        }

        @Override
        public void onResponse(Call<RspModel<AccountRspModel>> call, Response<RspModel<AccountRspModel>> response) {
            //从返回中得到全局的model，内部是使用的gson
            RspModel<AccountRspModel> rspModel = response.body();
            if (rspModel.success()) {
                AccountRspModel accountRspModel = rspModel.getResult();
                if (accountRspModel != null) {
                    User user = accountRspModel.getUser();

                    //Log.e(TAG, "response is successful and userId is " + user.getId());
                    //Log.e(TAG, "response is successful and userId is " + user.getPhone());
                    //第一种方式直接保存
                    DBHelper.save(User.class, user);
                    //user.save();
                        /*
                        //第二种保存方式
                        FlowManager.getModelAdapter(User.class).save(user);

                        //第三种放在事务中
                        DatabaseDefinition definition = FlowManager.getDatabase(AppDatabase.class);
                        definition.beginTransactionAsync(new ITransaction() {
                            @Override
                            public void execute(DatabaseWrapper databaseWrapper) {
                                FlowManager.getModelAdapter(User.class).save(user);
                            }
                        }).build().execute();
                         */

                    //Log.e(TAG, "response is successful and user saved =>" + accountRspModel.isBound());

                    Account.login(accountRspModel);

                    //Log.e(TAG, "response is successful and login saved =>");

                    if (accountRspModel.isBound()) {
                        Account.setBound(true);

                        //Log.e(TAG, "response is successful and bound updated as true");

                        if (callback != null) {
                            //数据库写入和缓存绑定 再返回
                            callback.onDataLoaded(user);
                        }
                    }
                } else {
                    //callback.onDataLoaded(accountRspModel.getUser());
                    //绑定设备的触发
                    //Log.e(TAG, "response is successful and but resModel is null");
                    bindPushId(callback);
                }

            } else {
                //TODO 对返回的model中的失败的code进行解析，解析到对应的string资源上面
                //callback.onDataFailed();
                Log.e(TAG, "response is failed");
                Factory.decodeResponseCode(rspModel, callback);
            }
        }

        @Override
        public void onFailure(Call<RspModel<AccountRspModel>> call, Throwable t) {
            //Log.e(TAG, "exception =>" + t.getStackTrace().toString());
            if (callback != null) {
                callback.onDataFailed(R.string.data_network_error);
            }
        }
    }
}
