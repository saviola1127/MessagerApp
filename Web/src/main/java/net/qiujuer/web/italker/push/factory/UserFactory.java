package net.qiujuer.web.italker.push.factory;

import com.google.common.base.Strings;
import net.qiujuer.web.italker.push.bean.api.account.AccountRspModel;
import net.qiujuer.web.italker.push.bean.api.base.ResponseModel;
import net.qiujuer.web.italker.push.bean.db.User;
import net.qiujuer.web.italker.push.utils.Hib;
import net.qiujuer.web.italker.push.utils.TextUtil;
import org.hibernate.Session;

import java.util.List;
import java.util.UUID;

/**
 * Created by savypan
 * On 2021/4/17 13:14
 */
public class UserFactory {


    public static User findByToken(String token) {

        return Hib.query(new Hib.IQueryOnly<User>() {

            @Override
            public User query(Session session) {
                User user = (User) session.createQuery("from User where token=:token")
                        .setParameter("token", token)
                        .uniqueResult();

                return user;
            }
        });
    }


    public static User findByPhone(String phone) {

        return Hib.query(new Hib.IQueryOnly<User>() {

            @Override
            public User query(Session session) {
                User user = (User) session.createQuery("from User where phone=:inPhone")
                        .setParameter("inPhone", phone)
                        .uniqueResult();

                return user;
            }
        });
    }


    public static User findByName(String name) {

        return Hib.query(new Hib.IQueryOnly<User>() {

            @Override
            public User query(Session session) {
                User user = (User) session.createQuery("from User where name=:name")
                        .setParameter("name", name)
                        .uniqueResult();

                return user;
            }
        });
    }


    /***
     * 更新用户信息到数据库
     * @param user
     * @return
     */
    public static User update(User user) {
        return Hib.query( session -> {
            session.saveOrUpdate(user);
            return user;
        });
    }



    public static User login(String account, String password) {
        String accountName = account.trim();
        String passwordTrim = encodePassword(password);

        //寻找
        User user = Hib.query( session ->
             (User) session.createQuery("from User where phone=:phone and password =:password")
                    .setParameter("phone", accountName)
                    .setParameter("password", passwordTrim)
                    .uniqueResult()
        );

        if (user != null) {
            user = login(user);
        }

        return user;
    }



    public static User register(String account, String password, String name) {

        //去除账户中的收尾空格
        account = account.trim();
        password = encodePassword(password);

        User user = createUser(account, password, name);

        if (user != null) {
            user = login(user);
        }

        return user;
    }


    private static User createUser(String account, String password, String name) {
        User user = new User();

        user.setName(name);
        user.setPassword(password);
        user.setPhone(account);

        return Hib.query(session ->
                {
                    session.save(user);
                    return user;
                });
    }



    public static User bindPushId(User user, String pushId){

        if (Strings.isNullOrEmpty(pushId)) {
            return null;
        }

        //第一步，查询是否有其他设备绑定了push
        //取消绑定，避免推送混乱

        Hib.query(session -> { //查询的列表不包括自己
            List<User> userList = (List<User>) session
                    .createQuery("from User where lower(pushId)=:pushId and id!=:userId")
                    .setParameter("pushId", pushId.toLowerCase())
                    .setParameter("userId", user.getId())
                    .list();

            for (User u : userList) {
                //更新为null
                u.setPushId(null);
                session.saveOrUpdate(u);
            }
        });

        if (pushId.equalsIgnoreCase(user.getPushId())) {
            //如果当前需要绑定的pushId之前已经绑定过了
            return user;
        } else {
            //如果
            if (Strings.isNullOrEmpty(user.getPushId())) {
                //如果当前账户的pushId和需要绑定的不同
                //那么需要单点登录，让之前的设备退出账户，给之前的设备推送一条退出登录消息
                //TODO 推送一个退出消息
            }

            //更新新的设备Id
            user.setPushId(pushId);
            return update(user);
        }
    }


    /**
     * 登录一个user，更新token本质
     * @param user
     * @return
     */
    private static User login(User user) {
        //使用一个随机的UUID值
        String newToken = UUID.randomUUID().toString();
        //进行一次base64格式化
        newToken = TextUtil.encodeBase64(newToken);
        user.setToken(newToken);

        return update(user);
    }


    private static String encodePassword(String password) {
        //去除收尾空格
        password = password.trim();
        password = TextUtil.getMD5(password); //calculate MD5 encryption

        return TextUtil.encodeBase64(password);
    }


}
