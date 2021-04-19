package net.qiujuer.web.italker.push.bean.api.account;

import com.google.gson.annotations.Expose;
import net.qiujuer.web.italker.push.bean.card.UserCard;
import net.qiujuer.web.italker.push.bean.db.User;

/**
 * 账户部分返回的model
 * Created by savypan
 * On 2021/4/17 16:28
 */
public class AccountRspModel {

    @Expose
    private UserCard user;
    @Expose
    private String account;
    @Expose
    private String token; //token after user finish registration
    @Expose
    private boolean isBound; //if tag is already bound to device pushId


    public AccountRspModel(User user) {
        //默认无绑定
        this(user, false);
    }

    public AccountRspModel(User user, boolean isBound) {
        this.user = new UserCard(user);
        this.account = user.getPhone();
        this.token = user.getToken();
        this.isBound = isBound;
    }
}
