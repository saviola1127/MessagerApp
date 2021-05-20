package com.savypan.italker.common.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.bumptech.glide.RequestManager;
import com.savypan.italker.common.R;
import com.savypan.italker.factory.model.IAuthor;

import de.hdodenhof.circleimageview.CircleImageView;

/***
 * 头像控件
 */
public class PortraitView extends CircleImageView {
    public PortraitView(Context context) {
        super(context);
    }

    public PortraitView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PortraitView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setup(RequestManager manager, IAuthor author) {
        if (author == null) {
            return;
        }
        setup(manager, author.getPortrait());
    }

    public void setup(RequestManager manager, String url) {
        setup(manager, R.drawable.default_portrait, url);
    }

    public void setup(RequestManager manager, int resourceId, String url) {
        if (url == null) {
            url = "";
        }

        manager.load(url)
                .placeholder(resourceId)
                .centerCrop()
                .dontAnimate() //这个布局不能使用动画，会导致延迟
                .into(this);
    }
}
