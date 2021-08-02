package com.savypan.italker.factory.presenter.mesage;

import com.savypan.italker.factory.data.helper.UserHelper;
import com.savypan.italker.factory.data.message.MessageDataSource;
import com.savypan.italker.factory.data.message.MessageRepository;
import com.savypan.italker.factory.model.db.Message;
import com.savypan.italker.factory.model.db.User;

public class UserPresenter extends ChatPresenter<ChatContract.IUserView> implements ChatContract.IPresenter {

    private User receiver;

    public UserPresenter(ChatContract.IUserView view, String receiverId) {
        //数据源，View，接收者，接收者的类型
        super(view, new MessageRepository(receiverId), receiverId, Message.RECEIVER_TYPE_NONE);
    }


    @Override
    public void start() {
        super.start();

        receiver = UserHelper.findFromLocal(receiverId);
        getView().onInit(receiver);
    }
}
