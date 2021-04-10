package com.savypan.italker.push.fragment.assist;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.savypan.italker.common.app.CommonApplication;
import com.savypan.italker.push.R;
import com.savypan.italker.push.fragment.media.GalleryFragment;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PermFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PermFragment extends BottomSheetDialogFragment implements EasyPermissions.PermissionCallbacks {

    private static final int RC = 0x0100; //权限回调的标识

    public PermFragment() {
        // Required empty public constructor
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new GalleryFragment.TransStatusBottomSheetDialog(getContext());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_perm, container, false);
        refreshStatus(root);

        root.findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPerms();
            }
        });

        return root;
    }


    @Override
    public void onResume() {
        super.onResume();
        refreshStatus(getView());
    }

    private void refreshStatus(View root) {
        Context context = getContext();
        root.findViewById(R.id.image_perm_network).setVisibility(isNetworkEnabled(context) ? View.VISIBLE:View.GONE);
        root.findViewById(R.id.image_perm_read).setVisibility(isReadEnabled(context) ? View.VISIBLE:View.GONE);
        root.findViewById(R.id.image_perm_write).setVisibility(isWriteEnabled(context) ? View.VISIBLE:View.GONE);
        root.findViewById(R.id.image_perm_record).setVisibility(isRecordEnabled(context) ? View.VISIBLE:View.GONE);
    }

    private static boolean isNetworkEnabled(Context context) {
        String[] perms = new String[] {
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_WIFI_STATE
        };

        return EasyPermissions.hasPermissions(context, perms);
    }

    private static boolean isReadEnabled(Context context) {
        String[] perms = new String[] {
                Manifest.permission.READ_EXTERNAL_STORAGE
        };

        return EasyPermissions.hasPermissions(context, perms);
    }

    private static boolean isWriteEnabled(Context context) {
        String[] perms = new String[] {
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        return EasyPermissions.hasPermissions(context, perms);
    }

    private static boolean isRecordEnabled(Context context) {
        String[] perms = new String[] {
                Manifest.permission.RECORD_AUDIO
        };

        return EasyPermissions.hasPermissions(context, perms);
    }


    private static void show(FragmentManager manager) {
        new PermFragment().show(manager, PermFragment.class.getName());
    }

    /***
     * check if all permissions been enabled
     * @param context
     * @param manager
     * @return
     */
    public static boolean hasAllPerm(Context context, FragmentManager manager) {
        boolean hasAll = isNetworkEnabled(context)
                && isReadEnabled(context)
                && isWriteEnabled(context)
                && isRecordEnabled(context);
        if (hasAll == false) {
            show(manager);
        }

        return hasAll;
    }

    @AfterPermissionGranted(RC)
    private void requestPerms() {
        String[] perms = new String[] {
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO
        };

        if (EasyPermissions.hasPermissions(getContext(), perms)) {
            CommonApplication.showToast(R.string.label_permission_ok);
            refreshStatus(getView());
        } else {

            EasyPermissions.requestPermissions(this,
                    getString(R.string.title_assist_permissions),
                    RC,
                    perms);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        //refreshStatus(getView());
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this)
                    .build()
                    .show();
        }
    }

    /***
     * 权限申请的时候调用的回调方法，在这个方法中把对应的权限申请状态交给EasyPerm框架去处理
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}