package com.savypan.italker.push.fragment.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.savypan.italker.common.app.CommonFragment;
import com.savypan.italker.common.app.PresenterFragment;
import com.savypan.italker.common.widget.EmptyView;
import com.savypan.italker.common.widget.GalleryView;
import com.savypan.italker.common.widget.PortraitView;
import com.savypan.italker.common.widget.recycler.RecyclerAdapter;
import com.savypan.italker.factory.model.db.Session;
import com.savypan.italker.factory.model.db.User;
import com.savypan.italker.factory.presenter.mesage.SessionContract;
import com.savypan.italker.factory.presenter.mesage.SessionPresenter;
import com.savypan.italker.push.R;
import com.savypan.italker.push.activities.MessageActivity;
import com.savypan.italker.push.activities.PersonalActivity;
import com.savypan.italker.utils.DateTimeUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class ActiveFragment extends PresenterFragment<SessionContract.IPresenter>
implements SessionContract.IView{

    @BindView(R.id.recycler)
    RecyclerView recyclerView;

    @BindView(R.id.emptyView)
    EmptyView emptyView;

    private RecyclerAdapter<Session> adapter; //可以直接从数据库查询数据

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_active;
    }

    @Override
    protected void initData() {
        super.initData();
    }


    @Override
    protected void initWidget(View root) {
        super.initWidget(root);

        //初始化RecyclerView的布局方向
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter = new RecyclerAdapter<Session>() {
            @Override
            protected ViewHolder<Session> onCreateViewHolder(View view, int viewType) {
                return new ActiveFragment.ViewHolder(view);
            }

            @Override
            protected int getItemViewType(int position, Session data) {
                //其实是返回布局cell的id
                return R.layout.cell_chat_list;
            }
        });

        //adapter的时间侦听
        adapter.setListener(new RecyclerAdapter.AdapterListenerImpl<Session>() {
            @Override
            public void onItemClick(RecyclerAdapter.ViewHolder viewHolder, Session data) {
                super.onItemClick(viewHolder, data);
                MessageActivity.show(getContext(), data);
            }
        });

        emptyView.bind(recyclerView);
        setPlaceHolderView(emptyView);
    }


    @Override
    protected void initFirstData() {
        super.initFirstData();
        myPresenter.start();
    }


    @Override
    protected SessionContract.IPresenter initPresenter() {
        return new SessionPresenter(this);
    }

    @Override
    public RecyclerAdapter<Session> getRecyclerViewAdapter() {
        return adapter;
    }

    @Override
    public void onAdapterDataChanged() {
        mPlaceHolderView.triggerOkOrEmpty(adapter.getItemCount() > 0);
    }

    class ViewHolder extends RecyclerAdapter.ViewHolder<Session> {

        @BindView(R.id.im_avatar)
        PortraitView portraitView;

        @BindView(R.id.tv_receiver)
        TextView name;

        @BindView(R.id.tv_content)
        TextView content;

        @BindView(R.id.tv_time)
        TextView time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(Session data) {
            portraitView.setup(Glide.with(ActiveFragment.this), data.getPicture());
            name.setText(data.getTitle());
            content.setText(TextUtils.isEmpty(data.getContent())? "":data.getContent());
            time.setText(DateTimeUtils.getSampleDate(data.getModifyAt()));
        }

        @OnClick(R.id.im_avatar)
        void onPortraitClick() {
            //打开PersonalActivity
            PersonalActivity.show(getContext(), mData.getId());
        }
    }
}