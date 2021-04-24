package com.savypan.italker.factory.data.helper;

import android.util.Log;

import com.raizlabs.android.dbflow.annotation.Database;
import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ITransaction;
import com.savypan.italker.factory.Factory;
import com.savypan.italker.factory.R;
import com.savypan.italker.factory.data.IDataSource;
import com.savypan.italker.factory.model.api.RspModel;
import com.savypan.italker.factory.model.api.account.AccountRspModel;
import com.savypan.italker.factory.model.api.account.RegisterModel;
import com.savypan.italker.factory.model.db.AppDatabase;
import com.savypan.italker.factory.model.db.User;
import com.savypan.italker.factory.network.IRemoteService;
import com.savypan.italker.factory.network.Network;
import com.savypan.italker.factory.persistence.Account;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountHelper {

    public static void register(RegisterModel model, IDataSource.ICallback<User> callback) {

        IRemoteService service = Network.getRetrofit().create(IRemoteService.class);
        Call<RspModel<AccountRspModel>> call = service.accountRegister(model);
        //异步的请求
        call.enqueue(new Callback<RspModel<AccountRspModel>>() {
            @Override
            public void onResponse(Call<RspModel<AccountRspModel>> call,
                                   Response<RspModel<AccountRspModel>> response) {

                if (response.isSuccessful()) {
                    Log.e("SAVY", "response is successful");
                }

                //从返回中得到全局的model，内部是使用的gson
                RspModel<AccountRspModel> rspModel = response.body();
                if (rspModel.success()) {
                    AccountRspModel accountRspModel = rspModel.getResult();
                    if (accountRspModel != null) {
                        User user = accountRspModel.getUser();

                        //第一种方式直接保存
                        user.save();

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

                        Account.login(accountRspModel);//user);

                        if (accountRspModel.isBound()) {
                            //数据库写入和缓存绑定 再返回
                            callback.onDataLoaded(user);
                        }
                    } else {
                        //callback.onDataLoaded(accountRspModel.getUser());
                        //绑定设备的触发
                        bindPushId(callback);
                    }

                } else {
                    //TODO 对返回的model中的失败的code进行解析，解析到对应的string资源上面
                    //callback.onDataFailed();
                    Factory.decodeResponseCode(rspModel, callback);
                }
            }

            @Override
            public void onFailure(Call<RspModel<AccountRspModel>> call, Throwable t) {
                callback.onDataFailed(R.string.data_network_error);
            }
        });
    }


    public static void bindPushId(IDataSource.ICallback<User> callback) {
        //first throw an exception, but actually this is due to binding not executed
        Account.setBound(true);
    }
}
