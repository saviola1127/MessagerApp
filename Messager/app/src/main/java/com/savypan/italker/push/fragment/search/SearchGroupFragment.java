package com.savypan.italker.push.fragment.search;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.savypan.italker.common.app.PresenterFragment;
import com.savypan.italker.factory.model.card.GroupCard;
import com.savypan.italker.factory.presenter.BaseContract;
import com.savypan.italker.factory.presenter.search.SearchContract;
import com.savypan.italker.factory.presenter.search.SearchGroupPresenter;
import com.savypan.italker.push.R;
import com.savypan.italker.push.activities.SearchActivity;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchGroupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchGroupFragment extends PresenterFragment<SearchContract.IPresenter>
        implements SearchActivity.ISearchFragment, SearchContract.IGroupView {





    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_search_group;
    }

    @Override
    public void search(String content) {
        myPresenter.search(content);
    }

    @Override
    protected SearchContract.IPresenter initPresenter() {
        return new SearchGroupPresenter(this);
    }

    @Override
    public void onSearchDone(List<GroupCard> groupCards) {

    }
}