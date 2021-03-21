package net.qiujuer.web.italker.push;

import net.qiujuer.web.italker.push.service.AccountService;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJsonProvider;
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
        register(JacksonJsonProvider.class);

        //register logger
        register(Logger.class);

    }
}
