package com.savypan.italker.factory.presenter.search;

import com.savypan.italker.factory.presenter.BasePresenter;

/**
 * 搜索群的实现
 */
public class SearchGroupPresenter extends BasePresenter<SearchContract.IGroupView> implements SearchContract.IPresenter {

    public SearchGroupPresenter(SearchContract.IGroupView view) {
        super(view);
    }

    @Override
    public void search(String content) {

    }
}
