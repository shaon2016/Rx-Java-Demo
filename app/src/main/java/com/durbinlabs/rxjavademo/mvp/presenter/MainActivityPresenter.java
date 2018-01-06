package com.durbinlabs.rxjavademo.mvp.presenter;

import com.durbinlabs.rxjavademo.mvp.view.MainActivityView;

/**
 * Created by Shaon on 1/6/2018.
 */

public class MainActivityPresenter {
    private MainActivityView view;

    MainActivityPresenter(MainActivityView view) {
        this.view = view;
    }
}
