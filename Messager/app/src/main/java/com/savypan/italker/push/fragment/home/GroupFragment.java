package com.savypan.italker.push.fragment.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.savypan.italker.common.app.CommonFragment;
import com.savypan.italker.push.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GroupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GroupFragment extends CommonFragment {

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_group;
    }
}