package com.durbinlabs.rxjavademo.mvp.presenter;

import com.durbinlabs.rxjavademo.mvp.MainActivityContractor;

/**
 * Created by Shaon on 1/6/2018.
 */

public class MainActivityPresenter implements MainActivityContractor.MainActivityPresenter {
    private MainActivityContractor.View view;
    private MainActivityContractor.MainActivityModelOperation modelOperation;

    public MainActivityPresenter(MainActivityContractor.View view,
                                 MainActivityContractor.MainActivityModelOperation modelOperation) {
        this.view = view;
        this.modelOperation = modelOperation;
    }

    @Override
    public void start() {

    }

    @Override
    public void attach() {

    }

    @Override
    public void detach() {

    }

    @Override
    public void destroy() {
        if (view != null)
            view = null;
    }

    @Override
    public void resume() {
        if (view != null)
            view.showLoading();
    }

}
