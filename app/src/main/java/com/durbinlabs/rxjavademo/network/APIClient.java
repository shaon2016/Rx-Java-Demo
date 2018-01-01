package com.durbinlabs.rxjavademo.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.durbinlabs.rxjavademo.util.C.BASE_URL;

/**
 * Created by hp on 1/1/2018.
 */

public class APIClient {
    private static Retrofit retrofit = null;

    public static Retrofit getRetrofit() {
        if (retrofit == null) {
            retrofit = create();
        }
        return retrofit;
    }

    private static Retrofit create() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
