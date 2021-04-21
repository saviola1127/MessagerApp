package com.savypan.italker.factory.data.helper;

import android.util.Log;

import com.savypan.italker.factory.R;
import com.savypan.italker.factory.data.IDataSource;
import com.savypan.italker.factory.model.api.RspModel;
import com.savypan.italker.factory.model.api.account.AccountRspModel;
import com.savypan.italker.factory.model.api.account.RegisterModel;
import com.savypan.italker.factory.model.db.User;
import com.savypan.italker.factory.network.IRemoteService;
import com.savypan.italker.factory.network.Network;

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
                    if (accountRspModel != null && accountRspModel.isBound()) {
                        User user = accountRspModel.getUser();

                        //数据库写入和缓存绑定 再返回
                        callback.onDataLoaded(user);
                    } else {
                        //绑定设备的装备
                        bindPushId(callback);
                    }

                } else {
                    //对返回的model中的失败的code进行解析，解析到对应的string资源上面
                    //callback.onDataFailed();
                }
            }

            @Override
            public void onFailure(Call<RspModel<AccountRspModel>> call, Throwable t) {
                Log.e("SAVY", "onFailure now");
                callback.onDataFailed(R.string.data_network_error);
            }
        });
    }


    public static void bindPushId(IDataSource.ICallback<User> callback) {

    }
}
