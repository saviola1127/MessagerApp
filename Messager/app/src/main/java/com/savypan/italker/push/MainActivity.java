package com.savypan.italker.push;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.savypan.italker.common.app.CommonActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends CommonActivity
    implements IView {

    @BindView(R.id.txt_result)
    TextView mTextView;

    @BindView(R.id.btn_submit)
    Button mButton;

    @BindView(R.id.edit_query)
    EditText mEditText;

    private IPresenter presenter;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }


    @Override
    protected void initData() {
        super.initData();

        presenter = new Presenter(this);
    }

    @OnClick(R.id.btn_submit)
    void onSubmit(View view) {
        presenter.search();
    }

    @Override
    public String getInputString() {
        return mEditText.getText().toString();
    }

    @Override
    public void setResultString(String statement) {
        mTextView.setText(statement);
    }
}