package com.savypan.italker.factory.presenter.mesage;

import com.savypan.italker.factory.model.db.Group;
import com.savypan.italker.factory.model.db.Message;
import com.savypan.italker.factory.model.db.User;
import com.savypan.italker.factory.presenter.BaseContract;

public interface ChatContract {
    interface IPresenter extends BaseContract.IPresenter {
        void transText(String content);
        void transAudio(String path);
        void transImage(String[] paths);
        boolean isRetrans(Message message); //resend a message, return if it is being distributed
    }

    interface IView<InitModel> extends BaseContract.IRecyclerView<Message, IPresenter> {
        void onInit(InitModel model);
    }

    //人聊天的界面
    interface IUserView extends IView<User> {

    }

    //群聊天的界面
    interface IGroupView extends IView<Group> {

    }
}
