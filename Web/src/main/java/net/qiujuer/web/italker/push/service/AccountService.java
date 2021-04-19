package net.qiujuer.web.italker.push.service;

import com.google.common.base.Strings;
import net.qiujuer.web.italker.push.bean.api.account.AccountRspModel;
import net.qiujuer.web.italker.push.bean.api.account.LoginModel;
import net.qiujuer.web.italker.push.bean.api.account.RegisterModel;
import net.qiujuer.web.italker.push.bean.api.base.ResponseModel;
import net.qiujuer.web.italker.push.bean.db.User;
import net.qiujuer.web.italker.push.factory.UserFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * Created by savypan
 * On 2021/3/21 18:00
 */
//127.0.0.1/api/account
@Path("/account")
public class AccountService extends BaseService {


    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<AccountRspModel> login(LoginModel model) {

        if (!LoginModel.check(model)) {
            return ResponseModel.buildParameterError();
        }

        User user = UserFactory.login(model.getAccount(), model.getPassword());
        if (user != null) {

            if (!Strings.isNullOrEmpty(model.getPushId())) {
                //如果有携带有pushId，则绑定登录
                return bind(user, model.getPushId());
            }

            AccountRspModel rspModel = new AccountRspModel(user);
            return ResponseModel.buildOk(rspModel);
        } else {
            return ResponseModel.buildLoginError();
        }
    }




    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<AccountRspModel> register(RegisterModel model) {

        if (!RegisterModel.check(model)) {
            return ResponseModel.buildParameterError();
        }

        User user = UserFactory.findByPhone(model.getAccount().trim());
        if (user != null) {
            //已有账户
            return ResponseModel.buildHaveAccountError();
        }

        user = UserFactory.findByName(model.getName().trim());
        if (user != null) {
            return ResponseModel.buildHaveNameError();
        }

        //开始注册逻辑
        user = UserFactory.register(model.getAccount(), model.getPassword(), model.getName());

        if (user != null) {
            AccountRspModel rspModel = new AccountRspModel(user);
            return ResponseModel.buildOk(rspModel);
        }

        return ResponseModel.buildRegisterError();
    }


    @POST
    @Path("/bind/{pushId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    //从请求头中获取token字段
    //pushId从url地址中获取
    public ResponseModel<AccountRspModel> bind(@HeaderParam("token") String token,
                                               @PathParam("pushId") String pushId) {

        if (Strings.isNullOrEmpty(token) || Strings.isNullOrEmpty(pushId)) {
            return ResponseModel.buildParameterError();
        }

        //User user = UserFactory.findByToken(token);
        User user = getSelf();
        return bind(user, pushId);
//        if (user != null) {
//            return bind(user, pushId);
//        } else {
//            //账户异常
//            return ResponseModel.buildAccountError();
//        }
    }


    private ResponseModel<AccountRspModel> bind(User self, String pushId) {
        User user = UserFactory.bindPushId(self, pushId);

        if (user != null) {
            AccountRspModel rspModel = new AccountRspModel(user, true);
            return ResponseModel.buildOk(rspModel);
        } else {
            return ResponseModel.buildServiceError();
        }
    }
}
