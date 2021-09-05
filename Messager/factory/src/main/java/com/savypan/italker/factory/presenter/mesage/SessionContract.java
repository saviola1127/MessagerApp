package com.savypan.italker.factory.presenter.mesage;

import com.savypan.italker.factory.model.db.Session;
import com.savypan.italker.factory.presenter.BaseContract;

public interface SessionContract {
    interface IPresenter extends BaseContract.IPresenter {

    }

    //都在基类里头完成了
    interface IView extends BaseContract.IRecyclerView<Session, IPresenter> {

    }
}
