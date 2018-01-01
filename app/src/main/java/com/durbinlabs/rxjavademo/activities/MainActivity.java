package com.durbinlabs.rxjavademo.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.durbinlabs.rxjavademo.R;
import com.durbinlabs.rxjavademo.adapter.RecyclerViewAdapter;
import com.durbinlabs.rxjavademo.db.AppDatabase;
import com.durbinlabs.rxjavademo.db.model.Client;
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
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView rv;
    private RecyclerViewAdapter adapter;
    private List<Client> clients, modifiedClients;
    private AppDatabase appDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rv = findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter = new RecyclerViewAdapter(new ArrayList<Client>(), this));
        clients = new ArrayList<>();
        modifiedClients = new ArrayList<>();
        fetchUserData();
        appDatabase = AppDatabase.getInstance(this);
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
                        //adapter.addAll(clients);

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

    private void filterData() {
        getObservable()
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
                .subscribe(getObserver());
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
        adapter.addAll(appDatabase.clientDao().getAll());
    }

    private Observable<List<Client>> getObservable() {
        return Observable.create(new ObservableOnSubscribe<List<Client>>() {
            @Override
            public void subscribe(ObservableEmitter<List<Client>> e) throws Exception {
                if (!e.isDisposed()) {
                    e.onNext(clients);
                    e.onComplete();
                }
            }
        });
    }

    private Observer<Client> getObserver() {
        return new Observer<Client>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Client client) {
                modifiedClients.add(client);
                Log.d(TAG, client.getName() + "");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                adapter.addAll(modifiedClients);
                Log.d(TAG, modifiedClients.size() + "LOL");
            }
        };
    }
}
