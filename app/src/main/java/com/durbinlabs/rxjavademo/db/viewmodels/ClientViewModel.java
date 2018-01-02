package com.durbinlabs.rxjavademo.db.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

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
        db = AppDatabase.getInstance(application.getApplicationContext());
        clients = db.clientDao().getAll();
    }

    public List<Client> getClient() {
        return clients;
    }

}
