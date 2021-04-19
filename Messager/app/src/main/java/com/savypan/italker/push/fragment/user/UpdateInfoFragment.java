package com.savypan.italker.push.fragment.user;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.savypan.italker.common.app.CommonApplication;
import com.savypan.italker.common.app.CommonFragment;
import com.savypan.italker.common.widget.PortraitView;
import com.savypan.italker.factory.Factory;
import com.savypan.italker.factory.network.UploadHelper;
import com.savypan.italker.push.R;
import com.savypan.italker.push.fragment.media.GalleryFragment;
import com.yalantis.ucrop.UCrop;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

public class UpdateInfoFragment extends CommonFragment {

    @BindView(R.id.image_pv)
    PortraitView portraitView;

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_updateinfo;
    }

    @OnClick(R.id.image_pv)
    void onPortraitViewClicked() {
        new GalleryFragment().setListener(new GalleryFragment.OnSelectedListener() {
            @Override
            public void onSelectedImage(String path) {
                Log.e("SAVY", "selected image path =>" + path);
                UCrop.Options options = new UCrop.Options();
                options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
                options.setCompressionQuality(96);

                File desPath = CommonApplication.getPortraitTmpFile();
                UCrop.of(Uri.fromFile(new File(path)), Uri.fromFile(desPath))
                        .withAspectRatio(1, 1) //1:1比例
                        .withMaxResultSize(520, 520) //返回最大尺寸
                        .withOptions(options)
                        .start(getActivity());
            }
        }).show(getChildFragmentManager(), GalleryFragment.class.getName());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            final Uri resultUri = UCrop.getOutput(data);
            if (resultUri != null) {
                loadPortrait(resultUri);
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
        }
    }

    private void loadPortrait(Uri uri) {

        Glide.with(this)
                .load(uri)
                .asBitmap()
                .centerCrop()
                .into(portraitView);

        //拿到本地文件的地址
        final String localPath = uri.getPath();
        Log.e("SAVY", "local Path " + localPath);

        Factory.runOnAsync(new Runnable() {
            @Override
            public void run() {
                String url = UploadHelper.uploadPortrait(localPath);
                Log.e("SAVY", "OSS Url:" + url);
            }
        });
    }
}
