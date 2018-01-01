package com.durbinlabs.rxjavademo.service;

import com.durbinlabs.rxjavademo.db.model.Client;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by hp on 1/1/2018.
 */

public interface APIService {
    @GET("/rxjava/clients.php")
    Call<List<Client>> getAll();
}
