package com.durbinlabs.rxjavademo.mvp;

import android.content.Context;

import com.durbinlabs.rxjavademo.data.db.model.Client;
import com.durbinlabs.rxjavademo.mvp.interfaces.BasePresenter;
import com.durbinlabs.rxjavademo.mvp.interfaces.BaseView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by hp on 1/6/2018.
 */

public interface MainActivityContractor {

    interface MainActivityPresenter extends BasePresenter {
        // TODO

    }

    interface View extends BaseView {
        //TODO change the methods name
        void showFilteredData(HashMap data);
        void showAll(HashMap data);
        void showFromDb(List<Client> clients);
        Context getAppContext();
    }

    /*
    Inspired from google to do app code (TASK model)
     */
    interface MainActivityModelOperation {
        //TODO edited ()
        List<String> getData();

        boolean isDataLoaded();
    }
}
