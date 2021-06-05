package com.savypan.italker.factory.data.group;

import com.savypan.italker.factory.data.helper.DBHelper;
import com.savypan.italker.factory.data.helper.GroupHelper;
import com.savypan.italker.factory.data.helper.UserHelper;
import com.savypan.italker.factory.model.card.GroupCard;
import com.savypan.italker.factory.model.card.GroupMemberCard;
import com.savypan.italker.factory.model.db.Group;
import com.savypan.italker.factory.model.db.GroupMember;
import com.savypan.italker.factory.model.db.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class GroupDispatcher implements IGroupCenter {

    private static IGroupCenter instance;

    //单线程池：处理群组一个个的消息序列
    private final Executor executor = Executors.newSingleThreadExecutor();

    public static IGroupCenter getInstance() {
        if (instance == null) {
            synchronized (GroupDispatcher.class) {
                if (instance == null) {
                    instance = new GroupDispatcher();
                }
            }
        }
        return instance;
    }

    @Override
    public void dispatch(GroupCard... cards) {
        if (cards == null || cards.length == 0) {
            return;
        }

        executor.execute(new GroupHandler(cards));
    }

    @Override
    public void dispatch(GroupMemberCard... cards) {
        if (cards == null || cards.length == 0) {
            return;
        }

        executor.execute(new GroupMemberHandler(cards));
    }

    private class GroupHandler implements Runnable {

        private final GroupCard[] cards;

        GroupHandler(GroupCard[] cards) {
            this.cards = cards;
        }

        @Override
        public void run() {
            List<Group> groups = new ArrayList<>();
            for (GroupCard card : cards) {
                User owner = UserHelper.searchFromLocal(card.getOwnerId());
                if (owner != null) {
                    Group group = card.build(owner);
                    groups.add(group);
                }
            }

            if (groups.size() > 0) {
                DBHelper.save(Group.class, groups.toArray(new Group[0]));
            }
        }
    }


    private class GroupMemberHandler implements Runnable {

        private final GroupMemberCard[] cards;

        private GroupMemberHandler(GroupMemberCard[] cards) {
            this.cards = cards;
        }

        @Override
        public void run() {
            List<GroupMember> members = new ArrayList<>();
            for (GroupMemberCard member : cards) {
                //群人员对应的信息
                User user = UserHelper.searchFromLocal(member.getUserId());
                //对群的搜索
                Group group = GroupHelper.find(member.getGroupId());
                if (user != null && group != null) {
                    GroupMember member1 = member.build(group, user);
                    members.add(member1);
                }
            }
            if (members.size() > 0)
            {
                DBHelper.save(GroupMember.class, members.toArray(new GroupMember[0]));
            }
        }
    }
}
