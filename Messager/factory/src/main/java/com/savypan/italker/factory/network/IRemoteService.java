package com.savypan.italker.factory.network;

import com.savypan.italker.factory.model.api.RspModel;
import com.savypan.italker.factory.model.api.account.AccountRspModel;
import com.savypan.italker.factory.model.api.account.RegisterModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface IRemoteService {

    /***
     * 网络请求一个注册接口
     * @param model
     * @return
     */
    @POST("account/register")
    Call<RspModel<AccountRspModel>> accountRegister(@Body RegisterModel model);
}
