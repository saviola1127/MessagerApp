package com.savypan.italker.push.fragment.search;


import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.savypan.italker.common.app.PresenterFragment;
import com.savypan.italker.common.widget.EmptyView;
import com.savypan.italker.common.widget.PortraitView;
import com.savypan.italker.common.widget.recycler.RecyclerAdapter;
import com.savypan.italker.factory.model.card.UserCard;
import com.savypan.italker.factory.presenter.contact.FollowingContract;
import com.savypan.italker.factory.presenter.contact.FollowingPresenter;
import com.savypan.italker.factory.presenter.search.SearchContract;
import com.savypan.italker.factory.presenter.search.SearchUserPresenter;
import com.savypan.italker.push.R;
import com.savypan.italker.push.activities.PersonalActivity;
import com.savypan.italker.push.activities.SearchActivity;

import net.qiujuer.genius.ui.Ui;
import net.qiujuer.genius.ui.compat.UiCompat;
import net.qiujuer.genius.ui.drawable.LoadingCircleDrawable;
import net.qiujuer.genius.ui.drawable.LoadingDrawable;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchUserFragment} factory method to
 * create an instance of this fragment.
 */
public class SearchUserFragment extends PresenterFragment<SearchContract.IPresenter>
        implements SearchActivity.ISearchFragment, SearchContract.IUserView {

    @BindView(R.id.id_rv_user)
    RecyclerView recyclerView;

    @BindView(R.id.empty_view)
    EmptyView emptyView;

    private RecyclerAdapter<UserCard> adapter;
    private static final String TAG = SearchUserFragment.class.getSimpleName();

    @Override
    protected SearchContract.IPresenter initPresenter() {
        return new SearchUserPresenter(this);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_search_user;
    }


    @Override
    protected void initData() {
        super.initData();

        //发起首次搜索
        search("");
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        //初始化RecyclerView的布局方向
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter = new RecyclerAdapter<UserCard>() {
            @Override
            protected ViewHolder<UserCard> onCreateViewHolder(View view, int viewType) {
                return new SearchUserFragment.ViewHolder(view);
            }

            @Override
            protected int getItemViewType(int position, UserCard data) {
                //其实是返回布局cell的id
                return R.layout.cell_search_list;
            }
        });

        emptyView.bind(recyclerView);
        setPlaceHolderView(emptyView);
    }

    @Override
    public void search(String content) {
        myPresenter.search(content);
    }

    @Override
    public void onSearchDone(List<UserCard> userCards) {
        //搜索成功的时候返回
        adapter.replace(userCards);

        //有数据就ok，没有就显示
        mPlaceHolderView.triggerOkOrEmpty(adapter.getItemCount() > 0);
    }

    /***
     * 每一个cell的布局操作
     */
    class ViewHolder extends RecyclerAdapter.ViewHolder<UserCard> implements FollowingContract.IView {

        @BindView(R.id.pv_portrait)
        PortraitView portraitView;

        @BindView(R.id.tv_name)
        TextView name;

        @BindView(R.id.im_following)
        ImageView following;

        private FollowingContract.IPresenter presenter;

        public ViewHolder(View itemView){
            super(itemView);
            setPresenter(new FollowingPresenter(this));
        }

        @Override
        protected void onBind(UserCard data) {
            portraitView.setup(Glide.with(SearchUserFragment.this), data);

            name.setText(data.getName());
            Log.e(TAG, "Data details =>" + data.getName() + "/" + data.isFollowed());
            following.setEnabled(!data.isFollowed());
        }

        @OnClick(R.id.pv_portrait)
        void onPortraitClick() {
            //打开PersonalActivity
            PersonalActivity.show(getContext(), mData.getId());
        }


        @OnClick(R.id.im_following)
        void onFollowClick() {
            //发起关注
            presenter.follow(mData.getId());
        }

        @Override
        public void onFollowSuccess(UserCard card) {
            if (following.getDrawable() instanceof LoadingDrawable) {
                ((LoadingDrawable) following.getDrawable()).stop();
                //设置为默认的
                following.setImageResource(R.drawable.sel_opt_done_add);
            }

            updateData(card);
        }

        @Override
        public void showError(int str) {
            if (following.getDrawable() instanceof LoadingDrawable) {
                //失败则停止动画，并且显示一个圆圈
                LoadingDrawable drawable = (LoadingDrawable) following.getDrawable();
                drawable.setProgress(1);
                drawable.stop();
            }
        }

        @Override
        public void showLoading() {
            int minSize = (int) Ui.dipToPx(getResources(), 22);
            int maxSize = (int) Ui.dipToPx(getResources(), 30);
            //初始化一个圆形的动画的Drawable
            LoadingDrawable drawable = new LoadingCircleDrawable(minSize, maxSize);
            drawable.setBackgroundColor(0);

            int[] color = new int[]{UiCompat.getColor(getResources(), R.color.white_alpha_128)};
            drawable.setForegroundColor(color);
            following.setImageDrawable(drawable);
            drawable.start();
        }

        @Override
        public void setPresenter(FollowingContract.IPresenter presenter) {
            this.presenter = presenter;
        }
    }
}