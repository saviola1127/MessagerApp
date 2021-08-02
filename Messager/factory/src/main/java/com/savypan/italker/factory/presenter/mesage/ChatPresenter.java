package com.savypan.italker.factory.presenter.mesage;

import android.support.v7.util.DiffUtil;

import com.savypan.italker.factory.data.message.MessageDataSource;
import com.savypan.italker.factory.model.db.Message;
import com.savypan.italker.factory.presenter.BaseSourcePresenter;
import com.savypan.italker.factory.utils.DiffUiDataCallback;

import java.util.List;

/***
 * 聊天presenter的基础类
 */
public class ChatPresenter<View extends ChatContract.IView>
        extends BaseSourcePresenter<Message, Message, MessageDataSource, View>
        implements ChatContract.IPresenter{

    protected String receiverId;
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

        List<Message> old = view.getRecyclerViewAdapter().getItems();

        DiffUiDataCallback<Message> callback = new DiffUiDataCallback<>(old, messages);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);
        refreshDataWithDiff(result, messages);
    }

    @Override
    public void transText(String content) {

    }

    @Override
    public void transAudio(String path) {

    }

    @Override
    public void transImage(String[] paths) {

    }

    @Override
    public boolean isRetrans(Message message) {
        return false;
    }
}
