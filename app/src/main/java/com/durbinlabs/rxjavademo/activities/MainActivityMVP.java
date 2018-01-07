package com.durbinlabs.rxjavademo.activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.durbinlabs.rxjavademo.R;
import com.durbinlabs.rxjavademo.data.db.model.Client;
import com.durbinlabs.rxjavademo.mvp.MainActivityContractor;

import java.util.HashMap;
import java.util.List;

public class MainActivityMVP extends AppCompatActivity implements MainActivityContractor.View,
        MainActivityContractor.requiredForOperation {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void stopLoading() {

    }

    @Override
    public void showFilteredData(HashMap data) {

    }

    @Override
    public void showAll(HashMap data) {

    }

    @Override
    public void showFromDb(List<Client> clients) {

    }


    @Override
    public Context getActivityContext() {
        return this;
    }

    @Override
    public Context getAppContext() {
        return getApplicationContext();
    }
}
