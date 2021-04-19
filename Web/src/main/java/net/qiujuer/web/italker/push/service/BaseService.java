package net.qiujuer.web.italker.push.service;

import net.qiujuer.web.italker.push.bean.db.User;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

/**
 * Created by savypan
 * On 2021/4/18 17:31
 */
public class BaseService {

    //添加一个上下文注解，该注解会给securityContext赋值
    //具体的值会为我们的拦截器中所返回的sc
    @Context
    protected SecurityContext securityContext;


    protected User getSelf() {
        if (securityContext == null) {
            return null;
        }
        return (User) securityContext.getUserPrincipal();
    }
}
