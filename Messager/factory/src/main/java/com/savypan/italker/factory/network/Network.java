package com.savypan.italker.factory.network;

import com.savypan.italker.common.Common;
import com.savypan.italker.factory.Factory;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Network {

    //构建一个Retrofit
    public static Retrofit getRetrofit() {
        //构建一个okhttpclient
        OkHttpClient client = new OkHttpClient.Builder().build();

        Retrofit.Builder builder = new Retrofit.Builder();
        return builder.baseUrl(Common.Constant.API_URL)
                .client(client)
                //设置json解析器
                .addConverterFactory(GsonConverterFactory.create(Factory.getGson()))
                .build();
    }
}
