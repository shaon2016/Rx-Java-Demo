package com.durbinlabs.rxjavademo.data;

import com.durbinlabs.rxjavademo.mvp.interfaces.ApiRequest;

/**
 * Created by hp on 1/7/2018.
 */

public class ClientRepository implements ApiRequest {

    @Override
    public void onRequestComplete(Object o) {

    }

    @Override
    public void onRequestError(String errorMsg) {

    }
}
