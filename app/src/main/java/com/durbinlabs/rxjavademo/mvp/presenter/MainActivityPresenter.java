package com.durbinlabs.rxjavademo.mvp.presenter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.durbinlabs.rxjavademo.data.db.model.Client;
import com.durbinlabs.rxjavademo.mvp.MainActivityContractor;
import com.durbinlabs.rxjavademo.mvp.interfaces.SimpleCallback;
import com.durbinlabs.rxjavademo.mvp.model.MainActivityModel;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Shaon on 1/6/2018.
 */

public class MainActivityPresenter implements MainActivityContractor.MainActivityPresenter,
        SimpleCallback {
    private static final String TAG = MainActivityPresenter.class.getSimpleName();
    private WeakReference<MainActivityContractor.View> view;
    private MainActivityModel model;
    private List<Client> clients, modifiedClients;

    public MainActivityPresenter(MainActivityContractor.View view, Context context) {
        this.view = new WeakReference<MainActivityContractor.View>(view);
        // To communicate with model
        model = new MainActivityModel(this, context, this);
        clients = new ArrayList<>();
        modifiedClients = new ArrayList<>();

    }

    private MainActivityContractor.View getView() throws NullPointerException {
        if (view != null) return view.get();
        else throw new NullPointerException("View is not available");
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
        if (getView() != null)
            view = null;
    }

    @Override
    public void resume() {

    }


    public void loadDataFromServer() {

        getView().showLoading();
        model.fetch();
//        model.fetch().subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(getObservableFetch());
    }

    public Observer getObservableFetch() {
        return new Observer<List<Client>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<Client> clients) {
                getView().showAll(clients);
                model.filter(clients);
                model.saveClients(clients);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                getView().stopLoading();

            }
        };
    }


    @Override
    public void justMe(Object o) {
        getView().showFilteredData((List<Client>) o);
        Log.d(TAG, ((List<Client>) o).size() + "");
    }



    public void loadClientsFromDbData() {
        model.getClientListFromDb(new MainActivityContractor.MainActivityModelOperation
                .ClientLoadCallBack() {
            @Override
            public void onClientsLoaded(List<Client> clients) {
                getView().showFromDb(clients);
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }

}
