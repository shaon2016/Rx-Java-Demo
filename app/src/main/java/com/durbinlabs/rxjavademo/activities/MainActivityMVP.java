package com.durbinlabs.rxjavademo.activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.durbinlabs.rxjavademo.R;
import com.durbinlabs.rxjavademo.adapter.RecyclerViewAdapterForFilterData;
import com.durbinlabs.rxjavademo.adapter.RecyclerViewAdapterForWithoutFilterData;
import com.durbinlabs.rxjavademo.data.db.model.Client;
import com.durbinlabs.rxjavademo.mvp.MainActivityContractor;
import com.durbinlabs.rxjavademo.mvp.presenter.MainActivityPresenter;

import java.util.ArrayList;
import java.util.List;

public class MainActivityMVP extends AppCompatActivity implements MainActivityContractor.View,
        MainActivityContractor.requiredForOperation {

    private MainActivityPresenter presenter;

    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView rv, rv2;
    private RecyclerViewAdapterForFilterData adapter;
    private RecyclerViewAdapterForWithoutFilterData adapter2;
    private List<Client> clients, modifiedClients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        configLayout();
        initialization();
        presenter.loadDataFromServer();
    }

    private void initialization() {
        presenter = new MainActivityPresenter(this, this);
        clients = new ArrayList<>();
        modifiedClients = new ArrayList<>();
    }

    private void configLayout() {
        rv = findViewById(R.id.rv);
        rv2 = findViewById(R.id.rv2);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv2.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter = new RecyclerViewAdapterForFilterData(new ArrayList<Client>(), this));
        rv2.setAdapter(adapter2 = new RecyclerViewAdapterForWithoutFilterData(new ArrayList<Client>
                (), this));
    }


    @Override
    public void showLoading() {
        Toast.makeText(this, "Loading Started", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void stopLoading() {
        Toast.makeText(this, "Loading Stopped", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void showFilteredData(List<Client> clients) {
        adapter.addAll(clients);
    }

    @Override
    public void showAll(List<Client> clients) {
        adapter2.addAll(clients);
    }

    @Override
    public void showFromDb(List<Client> clients) {
        adapter.addAll(clients);
    }


    @Override
    public Context getActivityContext() {
        return this;
    }

    @Override
    public Context getAppContext() {
        return getApplicationContext();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_from_db) {
            presenter.loadClientsFromDbData();
            return true;
        }

        return false;
    }
}
