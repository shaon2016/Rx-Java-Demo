package com.durbinlabs.rxjavademo.mvp.model;

import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;

import com.durbinlabs.rxjavademo.data.db.AppDatabase;
import com.durbinlabs.rxjavademo.data.db.model.Client;
import com.durbinlabs.rxjavademo.data.network.APIClient;
import com.durbinlabs.rxjavademo.data.service.APIService;
import com.durbinlabs.rxjavademo.mvp.MainActivityContractor;
import com.durbinlabs.rxjavademo.mvp.interfaces.ApiRequest;
import com.durbinlabs.rxjavademo.mvp.presenter.MainActivityPresenter;
import com.durbinlabs.rxjavademo.mvp.interfaces.SimpleCallback;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Shaon on 1/7/2018.
 */


public class MainActivityModel implements MainActivityContractor.MainActivityModelOperation {
    private SimpleCallback callback;
    private MainActivityPresenter presenter;
    private List<Client> clients, modifiedClients;
    private AppDatabase db;
    private static final String TAG = MainActivityModel.class.getSimpleName();
    private Context context;


    public MainActivityModel(MainActivityPresenter presenter,
                             Context context, SimpleCallback callback) {
        this.callback = callback;
        this.presenter = presenter;
        clients = new ArrayList<>();
        modifiedClients = new ArrayList<>();
        db = AppDatabase.getInstance(context.getApplicationContext());
        this.context = context;
    }

    @Override
    public void fetch() {
        APIService service = APIClient.getRetrofit().create(APIService.class);
        Observable<List<Client>> call = service.getAll();

        call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(presenter.getObservableFetch());
    }

    @Override
    public Observable getObservableForFilterData(List<Client> clients) {
        return Observable.create(e -> {
            if (!e.isDisposed()) {
                e.onNext(clients);
                e.onComplete();
            }
        });
    }

    @Override
    public void filter(List<Client> clients) {
        getObservableForFilterData(clients)
                .flatMap(new Function<List<Client>, ObservableSource<Client>>() {
                    @Override
                    public ObservableSource<Client> apply(List<Client> clients) throws
                            Exception {
                        return Observable.fromIterable(clients);
                    }
                })
                .filter(new Predicate<Client>() {
                    @Override
                    public boolean test(Client client) throws Exception {

                        return (client.getAge() < 25);
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(getObserverForFilterData());
    }

    @Override
    public void saveClients(List<Client> clients) {
        new Thread(() -> {
            for (Client client : clients)
                db.clientDao().insert(client);
        }).start();
    }

    @Override
    public void getClientListFromDb(ClientLoadCallBack callBack) {
        new Thread(() -> {
            clients = db.clientDao().getAll();

            Runnable runnable = () -> {
                if (!clients.isEmpty())
                    callBack.onClientsLoaded(clients);
                else callBack.onDataNotAvailable();
                Log.d(TAG, "DB clients Size in model: " + clients.size());
            };

            android.os.Handler handler = new android.os.Handler(Looper.getMainLooper());
            handler.post(runnable);
        }).start();
    }

    private Observer<Client> getObserverForFilterData() {
        return new Observer<Client>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Client client) {
                modifiedClients.add(client);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                callback.justMe(modifiedClients);
            }
        };
    }


}
