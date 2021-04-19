package net.qiujuer.web.italker.push.provider;

import com.google.common.base.Strings;
import net.qiujuer.web.italker.push.bean.api.base.ResponseModel;
import net.qiujuer.web.italker.push.bean.db.User;
import net.qiujuer.web.italker.push.factory.UserFactory;
import org.glassfish.jersey.server.ContainerRequest;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.security.Principal;

/**
 * 用于所有的请求的接口过滤和拦截器
 * Created by savypan
 * On 2021/4/18 16:53
 */
@Provider
public class AuthRequestFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        //检测是否是登录注册接口
        String relPath = ((ContainerRequest)requestContext).getPath(false);
        if (relPath.startsWith("account/login")
                || relPath.startsWith("account/register")) {
            //针对登录注册流程，直接返回
            return;
        }


        String token = requestContext.getHeaders().getFirst("token");
        if (!Strings.isNullOrEmpty(token)) {
            final User user = UserFactory.findByToken(token);
            if (user != null) {
                //给当前请求添加一个上下文
                requestContext.setSecurityContext(new SecurityContext() {

                    //主体部分
                    @Override
                    public Principal getUserPrincipal() {
                        return user;
                    }

                    @Override
                    public boolean isUserInRole(String role) {
                        //可以在这里写入用户权限, role是权限名，可以管理管理员权限等
                        return true;
                    }

                    @Override
                    public boolean isSecure() {
                        //是否是https
                        return false;
                    }

                    @Override
                    public String getAuthenticationScheme() {
                        //不用理会这个scheme
                        return null;
                    }
                });

                return;
            }
        }

        //直接返回一个账户异常，需要登录的model
        ResponseModel model = ResponseModel.buildAccountError();

        Response response = Response.status(Response.Status.OK)
                .entity(model)
                .build();

        //停止一个请求的继续下发，调用该方法后之间返回请求不会走到service中去
        requestContext.abortWith(response);
    }
}
