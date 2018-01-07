package com.durbinlabs.rxjavademo.mvp.interfaces;

/**
 * Created by hp on 1/6/2018.
 */

public interface ApiRequest {
    public void onRequestComplete(Object o);

    public void onRequestError(String errorMsg);
}
