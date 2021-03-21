package com.savypan.italker.push;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.savypan.italker.common.Common;
import com.savypan.italker.common.app.CommonActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends CommonActivity {

    @BindView(R.id.txt_test)
    TextView mTextView;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        mTextView.setText("Test starts and it is expected!");
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        return super.initArgs(bundle);
    }
}