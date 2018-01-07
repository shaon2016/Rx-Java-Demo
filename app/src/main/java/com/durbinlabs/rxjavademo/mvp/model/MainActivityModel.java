package com.durbinlabs.rxjavademo.mvp.model;

import com.durbinlabs.rxjavademo.data.db.model.Client;
import com.durbinlabs.rxjavademo.data.network.APIClient;
import com.durbinlabs.rxjavademo.data.service.APIService;
import com.durbinlabs.rxjavademo.mvp.MainActivityContractor;
import com.durbinlabs.rxjavademo.mvp.interfaces.ApiRequest;

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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hp on 1/7/2018.
 */

public class MainActivityModel implements MainActivityContractor.MainActivityModelOperation {
    private ApiRequest apiRequest;
    private MainActivityContractor.MainActivityPresenter presenter;
    private List<Client> clients, modifiedClients;

    public MainActivityModel(MainActivityContractor.MainActivityPresenter presenter,
                             ApiRequest apiRequest) {
        this.apiRequest = apiRequest;
        this.presenter = presenter;
        clients = new ArrayList<>();
        modifiedClients = new ArrayList<>();
    }

    @Override
    public Observable fetch() {
        APIService service = APIClient.getRetrofit().create(APIService.class);
        final Call<List<Client>> call = service.getAll();

        return Observable.create(e -> call.enqueue(new Callback<List<Client>>() {
            @Override
            public void onResponse(Call<List<Client>> call, Response<List<Client>> response) {
                e.onNext(response.body());
                e.onComplete();
            }

            @Override
            public void onFailure(Call<List<Client>> call, Throwable t) {

            }
        }));
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
                apiRequest.onRequestComplete(modifiedClients);
            }
        };
    }

    public List<Client> getFilteredData() {
        return modifiedClients;
    }


}
