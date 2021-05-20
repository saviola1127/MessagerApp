package com.savypan.italker.push.fragment.home;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.savypan.italker.common.app.PresenterFragment;
import com.savypan.italker.common.widget.EmptyView;
import com.savypan.italker.common.widget.PortraitView;
import com.savypan.italker.common.widget.recycler.RecyclerAdapter;
import com.savypan.italker.factory.model.db.User;
import com.savypan.italker.factory.presenter.contact.ContactContract;
import com.savypan.italker.factory.presenter.contact.ContactPresenter;
import com.savypan.italker.push.R;
import com.savypan.italker.push.activities.MessageActivity;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ContactFragment#} factory method to
 * create an instance of this fragment.
 */
public class ContactFragment extends PresenterFragment<ContactContract.IPresenter>
implements ContactContract.IView {

    @BindView(R.id.rv_user)
    RecyclerView recyclerView;

    @BindView(R.id.ev)
    EmptyView emptyView;

    private RecyclerAdapter<User> adapter; //可以直接从数据库查询数据

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_contact;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);

        //初始化RecyclerView的布局方向
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter = new RecyclerAdapter<User>() {
            @Override
            protected ViewHolder<User> onCreateViewHolder(View view, int viewType) {
                return new ContactFragment.ViewHolder(view);
            }

            @Override
            protected int getItemViewType(int position, User data) {
                //其实是返回布局cell的id
                return R.layout.cell_contact_list;
            }
        });

        //adapter的时间侦听
        adapter.setListener(new RecyclerAdapter.AdapterListenerImpl<User>() {
            @Override
            public void onItemClick(RecyclerAdapter.ViewHolder viewHolder, User data) {
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
    protected ContactContract.IPresenter initPresenter() {
        return new ContactPresenter(this);
    }

    @Override
    public RecyclerAdapter<User> getRecyclerViewAdapter() {
        return adapter;
    }

    @Override
    public void onAdapterDataChanged() {
        mPlaceHolderView.triggerOkOrEmpty(adapter.getItemCount() > 0);
    }

    class ViewHolder extends RecyclerAdapter.ViewHolder<User> {

        @BindView(R.id.contact_pv)
        PortraitView portraitView;

        @BindView(R.id.tv_name)
        TextView name;

        @BindView(R.id.tv_desc)
        TextView desc;

        @BindView(R.id.tv_chat)
        ImageView chat;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(User data) {
            portraitView.setup(Glide.with(ContactFragment.this), data);
            name.setText(data.getName());
            desc.setText(data.getDes());
        }
    }
}