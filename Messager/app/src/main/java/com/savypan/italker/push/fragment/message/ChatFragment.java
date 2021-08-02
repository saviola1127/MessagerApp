package com.savypan.italker.push.fragment.message;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.savypan.italker.common.app.CommonFragment;
import com.savypan.italker.common.app.PresenterFragment;
import com.savypan.italker.common.widget.PortraitView;
import com.savypan.italker.common.widget.adapter.TextWaterAdapter;
import com.savypan.italker.common.widget.recycler.RecyclerAdapter;
import com.savypan.italker.factory.model.db.Message;
import com.savypan.italker.factory.model.db.User;
import com.savypan.italker.factory.persistence.Account;
import com.savypan.italker.factory.presenter.mesage.ChatContract;
import com.savypan.italker.push.R;
import com.savypan.italker.push.activities.MessageActivity;
import net.qiujuer.genius.ui.compat.UiCompat;
import net.qiujuer.genius.ui.widget.Loading;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatFragment} factory method to
 * create an instance of this fragment.
 */
public abstract class ChatFragment<InitModel>
        extends PresenterFragment<ChatContract.IPresenter>
        implements AppBarLayout.OnOffsetChangedListener, ChatContract.IView<InitModel> {

    protected String receiverId;
    protected Adapter adapter;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.rv)
    RecyclerView recyclerView;

    @BindView(R.id.appBar)
    AppBarLayout appBarLayout;

    @BindView(R.id.et_message)
    EditText message;

    @BindView(R.id.iv_submit)
    ImageView submit;

    @Override
    protected void initArgs(Bundle bundle) {
        super.initArgs(bundle);
        receiverId = bundle.getString(MessageActivity.KEY_RECEIVER_ID);
    }


    @Override
    protected void initWidget(View root) {
        super.initWidget(root);

        initToolbar();
        initAppBar();
        initEditMsg();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new Adapter();
        recyclerView.setAdapter(adapter);
    }


    @Override
    protected void initData() {
        super.initData();

        myPresenter.start();
    }

    //初始化toolbar
    protected void initToolbar() {
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }


    private void initEditMsg() {
        message.addTextChangedListener(new TextWaterAdapter() {
            @Override
            public void afterTextChanged(Editable s) {
                String content = s.toString().trim();
                boolean needSendMsg = !TextUtils.isEmpty(content);
                submit.setActivated(needSendMsg);
            }
        });
    }

    /***
     * 给AppBar设置一个监听，得到关闭与打开的时候的进度
     */
    private void initAppBar() {
        appBarLayout.addOnOffsetChangedListener(this);
    }

    @OnClick(R.id.iv_face)
    void onFaceClick() {

    }

    @OnClick(R.id.iv_record)
    void onRecordClick() {

    }

    @OnClick(R.id.iv_submit)
    void onSubmitClick() {
        if (submit.isActivated()) {
            //发送消息
            String content = message.getText().toString();
            message.setText("");
            myPresenter.transText(content);
        } else {
            onMoreClick();
        }
    }

    private void onMoreClick() {
        //TODO
    }


    private class Adapter extends RecyclerAdapter<Message> {

        @Override
        protected ViewHolder<Message> onCreateViewHolder(View view, int viewType) {
            switch (viewType) {
                case R.layout.cell_chat_text_left:
                case R.layout.cell_chat_text_right:
                    return new TextHolder(view);

                case R.layout.cell_chat_audio_left:
                case R.layout.cell_chat_audio_right:
                    return new AudioHolder(view);

                case R.layout.cell_chat_pic_left:
                case R.layout.cell_chat_pic_right:
                    return new PicHolder(view);

                default:
                    return new TextHolder(view);
            }
        }

        @Override
        protected int getItemViewType(int position, Message data) {
            boolean isRight = Objects.equals(data.getSender().getId(), Account.getUserId());

            switch (data.getType()) {
                case Message.TYPE_STR:
                    return isRight? R.layout.cell_chat_text_right:R.layout.cell_chat_text_left;

                case Message.TYPE_AUDIO:
                    return isRight? R.layout.cell_chat_audio_right:R.layout.cell_chat_audio_left;

                case Message.TYPE_PIC:
                    return isRight? R.layout.cell_chat_pic_right:R.layout.cell_chat_pic_left;

                default:
                    return isRight? R.layout.cell_chat_text_right:R.layout.cell_chat_text_left;
            }
        }
    }


    @Override
    public RecyclerAdapter<Message> getRecyclerViewAdapter() {
        return adapter;
    }


    @Override
    public void onAdapterDataChanged() {
        //界面没有占位布局, RecyclerView是一直显示的，所以不需要做任何事情
    }

    //Holder的基类
    class BaseHolder extends RecyclerAdapter.ViewHolder<Message> {

        @BindView(R.id.im_pv)
        PortraitView portraitView;

        @Nullable
        @BindView(R.id.loading)
        Loading loading;

        public BaseHolder(@NonNull View itemView) {
            super(itemView);
        }

        @OnClick(R.id.im_pv)
        void onRepushClick() {
            //重新发送点击
            if (loading != null) {
                //必须是在右边，同时这样的发送失败，才能重新发送 TODO
            }
        }

        @Override
        protected void onBind(Message data) {
            User sender = data.getSender();
            sender.load(); //懒加载

            portraitView.setup(Glide.with(ChatFragment.this), sender);

            if (loading != null) {
                //当前布局应该是在右边
                if (data.getStatus() == Message.STATUS_DONE) {
                    //隐藏loading
                    loading.stop();
                    loading.setVisibility(View.GONE);
                } else if (data.getStatus() == Message.STATUS_CREATED) {
                    //正在发送中的状态
                    loading.setVisibility(View.VISIBLE);
                    loading.setProgress(0);
                    loading.setForegroundColor(UiCompat.getColor(getResources(), R.color.colorAccent));
                    loading.start();
                } else if (data.getStatus() == Message.STATUS_FAILED) {
                    //发送失败，允许重新发送
                    loading.setVisibility(View.VISIBLE);
                    loading.stop();
                    loading.setProgress(1);
                    loading.setForegroundColor(UiCompat.getColor(getResources(), R.color.alertImportant));
                }

                portraitView.setEnabled(data.getStatus() == Message.STATUS_FAILED);
            }
        }
    }


    //文字的holder
    class TextHolder extends BaseHolder {

        @BindView(R.id.txt_content)
        TextView content;

        public TextHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(Message data) {
            super.onBind(data);
            //把内容设置到布局上
            content.setText(data.getContent());
        }
    }

    //Audio的holder
    class AudioHolder extends BaseHolder {

        public AudioHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(Message data) {
            super.onBind(data);
            //TODO
        }
    }

    //Image的holder
    class PicHolder extends BaseHolder {

        public PicHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(Message data) {
            super.onBind(data);
            //TODO
        }
    }
}