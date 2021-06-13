package com.savypan.italker.factory.presenter;

import com.savypan.italker.factory.data.DBDataSource;
import com.savypan.italker.factory.data.IDataSource;

import java.util.List;

/***
 * 基础的仓库源的presenter
 */
public abstract class BaseSourcePresenter<T, ViewModel, Source extends DBDataSource<T>, View extends BaseContract.IRecyclerView>
        extends BaseRecyclerPresenter<ViewModel, View>
        implements IDataSource.SuccessCallback<List<T>> {

    protected Source source;

    public BaseSourcePresenter(View view, Source source) {
        super(view);
        this.source = source;
    }


    @Override
    public void start() {
        super.start();
        if (source != null) {
            //进行本地的数据加载并添加监听
            source.load(this);
        }
    }


    @Override
    public void destroy() {
        super.destroy();
        source.dispose();
        source = null;
    }
}
