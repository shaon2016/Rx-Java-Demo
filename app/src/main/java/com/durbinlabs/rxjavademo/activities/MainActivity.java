package com.durbinlabs.rxjavademo.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.durbinlabs.rxjavademo.R;
import com.durbinlabs.rxjavademo.adapter.RecyclerViewAdapter;
import com.durbinlabs.rxjavademo.db.model.Client;
import com.durbinlabs.rxjavademo.network.APIClient;
import com.durbinlabs.rxjavademo.service.APIService;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView rv;
    private RecyclerViewAdapter adapter;
    List<Client> clients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rv = findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter = new RecyclerViewAdapter(new ArrayList<Client>(), this));
        clients = new ArrayList<>();
        fetchUserData();
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
                        adapter.addAll(clients);
                        Log.d(TAG, clients.size() + "");
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


}
