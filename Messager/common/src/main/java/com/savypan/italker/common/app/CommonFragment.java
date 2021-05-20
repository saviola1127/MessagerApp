package com.savypan.italker.common.app;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.savypan.italker.common.widget.convention.PlaceHolderView;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class CommonFragment extends Fragment {

    protected View mRoot;
    protected Unbinder mUnbinder;
    protected PlaceHolderView mPlaceHolderView;

    //是否是第一次初始化数据
    protected boolean isFirstDataInit = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (mRoot == null) {
            int resId = getContentLayoutId();
            //init current root layout, but not ready for attaching to root layout
            View view = inflater.inflate(resId, container, false);
            initWidget(view);
            mRoot = view;
        } else {
            if (mRoot.getParent() != null) {
                //put current root away from parent layout
                ((ViewGroup)mRoot.getParent()).removeView(mRoot);
            }
        }
        return mRoot;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        //init args
        initArgs(getArguments());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (isFirstDataInit == true) {
            isFirstDataInit = false;
            initFirstData();
        }
        initData();
    }

    protected abstract int getContentLayoutId();

    /***
     * init View/widget
     */
    protected void initWidget(View root) {
        mUnbinder = ButterKnife.bind(this, root);

    }

    /***
     * init data
     */
    protected void initData() {

    }


    /***
     * init data of first time
     */
    protected void initFirstData() {

    }


    /***
     * init related parameters
     * @param bundle
     * @return
     */
    protected void initArgs(Bundle bundle) {

    }

    /***
     * call this function when back pressed
     * @return
     * true - already handled, activity won't care about this
     * false - not handled yet, activity needs to take action
     */
    public boolean onBackPressed() {

        return false;
    }

    /**
     * 设置占位布局
     * @param placeHolderView 继承了占位布局规范的view
     */
    public void setPlaceHolderView(PlaceHolderView placeHolderView) {
        this.mPlaceHolderView = placeHolderView;
    }

}
