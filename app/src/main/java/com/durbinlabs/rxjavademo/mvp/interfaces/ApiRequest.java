package com.durbinlabs.rxjavademo.mvp.interfaces;

/**
 * Created by hp on 1/6/2018.
 */

public interface ApiRequest {
    void onRequestComplete(Object o);

    void onRequestError(String errorMsg);
}
