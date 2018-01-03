package com.durbinlabs.rxjavademo.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.os.AsyncTask;
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
import com.durbinlabs.rxjavademo.db.AppDatabase;
import com.durbinlabs.rxjavademo.db.model.Client;
import com.durbinlabs.rxjavademo.db.viewmodels.ClientViewModel;
import com.durbinlabs.rxjavademo.network.APIClient;
import com.durbinlabs.rxjavademo.service.APIService;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.internal.observers.DisposableLambdaObserver;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView rv, rv2;
    private RecyclerViewAdapterForFilterData adapter;
    private RecyclerViewAdapterForWithoutFilterData adapter2;
    private List<Client> clients, modifiedClients;
    private AppDatabase appDatabase;
    private ClientViewModel clientViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        configLayout();
        initialization();
        //fetchUserData();
        fetch().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObservableFetch());
    }

    private void initialization() {
        clients = new ArrayList<>();
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

    private List<Client> fetchUserData() {
        try {

            APIService service = APIClient.getRetrofit().create(APIService.class);
            Call<List<Client>> call = service.getAll();

            call.enqueue(new Callback<List<Client>>() {
                @Override
                public void onResponse(Call<List<Client>> call, Response<List<Client>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        clients = response.body();
                        //adapter2.addAll(clients);

                        filterData();

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                for (Client client : clients)
                                    appDatabase.clientDao().insert(client);
                            }
                        }).start();
                    }
                }

                @Override
                public void onFailure(Call<List<Client>> call, Throwable t) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return clients;
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
        return false;
    }

    private void loadDataFromDb() {
        TextView tv = findViewById(R.id.tvFilterData);
        tv.setText(getResources().getString(R.string.without_filter_data_from_db));
        adapter.addAll(clientViewModel.getClient());
    }

    // Fetching Data
    private Observable fetch() {
        APIService service = APIClient.getRetrofit().create(APIService.class);
        final Call<List<Client>> call = service.getAll();

        return Observable.create(e -> call.enqueue(new Callback<List<Client>>() {
            @Override
            public void onResponse(Call<List<Client>> call, Response<List<Client>> response) {
                e.onNext(response.body());
                clients = response.body();
                filterData();
                insertIntoDb();
            }

            @Override
            public void onFailure(Call<List<Client>> call, Throwable t) {

            }
        }));
    }

    private Observer getObservableFetch() {
        return new Observer<List<Client>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<Client> clients) {
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

    private void insertIntoDb() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (Client client : clients)
                    appDatabase.clientDao().insert(client);
            }
        }).start();
    }

    // Filtering Fetched Data
    private void filterData() {
        getObservableForFilterData()
                .flatMap(new Function<List<Client>, ObservableSource<Client>>
                        () {
                    @Override
                    public ObservableSource<Client> apply(List<Client> clients) throws
                            Exception {
                        return Observable.fromIterable(clients);
                    }
                })
                .filter(new Predicate<Client>() {
                    @Override
                    public boolean test(Client client) throws Exception {
                        // filtering user who follows me.

                        return (client.getAge() < 25);
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(getObserverForFilterData());
    }

    private Observable getObservableForFilterData() {
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
