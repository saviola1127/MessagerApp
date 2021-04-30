package com.savypan.italker.factory.network;

import android.text.TextUtils;

import com.savypan.italker.common.Common;
import com.savypan.italker.factory.Factory;
import com.savypan.italker.factory.persistence.Account;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Network {

    private static Network instance;
    private Retrofit retrofit;

    static {
        instance = new Network();
    }

    private Network() {

    }

    //构建一个Retrofit
    public static Retrofit getRetrofit() {
        if (instance.retrofit != null) {
            return instance.retrofit;
        }

        //构建一个okhttpclient
        OkHttpClient client = new OkHttpClient.Builder()
                //给所有的请求添加一个拦截器
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        //拿到我们的请求，重新进行builder
                        Request ori = chain.request();
                        Request.Builder builder = ori.newBuilder();

                        if (!TextUtils.isEmpty(Account.getToken())) {
                            builder.addHeader("token", Account.getToken());
                        }

                        builder.addHeader("Content-Type", "application/json");
                        Request now = builder.build();

                        return chain.proceed(now);
                    }
                })
                .build();

        Retrofit.Builder builder = new Retrofit.Builder();
        instance.retrofit = builder.baseUrl(Common.Constant.API_URL)
                .client(client)
                //设置json解析器
                .addConverterFactory(GsonConverterFactory.create(Factory.getGson()))
                .build();

        return instance.retrofit;
    }


    public static IRemoteService remoteService() {
        return Network.getRetrofit().create(IRemoteService.class);
    }
}
