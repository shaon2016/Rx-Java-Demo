package com.durbinlabs.rxjavademo.mvp;

import com.durbinlabs.rxjavademo.mvp.interfaces.BasePresenter;
import com.durbinlabs.rxjavademo.mvp.interfaces.BaseView;

import java.util.List;

/**
 * Created by hp on 1/6/2018.
 */

public interface MainActivityContractor {

    interface MainActivityPresenter extends BasePresenter {
        // TODO need to update

    }

    interface View extends BaseView {
        // TODO
    }

    interface MainActivityModelOperation {
        //TODO edited
        List<String> getData();

        boolean loadData();
    }
}
