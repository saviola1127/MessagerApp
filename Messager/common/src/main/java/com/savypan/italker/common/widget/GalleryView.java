package com.savypan.italker.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.savypan.italker.common.R;
import com.savypan.italker.common.widget.recycler.RecyclerAdapter;

import net.qiujuer.genius.ui.widget.CheckBox;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * TODO: document your custom view class.
 */
public class GalleryView extends RecyclerView {

    private static final int LOADER_ID = 0x100;
    private static final int MAX_SELECTED_AMOUNT = 3;
    private static final int MIN_IMAGE_SIZE = 10 * 1024;

    private LoaderCallback callback = new LoaderCallback();
    private Adapter myAdapter = new Adapter();
    private List<Image> selectedImages = new LinkedList<>();
    private ISelectedImageChangedListener listener;

    public GalleryView(Context context) {
        super(context);
        init();
    }

    public GalleryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GalleryView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setLayoutManager(new GridLayoutManager(getContext(), 4));
        setAdapter(myAdapter);
        myAdapter.setListener(new RecyclerAdapter.AdapterListenerImpl<Image>() {
            @Override
            public void onItemClick(RecyclerAdapter.ViewHolder viewHolder, Image data) {
                //onclick ops when cell clicked, update UI and status
                //check max selected amount
                if (onItemSelectClick(data)) {
                    viewHolder.updateData(data);
                }
            }
        });
        Log.e("SAVY", "init and set LayoutManager");
    }

    /***
     * init function loader management
     * @param loaderManager
     */
    public int setup(LoaderManager loaderManager, ISelectedImageChangedListener listener) {
        Log.e("SAVY", "Fragment setup now");
        this.listener = listener;
        loaderManager.initLoader(LOADER_ID, null, callback);
        return LOADER_ID;
    }


    /***
     * get all paths for selected images
     * @return
     */
    public String[] getSelectedPath() {
        String[] paths = new String[selectedImages.size()];
        int index = 0;

        for (Image image : selectedImages) {
            paths[index++] = image.path;
        }

        return paths;
    }


    public void resetSelectedImages() {
        for (Image image : selectedImages) {
            //first to reset status
            image.isSelected = false;
        }

        selectedImages.clear();

        //notify update for refresh
        myAdapter.notifyDataSetChanged();
    }


    /***
     * detailed ops for image click
     * true means data changed and UI to be refreshed
     * false means reversed
     */
    private boolean onItemSelectClick(Image image) {
        boolean notifyRefresh = false;
        if (selectedImages.contains(image)) {
            selectedImages.remove(image);
            image.isSelected = false;
            notifyRefresh = true;
        } else {
            if (selectedImages.size() >= MAX_SELECTED_AMOUNT) {

                String str = getResources().getString(R.string.label_gallery_select_max_size);
                str = String.format(str, MAX_SELECTED_AMOUNT);
                Toast.makeText(getContext(), str, Toast.LENGTH_SHORT).show();
                notifyRefresh = false;
            } else {
                selectedImages.add(image);
                image.isSelected = true;
                notifyRefresh = true;
            }
        }

        if (notifyRefresh) {
            notifySelectedImageChanged();
        }
        return true;
    }


    /***
     * notify external widget about data set changed
     */
    private void notifySelectedImageChanged() {
        if (listener != null) {
            listener.onSelectedCountChanged(selectedImages.size());
        }
    }


    private static class Image {
        int id;
        String path; //image url
        long date;
        boolean isSelected;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Image image = (Image) o;
            return path != null? path.equals(image.path) : image.path == null;
        }

        @Override
        public int hashCode() {
            return path != null ? path.hashCode() : 0;
        }
    }
    
    private class Adapter extends RecyclerAdapter<Image> {

        @Override
        protected ViewHolder<Image> onCreateViewHolder(View view, int viewType) {
            Log.e("SAVY", "create View Holder for each image");
            return new GalleryView.ViewHolder(view);
        }

        @Override
        protected int getItemViewType(int position, Image data) {
            return R.layout.cell_gallery;
        }
    }

    //cell对应的holder
    private class ViewHolder extends RecyclerAdapter.ViewHolder<Image> {

        private ImageView myPic;
        private View myShader;
        private CheckBox mySelecting;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            myPic = itemView.findViewById(R.id.image);
            myShader = itemView.findViewById(R.id.view_shader);
            mySelecting = itemView.findViewById(R.id.cb_select);
        }

        @Override
        protected void onBind(Image data) {
            Log.e("SAVY", "bind data for each image");
            //image loading for rendering
            Glide.with(getContext())
                    .load(data.path)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .centerCrop()
                    .placeholder(R.color.grey_200) //default color
                    .into(myPic);

            myShader.setVisibility(data.isSelected?VISIBLE:INVISIBLE);
            mySelecting.setChecked(data.isSelected);
            mySelecting.setVisibility(VISIBLE);
        }
    }


    /***
     * notify adapter about data set update
     * @param images
     */
    private void updateUISource(List<Image> images) {
        myAdapter.replace(images);
    }


    /***
     * actual data loader
     */
    private class LoaderCallback implements LoaderManager.LoaderCallbacks<Cursor> {

        private final String[] IMAGES_PROJECTION = new String[] {
                MediaStore.Images.Media._ID,  //image ID
                MediaStore.Images.Media.DATA, //image url
                MediaStore.Images.Media.DATE_ADDED //image datetime created
        };

        @NonNull
        @Override
        public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle bundle) {
            Log.e("SAVY", "create Loader now!");
            if (id == LOADER_ID) {
                return new CursorLoader(
                        getContext(),
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        IMAGES_PROJECTION,
                        null,
                        null,
                        IMAGES_PROJECTION[2] + " DESC" //降序查询
                );
            }
            return null;
        }

        @Override
        public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
            List<Image> images = new ArrayList<>();

            if (cursor != null) {
                int count = cursor.getCount();
                if (count > 0 ) {
                    cursor.moveToFirst();

                    int indexId = cursor.getColumnIndexOrThrow(IMAGES_PROJECTION[0]);
                    int indexPath = cursor.getColumnIndexOrThrow(IMAGES_PROJECTION[1]);
                    int indexDate = cursor.getColumnIndexOrThrow(IMAGES_PROJECTION[2]);

                    do {
                        //read the data until next is null
                        int id = cursor.getInt(indexId);
                        String path = cursor.getString(indexPath);
                        long date = cursor.getLong(indexDate);

                        File file = new File(path);
                        if (!file.exists() || file.length() < MIN_IMAGE_SIZE) {
                            continue;
                        } else {
                            Image image = new Image();
                            image.id = id;
                            image.path = path;
                            image.date = date;

                            images.add(image);
                        }

                    } while (cursor.moveToNext());
                }
            }

            Log.e("SAVY", "finish Loading Images for dataSet");
            updateUISource(images);
        }

        @Override
        public void onLoaderReset(@NonNull Loader<Cursor> loader) {
            //when loader destroyed or reset
            updateUISource(null);
        }
    }

    public interface ISelectedImageChangedListener {
        void onSelectedCountChanged(int count);
    }
}