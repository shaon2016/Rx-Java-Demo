package com.durbinlabs.rxjavademo.mvp.interfaces;

/**
 * Created by hp on 1/6/2018.
 */

public interface BasePresenter {
    void start();

    void attach();

    void detach();

    void destroy();

    void resume();
}
