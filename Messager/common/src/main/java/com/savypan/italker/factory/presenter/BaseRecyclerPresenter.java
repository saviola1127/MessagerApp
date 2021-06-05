package com.savypan.italker.factory.presenter;

import android.support.v7.util.DiffUtil;

import com.savypan.italker.common.widget.recycler.RecyclerAdapter;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.util.List;

/***
 * 对recyclerview进行的一个简单的presenter封装
 * @param <ViewModel>
 * @param <View>
 */
public class BaseRecyclerPresenter<ViewModel, View extends BaseContract.IRecyclerView> extends BasePresenter<View> {
    public BaseRecyclerPresenter(View view) {
        super(view);
    }

    protected void refreshData(final List<ViewModel> modelList) {
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                View view = getView();
                if (view == null) {
                    return;
                }

                //基本的更新数据并且刷新界面
                RecyclerAdapter<ViewModel> adapter = view.getRecyclerViewAdapter();
                adapter.replace(modelList);
                view.onAdapterDataChanged();
            }
        });
    }


    /***
     * 刷新界面操作，该操作可以保证执行方法在主线程进行
     * diff差异的结果集合需要在子线程计算
     * @param result
     * @param modelList
     */
    protected void refreshDataWithDiff(final DiffUtil.DiffResult result, final List<ViewModel> modelList) {
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                refreshDataOnUIThread(result, modelList);
            }
        });
    }


    private void refreshDataOnUIThread(final DiffUtil.DiffResult result, final List<ViewModel> modelList) {
        View view = getView();
        if (view == null) {
            return;
        }
        RecyclerAdapter<ViewModel> adapter = view.getRecyclerViewAdapter();

        //改变数据集合并不通知界面刷新
        adapter.getItems().clear();
        adapter.getItems().addAll(modelList);
        //通知界面刷新占位布局
        view.onAdapterDataChanged();

        result.dispatchUpdatesTo(adapter);
    }
}
