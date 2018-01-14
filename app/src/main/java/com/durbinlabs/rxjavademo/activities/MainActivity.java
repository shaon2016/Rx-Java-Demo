package com.durbinlabs.rxjavademo.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.durbinlabs.rxjavademo.R;
import com.durbinlabs.rxjavademo.adapter.RecyclerViewAdapterForFilterData;
import com.durbinlabs.rxjavademo.adapter.RecyclerViewAdapterForWithoutFilterData;
import com.durbinlabs.rxjavademo.data.db.AppDatabase;
import com.durbinlabs.rxjavademo.data.db.model.Client;
import com.durbinlabs.rxjavademo.data.db.viewmodels.ClientViewModel;
import com.durbinlabs.rxjavademo.data.network.APIClient;
import com.durbinlabs.rxjavademo.data.service.APIService;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

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

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView rv, rv2;
    private RecyclerViewAdapterForFilterData adapter;
    private RecyclerViewAdapterForWithoutFilterData adapter2;
    private List<Client> clientList, modifiedClients;
    private AppDatabase appDatabase;
    private ClientViewModel clientViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        configLayout();
        initialization();
        fetchUserData();

    }

    private void initialization() {
        clientList = new ArrayList<>();
        modifiedClients = new ArrayList<>();
        appDatabase = AppDatabase.getInstance(this);

        clientViewModel = ViewModelProviders.of(MainActivity.this)
                .get(ClientViewModel.class);
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

    private void fetchUserData() {
        try {

            APIService service = APIClient.getRetrofit().create(APIService.class);
            Observable<List<Client>> call = service.getAll();

            call.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(getObservableFetch());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private Observer getObservableFetch() {
        return new Observer<List<Client>>() {

            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<Client> clients) {
                Log.d("DataTag", "Size: " + clients.size());
                //clientList = clients;
                filterData(clients);
                insertIntoDb(clients);
                adapter2.addAll(clients);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_from_db) {
            loadDataFromDb();
            return true;
        }
        if (item.getItemId() == R.id.mvp) {
            startActivity(new Intent(this, MainActivityMVP.class));
            return true;
        }
        return false;
    }

    private void loadDataFromDb() {
        TextView tv = findViewById(R.id.tvFilterData);
        tv.setText(getResources().getString(R.string.without_filter_data_from_db));


        new Thread(() -> {
            List<Client> clients = appDatabase.clientDao().getAll();

            Runnable runnable = () -> {
                if (!clients.isEmpty())
                    adapter.addAll(clients);
            };

            android.os.Handler handler = new android.os.Handler(Looper.getMainLooper());
            handler.post(runnable);
        }).start();
    }


    private void insertIntoDb(List<Client> clients) {
        new Thread(() -> {
            for (Client client : clients)
                appDatabase.clientDao().insert(client);
        }).start();
    }

    // Filtering Fetched Data
    private void filterData(List<Client> clients) {
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

    private Observable getObservableForFilterData(List<Client> clients) {
        return Observable.create(e -> {
            if (!e.isDisposed()) {
                e.onNext(clients);
                e.onComplete();
            }
        });
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
                adapter.addAll(modifiedClients);
            }
        };
    }
}
