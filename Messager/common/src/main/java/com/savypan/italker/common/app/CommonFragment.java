package com.savypan.italker.common.app;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class CommonFragment extends Fragment {

    protected View mRoot;
    protected Unbinder mUnbinder;

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

}