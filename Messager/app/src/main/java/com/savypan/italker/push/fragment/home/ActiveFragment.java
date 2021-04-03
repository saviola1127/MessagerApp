package com.savypan.italker.push.fragment.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.savypan.italker.common.app.CommonFragment;
import com.savypan.italker.common.widget.GalleryView;
import com.savypan.italker.push.R;

import butterknife.BindView;

public class ActiveFragment extends CommonFragment {

    @BindView(R.id.gallery_view)
    GalleryView galleryView;

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_active;
    }

    @Override
    protected void initData() {
        super.initData();

        galleryView.setup(getLoaderManager(),
                new GalleryView.ISelectedImageChangedListener() {
            @Override
            public void onSelectedCountChanged(int count) {

            }
        });
    }
}