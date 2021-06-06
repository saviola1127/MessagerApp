package com.savypan.italker.factory.data;

import android.support.annotation.NonNull;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;
import com.savypan.italker.factory.data.helper.DBHelper;
import com.savypan.italker.factory.model.db.BaseDBModel;
import com.savypan.italker.factory.model.db.User;
import com.savypan.italker.factory.persistence.Account;
import net.qiujuer.genius.kit.reflect.Reflector;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

/***
 * 基础的数据库监听类
 */
public abstract class BaseDBRepository<T extends BaseDBModel<T>> implements DBDataSource<T>, DBHelper.IChangedListener<T>,
        QueryTransaction.QueryResultListCallback<T> {

    private final List<T> dataSet = new LinkedList<T>();
    private SuccessCallback<List<T>> callback;
    private Class<T> tClass;

    public BaseDBRepository() {
        //拿当前类的泛型数据信息
        Type[] actualTypeArguments = Reflector.getActualTypeArguments(BaseDBRepository.class, this.getClass());
        tClass = (Class<T>) actualTypeArguments[0];
    }

    @Override
    public void load(SuccessCallback<List<T>> callback) {
        this.callback = callback;
        addDBChangedListener();
    }

    @Override
    public void dispose() {
        this.callback = null;
        DBHelper.removeChangedListener(tClass, this);
        dataSet.clear();
    }

    //数据库框架通知，统一通知的地方
    @Override
    public void onDataSaved(T... list) {
        boolean isChanged = false;
        // 当数据库数据变更的操作
        for (T data : list) {
            // 是我关注的用户同时不是我自己
            if (isQualifiedUser(data)) {
                insertOrUpdate(data);
                isChanged = true;
            }
        }

        if (isChanged == true) {
            notifyDataChange();
        }
    }

    @Override
    public void onDataDeleted(T... list) {
        // 当数据库数据删除的操作
        boolean isRemoved = false;
        for (T data : list) {
            boolean remove = dataSet.remove(data);
            if (remove == true) {
                isRemoved = true;
            }
        }

        if (isRemoved == true) {
            notifyDataChange();
        }
    }

    //DB flow框架通知的回调
    @Override
    public void onListQueryResult(QueryTransaction transaction, @NonNull List<T> tResult) {
        if (tResult.size() == 0) {
            dataSet.clear();
            notifyDataChange();
            return;
        }

        T[] users = com.savypan.italker.common.utils.CollectionUtil.toArray(tResult, tClass);
        onDataSaved(users);
    }

    // 过滤方法来识别哪些关注对象需要触发
    protected abstract boolean isQualifiedUser(T data);


    //刷新数据，让View这边来调用
    private void notifyDataChange() {
        if (callback != null) {
            callback.onDataLoaded(dataSet);
        }
    }


    private void insertOrUpdate(T data) {
        int index = indexOf(data);
        if (index >= 0) {
            // 替换操作
            replace(data, index);
        } else {
            // 插入操作
            insert(data);
        }
    }


    private void insert(T data) {
        dataSet.add(data);
    }


    private void replace(T data, int index) {
        dataSet.remove(index);
        dataSet.add(index, data);
    }


    private int indexOf(T data) {
        int index = -1;
        for (T t : dataSet) {
            index++;
            if (t.isSame(data)) {
                return index;
            }
        }
        return -1;
    }


    protected void addDBChangedListener() {
        DBHelper.addChangedListener(tClass, this);
    }
}
