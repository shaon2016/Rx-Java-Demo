package com.durbinlabs.rxjavademo.db.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.durbinlabs.rxjavademo.db.AppDatabase;
import com.durbinlabs.rxjavademo.db.model.Client;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hp on 1/2/2018.
 */

public class ClientViewModel extends AndroidViewModel {
    private AppDatabase db;
    private List<Client> clients;

    public ClientViewModel(@NonNull Application application) {
        super(application);
        db = AppDatabase.getInstance(this.getApplication());

        loadData();
    }

    private void loadData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                clients = db.clientDao().getAll();
            }
        }).start();
    }

    public List<Client> getClient() {
        return clients;
    }


}
