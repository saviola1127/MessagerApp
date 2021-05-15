package net.qiujuer.web.italker.push.service;

import com.google.common.base.Strings;
import net.qiujuer.web.italker.push.bean.api.base.ResponseModel;
import net.qiujuer.web.italker.push.bean.api.user.UpdateInfoModel;
import net.qiujuer.web.italker.push.bean.card.UserCard;
import net.qiujuer.web.italker.push.bean.db.User;
import net.qiujuer.web.italker.push.factory.UserFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by savypan
 * On 2021/4/18 16:26
 */
//127.0.0.1/api/user
@Path("/user")
public class UserService extends BaseService {

    @PUT
    //@Path("aa")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<UserCard> update(UpdateInfoModel model) {

        User user = getSelf();

        //更新用户信息
        user = model.updateToUser(user);
        user = UserFactory.update(user);

        //构建自己的用户信息
        UserCard userCard = new UserCard(user);

        return ResponseModel.buildOk(userCard);
    }

    @GET //get contacts
    @Path("/contact")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<List<UserCard>> contact() {
        User self = getSelf();

        List<User> users = UserFactory.contacts(self);
        List<UserCard> userCards = users.stream()
                .map(user -> {
                    return new UserCard(user, true);
                }) //转置生成UserCard
        .collect(Collectors.toList());

        return ResponseModel.buildOk(userCards);
    }


    @PUT //简化：关注的同时，对方也关注
    @Path("/follow/{followId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<UserCard> follow(@PathParam("followId") String followId) {
        User self = getSelf();

        //自己不能关住自己
        if (self.getId().equalsIgnoreCase(followId)
                || Strings.isNullOrEmpty(followId)) {
            return ResponseModel.buildParameterError();
        }

        //找到我要关注的人
        User followingUser = UserFactory.findById(followId);
        if (followingUser == null) {
            return ResponseModel.buildNotFoundUserError(null);
        }

        //备注没有，以后可以扩展
        followingUser = UserFactory.follow(self, followingUser, null);
        if (followingUser == null) {
            return ResponseModel.buildServiceError();
        }

        //TODO 通知被关注人，我关注了你

        return ResponseModel.buildOk(new UserCard(followingUser, true));
    }


    //获取某人的信息
    @GET
    @Path("/{id}")  //127.0.0.1/api/user/{id}
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<UserCard> getUser(@PathParam("id") String id) {
        if (Strings.isNullOrEmpty(id)) {
            return ResponseModel.buildParameterError();
        }

        User self = getSelf();
        if (self.getId().equalsIgnoreCase(id)) {
            //返回自己，不用查询数据库
            return ResponseModel.buildOk(new UserCard(self, true));
        }

        User user = UserFactory.findById(id);
        if (user == null) {
            //没找到，返回没有找到用户异常
            return ResponseModel.buildNotFoundUserError(null);
        }

        //获取当前用户对于跟某人关注信息的查询生产usercard
        boolean isFollowed = UserFactory.getUserFollow(self, user) != null;
        return ResponseModel.buildOk(new UserCard(user, isFollowed));
    }


    //搜索人的实现
    @GET //get contacts
    @Path("/search/{name:(.*)?}")   //127.0.01/api/user/search/...
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<List<UserCard>> search(@DefaultValue("") @PathParam("name") String name) {
        User self = getSelf();

        //先查询数据
        List<User> userList = UserFactory.search(name);
        //查询的人封装为usercard，判断这些人哪些是我已经关注的人，如果有，则已经标注好
        List<User> contacts = UserFactory.contacts(self);

        List<UserCard> userCards = userList.stream()
                .map(user -> {
                    //判断user是否在我的联系人中，或者是我自己
                    boolean isFollowed = user.getId().equalsIgnoreCase(self.getId())
                            || contacts.stream().anyMatch(
                                    contact -> contact.getId().equalsIgnoreCase(user.getId()));

                    return new UserCard(user, isFollowed);
                }).collect(Collectors.toList());

        return ResponseModel.buildOk(userCards);
    }
}
