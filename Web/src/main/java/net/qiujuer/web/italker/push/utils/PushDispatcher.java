package net.qiujuer.web.italker.push.utils;

import com.gexin.rp.sdk.base.IBatch;
import com.gexin.rp.sdk.base.IIGtPush;
import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.TransmissionTemplate;
import com.google.common.base.Strings;
import net.qiujuer.web.italker.push.bean.api.base.PushModel;
import net.qiujuer.web.italker.push.bean.db.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 消息依赖工具类
 * Created by savypan
 * On 2021/6/13 16:45
 */
public class PushDispatcher {

    private static final String appId = "xfeA8x9Lzs6HadCFEEHK99";
    private static final String appKey = "qA9SU0fxi2AtKcGZo0NDZ";
    private static final String masterSecret = "SlsCBnlgj47UKBpYHeUfV5";
    private static final String host = "http://sdk.open.api.igexin.com/apiex.htm";

    private final IIGtPush pusher;

    //要收到消息的人和内容的列表
    private List<BatchBean> beans = new ArrayList<>();

    public PushDispatcher() {
        pusher = new IGtPush(host, appKey, masterSecret);
    }


    /***
     * 添加一条消息
     * @param receiver 接收者
     * @param model 接收的推送的数据model
     * @return
     */
    public boolean add(User receiver, PushModel model) {
        // 基础检查，必须要接收者的用户设备ID
        if (receiver == null || model == null || Strings.isNullOrEmpty(receiver.getPushId())) {
            return false;
        }

        String pushString = model.getPushString();
        if (Strings.isNullOrEmpty(pushString)) {
            return false;
        }

        //构建一个目标加内容
        BatchBean batchBean = buildMessage(receiver.getPushId(), pushString);
        beans.add(batchBean);
        return true;
    }


    /***
     * 对要发送的数据进行格式化封装
     * @param clientId
     * @param text
     * @return
     */
    private BatchBean buildMessage(String clientId, String text) {
        //构建透传消息, 不是通知栏显示，而是在广播接收器处理
        TransmissionTemplate template = new TransmissionTemplate();
        SingleMessage message = new SingleMessage();

        template.setAppId(appId);
        template.setAppkey(appKey);
        template.setTransmissionContent(text);
        template.setTransmissionType(0); // 这个Type为int型，填写1则自动启动app

        message.setData(template); //把透传消息设置到单消息模板中
        message.setOffline(true); //是否运行离线发送
        message.setOfflineExpireTime(1 * 1000); // 设置离线消息时长 (在此时间之内，都可以收到消息)

        //设置推送目标，填入appId和clientId
        Target target = new Target();
        target.setAppId(appId);
        target.setClientId(clientId);

        return new BatchBean(message, target);
    }



    /***
     * 处理消息的最终发送
     */
    public boolean submit() {
        IBatch batch = pusher.getBatch();
        boolean hasData = false;

        for (BatchBean bean : beans) {
            try {
                batch.add(bean.message, bean.target);
                hasData = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (hasData == false) {
            //如果没有数据，直接返回
            return false;
        }

        IPushResult result = null;
        try {
            result = batch.submit();
        } catch (IOException e) {
            e.printStackTrace();

            try {
                batch.retry();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        if (result != null) {
            try {
                Logger.getLogger("PushDispatcher").log(Level.INFO, (String) result.getResponse().get("result"));
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Logger.getLogger("PushDispatcher").log(Level.WARNING, "推送服务器响应异常");
        return false;
    }


    //给每个人发送消息的一个bean封装
    private static class BatchBean {
        SingleMessage message;
        Target target;

        public BatchBean(SingleMessage message, Target target) {
            this.message = message;
            this.target = target;
        }
    }
}
