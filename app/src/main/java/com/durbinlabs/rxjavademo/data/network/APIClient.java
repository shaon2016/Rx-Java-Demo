package com.durbinlabs.rxjavademo.data.network;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import io.reactivex.plugins.RxJavaPlugins;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
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

    /*
    If you wish to default network calls to be asynchronous, you need to use createWithScheduler().

RxJavaCallAdapterFactory rxAdapter = RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io());
     */

    private static Retrofit create() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }
}
