package com.durbinlabs.rxjavademo.mvp.model;

import com.durbinlabs.rxjavademo.data.db.model.Client;
import com.durbinlabs.rxjavademo.data.network.APIClient;
import com.durbinlabs.rxjavademo.data.service.APIService;
import com.durbinlabs.rxjavademo.mvp.MainActivityContractor;
import com.durbinlabs.rxjavademo.mvp.interfaces.ApiRequest;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hp on 1/7/2018.
 */

public class MainActivityModel implements MainActivityContractor.MainActivityModelOperation {





    @Override
    public Observable fetch() {
        APIService service = APIClient.getRetrofit().create(APIService.class);
        final Call<List<Client>> call = service.getAll();

        return Observable.create(e -> call.enqueue(new Callback<List<Client>>() {
            @Override
            public void onResponse(Call<List<Client>> call, Response<List<Client>> response) {
                e.onNext(response.body());
            }

            @Override
            public void onFailure(Call<List<Client>> call, Throwable t) {

            }
        }));
    }
}
