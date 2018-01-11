package com.durbinlabs.rxjavademo.data.service;

import com.durbinlabs.rxjavademo.data.db.model.Client;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by Shaon on 1/1/2018.
 */

public interface APIService {
    @GET("/rxjava/clients.php")
    Observable<List<Client>> getAll();
}
