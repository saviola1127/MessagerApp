package com.savypan.italker.factory.presenter.mesage;

import com.savypan.italker.factory.data.message.MessageDataSource;
import com.savypan.italker.factory.model.db.Message;
import com.savypan.italker.factory.presenter.BaseSourcePresenter;

import java.util.List;

/***
 * 聊天presenter的基础类
 */
public class ChatPresenter<View extends ChatContract.IView>
        extends BaseSourcePresenter<Message, Message, MessageDataSource, View>
        implements ChatContract.IPresenter{

    public ChatPresenter(View view, MessageDataSource source) {
        super(view, source);
    }

    @Override
    public void onDataLoaded(List<Message> messages) {

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
