package com.savypan.italker.factory.data.helper;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.savypan.italker.factory.Factory;
import com.savypan.italker.factory.model.api.RspModel;
import com.savypan.italker.factory.model.card.GroupCard;
import com.savypan.italker.factory.model.db.Group;
import com.savypan.italker.factory.model.db.Group_Table;
import com.savypan.italker.factory.model.db.User;
import com.savypan.italker.factory.network.IRemoteService;
import com.savypan.italker.factory.network.Network;

import retrofit2.Response;

public class GroupHelper {

    public static Group find(String groupId) {
        Group group = findFromLocal(groupId);
        if (group == null)
            group = findFormNetwork(groupId);
        return group;
    }

    // 从本地找Group
    public static Group findFromLocal(String groupId) {
        return SQLite.select()
                .from(Group.class)
                .where(Group_Table.id.eq(groupId))
                .querySingle();
    }

    // 从网络找Group
    public static Group findFormNetwork(String id) {
        IRemoteService remoteService = Network.remoteService();
        try {
            Response<RspModel<GroupCard>> response = remoteService.groupFind(id).execute();
            GroupCard card = response.body().getResult();
            if (card != null) {
                // 数据库的存储并通知
                Factory.getGroupCenter().dispatch(card);

                User user = UserHelper.searchFromLocal(card.getOwnerId());
                if (user != null) {
                    return card.build(user);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
