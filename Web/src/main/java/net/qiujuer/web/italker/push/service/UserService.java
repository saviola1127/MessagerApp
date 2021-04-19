package net.qiujuer.web.italker.push.service;

import net.qiujuer.web.italker.push.bean.api.base.ResponseModel;
import net.qiujuer.web.italker.push.bean.api.user.UpdateInfoModel;
import net.qiujuer.web.italker.push.bean.card.UserCard;
import net.qiujuer.web.italker.push.bean.db.User;
import net.qiujuer.web.italker.push.factory.UserFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

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
}
