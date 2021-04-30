package com.savypan.italker.factory.data.helper;

import com.savypan.italker.factory.Factory;
import com.savypan.italker.factory.R;
import com.savypan.italker.factory.data.IDataSource;
import com.savypan.italker.factory.model.api.RspModel;
import com.savypan.italker.factory.model.api.account.AccountRspModel;
import com.savypan.italker.factory.model.api.user.UserUpdateModel;
import com.savypan.italker.factory.model.card.UserCard;
import com.savypan.italker.factory.model.db.User;
import com.savypan.italker.factory.network.IRemoteService;
import com.savypan.italker.factory.network.Network;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserHelper {
    public static void update(UserUpdateModel model, IDataSource.ICallback<UserCard> callback) {
        IRemoteService service = Network.remoteService();
        Call<RspModel<UserCard>> call = service.userUpdate(model);
        //异步的请求
        call.enqueue(new UserHelper.UserRspCallback(callback));
    }
    

    private static class UserRspCallback implements Callback<RspModel<UserCard>> {

        private IDataSource.ICallback<UserCard> callback;

        public UserRspCallback(IDataSource.ICallback<UserCard> callback) {
            this.callback = callback;
        }

        @Override
        public void onResponse(Call<RspModel<UserCard>> call, Response<RspModel<UserCard>> response) {
            RspModel<UserCard> rspModel = response.body();
            if (rspModel.success()) {
                UserCard card = rspModel.getResult();
                //数据库的操作，需要把usercard转换一个user

                //返回成功
                callback.onDataLoaded(card);
            } else {
                //错误情况下分配
                Factory.decodeResponseCode(rspModel, callback);
            }
        }

        @Override
        public void onFailure(Call<RspModel<UserCard>> call, Throwable t) {
            if (callback != null) {
                callback.onDataFailed(R.string.data_network_error);
            }
        }
    }
}
