package net.qiujuer.web.italker.push;

import net.qiujuer.web.italker.push.provider.AuthRequestFilter;
import net.qiujuer.web.italker.push.provider.GsonProvider;
import net.qiujuer.web.italker.push.service.AccountService;
import org.glassfish.jersey.server.ResourceConfig;

import java.util.logging.Logger;

/**
 * Created by savypan
 * On 2021/3/21 15:47
 */
public class Application extends ResourceConfig {

    public Application(){
        //register service
        //packages("net.qiujuer.web.italker.push.service");
        packages(AccountService.class.getPackage().getName());

        //register json parser
        //register(JacksonJsonProvider.class);
        register(GsonProvider.class);

        //注册全局的请求拦截器
        register(AuthRequestFilter.class);

        //register logger
        register(Logger.class);

    }
}
