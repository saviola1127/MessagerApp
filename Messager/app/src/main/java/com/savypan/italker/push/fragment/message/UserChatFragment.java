package com.savypan.italker.push.fragment.message;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.savypan.italker.common.widget.PortraitView;
import com.savypan.italker.factory.model.db.User;
import com.savypan.italker.factory.presenter.mesage.ChatContract;
import com.savypan.italker.factory.presenter.mesage.UserPresenter;
import com.savypan.italker.push.R;
import com.savypan.italker.push.activities.PersonalActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 用户聊天界面
 * create an instance of this fragment.
 */
public class UserChatFragment extends ChatFragment<User>
implements ChatContract.IUserView {

    @BindView(R.id.im_pv)
    PortraitView portraitView;

    private MenuItem userInfoMenuItem;

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_user_chat;
    }

    //进行高度的综合运算，透明我们的头像和icon
    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (portraitView == null || userInfoMenuItem == null) {
            return;
        }

        if (verticalOffset == 0) {
            //完全展开
            portraitView.setVisibility(View.VISIBLE);
            portraitView.setScaleX(1);
            portraitView.setScaleY(1);
            portraitView.setAlpha(1);

            //隐藏user菜单
            userInfoMenuItem.setVisible(false);
            userInfoMenuItem.getIcon().setAlpha(0);
        } else {
            verticalOffset = Math.abs(verticalOffset);

            final int totalScrollRange = appBarLayout.getTotalScrollRange();
            if (verticalOffset >= totalScrollRange) {
                //设置为关闭状态
                portraitView.setVisibility(View.GONE);
                portraitView.setScaleX(0);
                portraitView.setScaleY(0);
                portraitView.setAlpha(0);

                userInfoMenuItem.setVisible(true);
                userInfoMenuItem.getIcon().setAlpha(255);
            } else {
                //中间状态
                float progress = 1 - verticalOffset/(float) totalScrollRange;
                portraitView.setVisibility(View.VISIBLE);
                portraitView.setScaleX(progress);
                portraitView.setScaleY(progress);
                portraitView.setAlpha(progress);

                userInfoMenuItem.setVisible(false);
                userInfoMenuItem.getIcon().setAlpha(255 - (int) (255*progress));
            }
        }
    }

    @Override
    protected void initToolbar() {
        super.initToolbar();

        Toolbar toolbar = super.toolbar;
        toolbar.inflateMenu(R.menu.chat_user);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.action_person) {
                    onPortraitViewClick();
                }
                return false;
            }
        });

        //get menu Icon
        userInfoMenuItem = toolbar.getMenu().findItem(R.id.action_person);
    }

    @OnClick(R.id.im_pv)
    void onPortraitViewClick() {
        PersonalActivity.show(getContext(), receiverId);
    }

    @Override
    protected ChatContract.IPresenter initPresenter() {
        return new UserPresenter(this, receiverId);
    }

    @Override
    public void onInit(User user) {
        //对和你聊天的用户信息进行初始化

    }
}