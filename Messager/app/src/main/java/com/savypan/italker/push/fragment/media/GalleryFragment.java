package com.savypan.italker.push.fragment.media;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.savypan.italker.common.app.CommonFragment;
import com.savypan.italker.common.tools.UiTool;
import com.savypan.italker.common.widget.GalleryView;
import com.savypan.italker.push.R;

import net.qiujuer.genius.ui.Ui;

/**
 * image selection fragment
 */

public class GalleryFragment extends BottomSheetDialogFragment
implements GalleryView.ISelectedImageChangedListener{

    private GalleryView galleryView;
    private OnSelectedListener listener;


    public GalleryFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        galleryView = root.findViewById(R.id.gallery_view);
        //Log.e("SAVY", "current Activity for gallery is " + getActivity().getLocalClassName());
        return root;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new TransStatusBottomSheetDialog(getContext());
    }

    @Override
    public void onStart() {
        super.onStart();
        galleryView.setup(getLoaderManager(), this);
    }

    @Override
    public void onSelectedCountChanged(int count) {
        if (count > 0) {
            dismiss();
            if (listener != null) {
                String[] paths = galleryView.getSelectedPath();
                listener.onSelectedImage(paths[0]);
                listener = null;
            }
        }
    }


    public GalleryFragment setListener(OnSelectedListener listener) {
        this.listener = listener;
        return this;
    }


    public interface OnSelectedListener {
        void onSelectedImage(String path);
    }


    public static class TransStatusBottomSheetDialog extends BottomSheetDialog {

        public TransStatusBottomSheetDialog(@NonNull Context context) {
            super(context);
        }

        public TransStatusBottomSheetDialog(@NonNull Context context, int theme) {
            super(context, theme);
        }

        protected TransStatusBottomSheetDialog(@NonNull Context context, boolean cancelable, OnCancelListener cancelListener) {
            super(context, cancelable, cancelListener);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            final Window window = getWindow();
            if (window == null) {
                return;
            }

            int screenHeight = UiTool.getScreenHeight(getOwnerActivity());
            int statusHeight = UiTool.getStatusBarHeight(getOwnerActivity());

            int dialogHeight = screenHeight - statusHeight;
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                    dialogHeight == 0? ViewGroup.LayoutParams.MATCH_PARENT : dialogHeight);

        }
    }
}