package com.savypan.italker.factory.presenter;

public class BasePresenter<T extends BaseContract.IView> implements BaseContract.IPresenter {

    protected T myView;

    public BasePresenter(T view) {
        setView(view);
    }

    protected void setView(T view) {
        myView = view;
    }


    protected final T getView() {
        return myView;
    }


    @Override
    public void start() {
        T view = myView;
        if (view != null) {
            view.showLoading();
        }
    }

    @Override
    public void destroy() {
        T view = myView;
        myView = null;
        if (view != null) {
            view.setPresenter(null);
        }
    }
}
