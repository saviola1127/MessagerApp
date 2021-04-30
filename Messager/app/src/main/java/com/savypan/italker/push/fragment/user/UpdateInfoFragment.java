package com.savypan.italker.push.fragment.user;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.savypan.italker.common.app.CommonApplication;
import com.savypan.italker.common.app.CommonFragment;
import com.savypan.italker.common.app.PresenterFragment;
import com.savypan.italker.common.widget.PortraitView;
import com.savypan.italker.factory.Factory;
import com.savypan.italker.factory.network.UploadHelper;
import com.savypan.italker.factory.presenter.user.UpdateInfoContract;
import com.savypan.italker.factory.presenter.user.UpdateInfoPresenter;
import com.savypan.italker.push.R;
import com.savypan.italker.push.activities.MainActivity;
import com.savypan.italker.push.fragment.media.GalleryFragment;
import com.yalantis.ucrop.UCrop;

import net.qiujuer.genius.ui.widget.Button;
import net.qiujuer.genius.ui.widget.EditText;
import net.qiujuer.genius.ui.widget.Loading;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

public class UpdateInfoFragment extends PresenterFragment<UpdateInfoContract.IPresenter>
implements UpdateInfoContract.IView{

    private static final String TAG = UpdateInfoFragment.class.getSimpleName();

    @BindView(R.id.im_ptv)
    PortraitView portraitView;

    @BindView(R.id.im_sex)
    ImageView im_sex;

    @BindView(R.id.et_desc)
    EditText editText;

    @BindView(R.id.loading)
    Loading myLoading;

    @BindView(R.id.btn_submit)
    Button submit;

    private String portraitPath; //头像的本地路径
    private boolean isMan = true;

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_updateinfo;
    }

    @OnClick(R.id.im_ptv)
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
            Log.e(TAG, "resultUri =>" + resultUri.toString());
            if (resultUri != null) {
                loadPortrait(resultUri);
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            CommonApplication.showToast(R.string.data_rsp_error_unknown);
            //final Throwable cropError = UCrop.getError(data);
        }
    }

    private void loadPortrait(Uri uri) {

        Glide.with(this)
                .load(uri)
                .asBitmap()
                .centerCrop()
                .into(portraitView);

        //拿到本地文件的地址
        portraitPath = uri.getPath();
        Log.i("SAVY", "local Path " + portraitPath);

//        Factory.runOnAsync(new Runnable() {
//            @Override
//            public void run() {
//                String url = UploadHelper.uploadPortrait(portraitPath);
//                Log.i("SAVY", "OSS Url:" + url);
//            }
//        });
    }


    @OnClick(R.id.btn_submit)
    void onSubmit() {

        String desc = editText.getText().toString();
        myPresenter.update(portraitPath, desc, isMan);
    }


    @OnClick(R.id.im_sex)
    void onSexClick() {
        isMan = !isMan;
        Drawable drawable = getResources().getDrawable(isMan?
                R.drawable.ic_sex_man:R.drawable.ic_sex_woman);
        im_sex.setImageDrawable(drawable);
        im_sex.getBackground().setLevel(isMan?0:1);
    }


    @Override
    protected UpdateInfoContract.IPresenter initPresenter() {
        return new UpdateInfoPresenter(this);
    }

    @Override
    public void updateSuccess() {
        MainActivity.show(getContext());
        getActivity().finish();
    }


    @Override
    public void showLoading() {
        super.showLoading();

        //正在等待中… 界面不可操作
        myLoading.start();
        //控件不可以输入
        editText.setEnabled(false);
        im_sex.setEnabled(false);

        //按钮不能重复点击
        submit.setEnabled(false);
    }


    @Override
    public void showError(int str) {
        super.showError(str);

        myLoading.stop();
        //恢复控件可以输入
        editText.setEnabled(true);
        im_sex.setEnabled(true);

        //按钮不能重复点击
        submit.setEnabled(true);
    }
}
