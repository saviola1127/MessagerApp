package com.savypan.italker.factory.data.helper;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.savypan.italker.factory.Factory;
import com.savypan.italker.factory.R;
import com.savypan.italker.factory.data.IDataSource;
import com.savypan.italker.factory.model.api.RspModel;
import com.savypan.italker.factory.model.api.user.UserUpdateModel;
import com.savypan.italker.factory.model.card.UserCard;
import com.savypan.italker.factory.model.db.User;
import com.savypan.italker.factory.model.db.User_Table;
import com.savypan.italker.factory.network.IRemoteService;
import com.savypan.italker.factory.network.Network;
import com.savypan.italker.factory.presenter.contact.FollowingPresenter;

import java.io.IOException;
import java.util.List;

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
                //唤起进行保存的操作
                Factory.getUserCenter().dispatch(card);

//                //数据库的操作，需要把usercard转换一个user
//                User user = card.build();
//                //异步统一的保存
//                DBHelper.save(User.class, user);
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

    //搜索的方法网络实现
    public static Call search(String name, IDataSource.ICallback<List<UserCard>> callback) {
        IRemoteService service = Network.remoteService();
        Call<RspModel<List<UserCard>>> call = service.userSearch(name);
        //异步的请求
        call.enqueue(new Callback<RspModel<List<UserCard>>>() {
            @Override
            public void onResponse(Call<RspModel<List<UserCard>>> call, Response<RspModel<List<UserCard>>> response) {
                RspModel<List<UserCard>> rspModel = response.body();
                if (response.isSuccessful()) {
                    //返回搜索的数据即可
                    callback.onDataLoaded(rspModel.getResult());
                } else {
                    Factory.decodeResponseCode(rspModel, callback);
                }
            }

            @Override
            public void onFailure(Call<RspModel<List<UserCard>>> call, Throwable t) {
                callback.onDataFailed(R.string.data_network_error);
            }
        });
        return call;
    }

    public static void follow(String id, IDataSource.ICallback<UserCard> callback) {
        IRemoteService service = Network.remoteService();
        Call<RspModel<UserCard>> call = service.userFollow(id);
        //异步的请求
        call.enqueue(new Callback<RspModel<UserCard>>() {
            @Override
            public void onResponse(Call<RspModel<UserCard>> call, Response<RspModel<UserCard>> response) {
                RspModel<UserCard> rspModel = response.body();
                if (response.isSuccessful()) {
                    //返回搜索的数据即可
                    UserCard userCard = rspModel.getResult();
                    Factory.getUserCenter().dispatch(userCard);
                    //user.save();
                    callback.onDataLoaded(userCard);
                } else {
                    Factory.decodeResponseCode(rspModel, callback);
                }
            }

            @Override
            public void onFailure(Call<RspModel<UserCard>> call, Throwable t) {
                callback.onDataFailed(R.string.data_network_error);
            }
        });
    }


    //刷新联系人的操作，不需要callback，直接存储到数据库，
    //并通过数据库观察者进行通知界面更新，更新的时候进行对比，然后局部差异更新
    public static void refreshContacts() {
        IRemoteService service = Network.remoteService();
        //异步的请求
        service.userContact().enqueue(new Callback<RspModel<List<UserCard>>>() {
            @Override
            public void onResponse(Call<RspModel<List<UserCard>>> call, Response<RspModel<List<UserCard>>> response) {
                RspModel<List<UserCard>> rspModel = response.body();
                if (response.isSuccessful()) {

                    List<UserCard> cards = rspModel.getResult();
                    if (cards == null || cards.size() == 0) {
                        return;
                    }

                    Factory.getUserCenter().dispatch(com.savypan.italker.common.utils.CollectionUtil.toArray(cards, UserCard.class));
                } else {
                    Factory.decodeResponseCode(rspModel, null);
                }
            }

            @Override
            public void onFailure(Call<RspModel<List<UserCard>>> call, Throwable t) {

            }
        });
    }


    /***
     * 从本地查询一个用户信息
     * @param id
     * @return
     */
    public static User findFromLocal(String id) {
        return SQLite.select()
                .from(User.class)
                .where(User_Table.id.eq(id))
                .querySingle();
    }


    /***
     * 从网络查询一个用户信息
     * @param id
     * @return
     */
    public static User findFromNetwork(String id) {

        IRemoteService remoteService = Network.remoteService();
        Response<RspModel<UserCard>> response = null;
        try {
            response = remoteService.userFind(id).execute();
            UserCard card = response.body().getResult();

            if (card != null) {
                //数据库刷新
                User user = card.build();
                Factory.getUserCenter().dispatch(card);
                return user;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    /***
     * 搜索一个用户，优先本地缓存，
     * 没有数据以后然后再从网络拉取
     * @param id
     * @return
     */
    public static User searchFromLocal(String id) {
        User user = findFromLocal(id);
        if (user == null) {
            return findFromNetwork(id);
        }

        return user;
    }

    /***
     * 搜索一个用户，优先从网络拉取
     * @param id
     * @return
     */
    public static User searchFromNetwork(String id) {
        User user = findFromNetwork(id);
        if (user == null) {
            return findFromLocal(id);
        }

        return user;
    }
}
