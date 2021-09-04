package com.savypan.italker.factory.presenter.mesage;

import android.support.v7.util.DiffUtil;

import com.savypan.italker.factory.data.helper.MessageHelper;
import com.savypan.italker.factory.data.message.MessageDataSource;
import com.savypan.italker.factory.model.api.message.MessageCreationModel;
import com.savypan.italker.factory.model.db.Message;
import com.savypan.italker.factory.persistence.Account;
import com.savypan.italker.factory.presenter.BaseSourcePresenter;
import com.savypan.italker.factory.utils.DiffUiDataCallback;

import java.util.List;

/***
 * 聊天presenter的基础类
 */
public class ChatPresenter<View extends ChatContract.IView>
        extends BaseSourcePresenter<Message, Message, MessageDataSource, View>
        implements ChatContract.IPresenter{

    //接收者ID，可能是群或者人的ID
    protected String receiverId;
    //区分是人或者是群ID
    protected int receiverType;

    public ChatPresenter(View view, MessageDataSource source, String receiverId, int receiverType) {
        super(view, source);
        this.receiverId = receiverId;
        this.receiverType = receiverType;
    }

    @Override
    public void onDataLoaded(List<Message> messages) {
        ChatContract.IView view = getView();
        if (view == null) {
            return;
        }

        //拿到老数据
        List<Message> old = view.getRecyclerViewAdapter().getItems();

        DiffUiDataCallback<Message> callback = new DiffUiDataCallback<>(old, messages);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);

        refreshDataWithDiff(result, messages);
    }

    @Override
    public void transText(String content) {
        //构建一个新的消息
        MessageCreationModel model = new MessageCreationModel.Builder()
                .receiver(receiverId, receiverType)
                .content(content, Message.TYPE_STR)
                .build();

        //进行网络发送
        MessageHelper.push(model);
    }

    @Override
    public void transAudio(String path) {
        //TODO
    }

    @Override
    public void transImage(String[] paths) {
        //TODO
    }

    @Override
    public boolean isRePush(Message message) {
        //确定消息是可以重复发送的，而且处于登录状态，并且发送者是我本人
        if (message.getSender().getId().equalsIgnoreCase(Account.getUserId())
        && message.getStatus() == Message.STATUS_FAILED) {

            message.setStatus(Message.STATUS_CREATED);
            MessageCreationModel model = MessageCreationModel.buildwithMessage(message);
            MessageHelper.push(model);
            return true;
        }
        return false;
    }
}
