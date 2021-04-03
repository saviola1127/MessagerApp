package com.savypan.italker.common.app;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import java.util.List;

import butterknife.ButterKnife;

public abstract class CommonActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initWindows();

        if (initArgs(getIntent().getExtras())) {
            setContentView(getContentLayoutId());
            initWidget();
            initData();
        } else {
            finish();
        }

    }

    /***
     * init window related content
     */
    protected void initWindows() {
    }

    /***
     * get current layout resource ID
     * @return
     */
    protected abstract int getContentLayoutId();

    /***
     * init View/widget
     */
    protected void initWidget() {
        ButterKnife.bind(this);
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
    protected boolean initArgs(Bundle bundle) {
        return true;
    }


    @Override
    public void onBackPressed() {

        /***
         * if multi fragments in Activity inside, activity need to handle those fragment in the stack first
         */
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        if (fragmentList != null && fragmentList.size() > 0) {
            for (Fragment fragment: fragmentList) {
                if (fragment instanceof CommonFragment && fragment != null) {
                    ((CommonFragment) fragment).onBackPressed();
                    return;
                }
            }
        }
        super.onBackPressed();
        finish();
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE" };

    public static void verifyStoragePermissions(Activity activity) {
        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
                System.out.println("here1 ");
            }else {
                System.out.println("here2 ");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
