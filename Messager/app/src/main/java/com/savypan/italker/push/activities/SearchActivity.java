package com.savypan.italker.push.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.savypan.italker.common.app.ToolbarActivity;
import com.savypan.italker.push.R;
import com.savypan.italker.push.fragment.search.SearchGroupFragment;
import com.savypan.italker.push.fragment.search.SearchUserFragment;

public class SearchActivity extends ToolbarActivity {
    private static final String EXTRA_TYPE = "EXTRA_TYPE";
    public static final int TYPE_USER = 1; //搜索人
    public static final int TYPE_GROUP = 2; //搜索群
    private static final String TAG = SearchActivity.class.getSimpleName();

    private int type;
    private ISearchFragment searchFragment;

    public static void show(Context context, int type) {
        Intent intent = new Intent(context, SearchActivity.class);
        intent.putExtra(EXTRA_TYPE, type);
        context.startActivity(intent);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_search;
    }


    @Override
    protected boolean initArgs(Bundle bundle) {
        type = bundle.getInt(EXTRA_TYPE);

        return type == TYPE_USER || type == TYPE_GROUP;
    }


    @Override
    protected void initWidget() {
        super.initWidget();

        //显示对应的fragment
        Fragment fragment;
        if (type == TYPE_USER) {
            SearchUserFragment searchUserFragment = new SearchUserFragment();
            fragment = searchUserFragment;
            searchFragment = searchUserFragment;
            Log.e(TAG, "initWidget as UserFragment");
        } else {
            SearchGroupFragment searchGroupFragment = new SearchGroupFragment();
            fragment = searchGroupFragment;
            searchFragment = searchGroupFragment;
            Log.e(TAG, "initWidget as GroupFragment");
        }

        getSupportFragmentManager().beginTransaction()
                .add(R.id.search_container, fragment)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //初始化菜单
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        if (searchView != null) {
            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    //当点击了提交按钮的时候
                    search(query);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    //当文字改变的时候，咱们不会及时搜索，只在为null的时候进行搜索
                    if (TextUtils.isEmpty(s)) {
                        search("");
                        return true;
                    }
                    return false;
                }
            });
        }

        return super.onCreateOptionsMenu(menu);
    }


    /***
     * 搜索的trigger point
     * @param query
     */
    private void search(String query) {
        if (searchFragment == null) {
            return;
        }
        searchFragment.search(query);
    }


    public interface ISearchFragment{
        void search(String content);
    }
}