package net.qiujuer.web.italker.push.factory;

import net.qiujuer.web.italker.push.bean.db.Group;
import net.qiujuer.web.italker.push.bean.db.GroupMember;
import net.qiujuer.web.italker.push.bean.db.User;

import java.util.Set;

/**
 * 群数据库处理
 * Created by savypan
 * On 2021/6/19 15:29
 */
public class GroupFactory {
    public static Group findById(String groupId) {
        //TODO 查询一个群
        return null;
    }

    public static Set<GroupMember> getMembers(Group group) {
        //TODO 查询一个群的所有成员
        return null;
    }

    public static Group findById(User sender, String receiverId) {
        //TODO 查询一个群 同时该user必须为一个群的成员
        return null;
    }
}
