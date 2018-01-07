package com.durbinlabs.rxjavademo.mvp.presenter;

import com.durbinlabs.rxjavademo.data.db.model.Client;
import com.durbinlabs.rxjavademo.mvp.MainActivityContractor;
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

public class MainActivityPresenter implements MainActivityContractor.MainActivityPresenter {
    private WeakReference<MainActivityContractor.View> view;
    private MainActivityModel model;
    private List<Client> clients;

    public MainActivityPresenter(MainActivityContractor.View view,
                                 MainActivityContractor.MainActivityModelOperation modelOperation) {
        this.view = new WeakReference<MainActivityContractor.View>(view);
        model = new MainActivityModel();
        clients = new ArrayList<>();
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
        if (view != null)
            view = null;
    }

    @Override
    public void resume() {
        loadDataFromServer();
    }

    private void loadDataFromServer() {

        getView().showLoading();

        model.fetch().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObservableFetch());

    }

    private Observer getObservableFetch() {
        return new Observer<List<Client>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<Client> clients) {
                getView().showAll(clients);
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

}
