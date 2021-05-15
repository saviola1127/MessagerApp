package net.qiujuer.web.italker.push.factory;

import com.google.common.base.Strings;
import net.qiujuer.web.italker.push.bean.db.User;
import net.qiujuer.web.italker.push.bean.db.UserFollow;
import net.qiujuer.web.italker.push.utils.Hib;
import net.qiujuer.web.italker.push.utils.TextUtil;
import org.hibernate.Session;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

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


    public static User findById(String id) {

        return Hib.query( session -> {
            return session.get(User.class, id);
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


    //获取联系人的列表
    public static List<User> contacts(User self) {
        //这里不能用self.getFollowing去获取关注的人的数据是因为之前是通过token获取的，
        //在上一次的获取中，事务已经结束，并且不能在后续中得到，所以需要重新在一个事务中加载一次，这个就是懒加载
        return Hib.query(session -> {
            //重新加载一次用户信息到self中，和当前session绑定
            session.load(self, self.getId());

            //获取关注的人的集合
            Set<UserFollow> followings = self.getFollowings();

            return followings.stream().map(following -> following.getTarget()).collect(Collectors.toList());
        });
    }


    /***
     * 关注操作
     * @param origin 关注发起人
     * @param target 关注的被关注人
     * @param alias 在本地的被关注人的备注别名
     * @return 被关注人的信息
     */
    public static User follow(final User origin, final User target, final String alias) {
        UserFollow follow = getUserFollow(origin, target);
        if (follow != null) {
            return follow.getTarget();
        }

        return Hib.query(session -> {
            //想要操作懒加载的数据，再load一次
            session.load(origin, origin.getId());
            session.load(target, target.getId());

            //我关注人的时候，同时他也关注我，所以需要添加两条数据
            UserFollow originFollowing = new UserFollow();
            originFollowing.setOrigin(origin);
            originFollowing.setTarget(target);
            originFollowing.setAlias(alias);

            UserFollow targetFollowing = new UserFollow();
            targetFollowing.setOrigin(target);
            targetFollowing.setTarget(origin);

            session.save(originFollowing);
            session.save(targetFollowing);

            return target;
        });
    }


    /***
     * 查询关注关系
     * @param origin 发起者
     * @param target 被关注人
     * @return 中间类关系信息
     */
    public static UserFollow getUserFollow(final User origin, final User target) {
        return Hib.query(session -> {
            return (UserFollow) session.createQuery("from UserFollow where originId =:originId and targetId = : targetId")
                    .setParameter("originId", origin.getId())
                    .setParameter("targetId", target.getId())
                    .setMaxResults(1)
                    .uniqueResult();
        });
    }


    /***
     * 搜索联系人的实现
     * @param name 查询的name，允许为空
     * @return 查询到的用户集合，如果name为空，则返回最近的用户
     */
    public static List<User> search(String name) {
        if (Strings.isNullOrEmpty(name)) {
            //保证不能为null的情况下，减少后面的判断和额外的错误情况
            name = "";
        }
        String searchName = "%" + name + "%";//模糊匹配

        return Hib.query(session -> {
            return session.createQuery("from User where lower(name) like :name and portrait is not null and description is not null")
                    .setParameter("name", searchName)
                    .setMaxResults(20)
                    .list();
        });
    }
}
