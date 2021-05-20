package com.savypan.italker.factory.presenter.contact;

import com.savypan.italker.common.widget.recycler.RecyclerAdapter;
import com.savypan.italker.factory.model.db.User;
import com.savypan.italker.factory.presenter.BaseContract;

import java.util.List;

public interface ContactContract {
    interface IPresenter extends BaseContract.IPresenter {

    }

    //都在基类里头完成了
    interface IView extends BaseContract.IRecyclerView<User, IPresenter> {

    }
}
