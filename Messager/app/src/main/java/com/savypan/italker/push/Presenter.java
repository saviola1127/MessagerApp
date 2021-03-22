package com.savypan.italker.push;

import android.text.TextUtils;

public class Presenter implements IPresenter {

    private IView mView;
    public Presenter(IView view){
        mView = view;
    }

    @Override
    public void search() {
        //start UI loading...



        String inputString = mView.getInputString();
        if (TextUtils.isEmpty(inputString)) {
            //as null, return immediately
            return;
        }

        IUserService service = new UserService();

        String ret = "Result : " + inputString + service.search(inputString.hashCode());

        //handling page stuff... stop loading?
        mView.setResultString(ret);
    }
}
