package com.durbinlabs.rxjavademo.mvp;

import android.content.Context;

import com.durbinlabs.rxjavademo.data.db.model.Client;
import com.durbinlabs.rxjavademo.mvp.interfaces.ApiRequest;
import com.durbinlabs.rxjavademo.mvp.interfaces.BasePresenter;
import com.durbinlabs.rxjavademo.mvp.interfaces.BaseView;

import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;

/**
 * Created by hp on 1/6/2018.
 */

public interface MainActivityContractor {

    interface MainActivityPresenter extends BasePresenter {
        // TODO

    }

    interface View extends BaseView {
        //TODO change the methods name
        void showFilteredData(List<Client> clients);

        void showAll(List<Client> clients);

        void showFromDb(List<Client> clients);
    }

    /*
    Inspired from google to do app code (TASK model)
     */
    interface MainActivityModelOperation {
        void fetch();

        Observable getObservableForFilterData(List<Client> clients);

        void filter(List<Client> clients);

        void saveClients(List<Client> clients);

        void getClientListFromDb(ClientLoadCallBack callBack);

        interface ClientLoadCallBack {
            void onClientsLoaded(List<Client> clients);

            void onDataNotAvailable();
        }
    }

    interface requiredForOperation {
        Context getActivityContext();

        Context getAppContext();
    }
}
