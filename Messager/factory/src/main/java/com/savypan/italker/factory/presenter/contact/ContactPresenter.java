package com.savypan.italker.factory.presenter.contact;

import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.util.Log;
import android.view.View;

import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.CursorResult;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ITransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;
import com.savypan.italker.common.widget.recycler.RecyclerAdapter;
import com.savypan.italker.factory.data.DBDataSource;
import com.savypan.italker.factory.data.IDataSource;
import com.savypan.italker.factory.data.helper.UserHelper;
import com.savypan.italker.factory.data.user.ContactDataSource;
import com.savypan.italker.factory.data.user.ContactRepository;
import com.savypan.italker.factory.model.card.UserCard;
import com.savypan.italker.factory.model.db.AppDatabase;
import com.savypan.italker.factory.model.db.User;
import com.savypan.italker.factory.model.db.User_Table;
import com.savypan.italker.factory.persistence.Account;
import com.savypan.italker.factory.presenter.BasePresenter;
import com.savypan.italker.factory.presenter.BaseRecyclerPresenter;
import com.savypan.italker.factory.presenter.BaseSourcePresenter;
import com.savypan.italker.factory.utils.DiffUiDataCallback;

import java.util.ArrayList;
import java.util.List;

public class ContactPresenter extends BaseSourcePresenter<User, User, ContactDataSource, ContactContract.IView>
        implements ContactContract.IPresenter, IDataSource.SuccessCallback<List<User>> {

    private static final String TAG = ContactContract.class.getSimpleName();

    public ContactPresenter(ContactContract.IView view) {
        super(view, new ContactRepository());
    }


    @Override
    public void start() {
        super.start();

        //??????????????????????????????????????????
        //source.load(this);

        // load ????????????
        UserHelper.refreshContacts();
    }

    private void diff(List<User> newList, List<User> oldList) {

        //??????????????????
        DiffUtil.Callback callback = new DiffUiDataCallback<User>(oldList, newList);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);

        //???????????????????????????????????????
        getView().getRecyclerViewAdapter().replace(newList);

        //??????????????????
        result.dispatchUpdatesTo(getView().getRecyclerViewAdapter());
        getView().onAdapterDataChanged();
    }

    //??????????????????diff???????????????
    @Override
    public void onDataLoaded(List<User> users) {
        // ???????????????????????????????????????????????????????????????
        final ContactContract.IView view = getView();
        if (view == null) {
            return;
        }

        RecyclerAdapter adapter = view.getRecyclerViewAdapter();
        List<User> oldUsers = adapter.getItems();

        DiffUtil.Callback callback = new DiffUiDataCallback<User>(oldUsers, users);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);

        //??????????????????
        refreshDataWithDiff(result, users);
    }
}
