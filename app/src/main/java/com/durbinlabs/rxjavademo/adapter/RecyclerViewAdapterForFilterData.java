package com.durbinlabs.rxjavademo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.durbinlabs.rxjavademo.R;
import com.durbinlabs.rxjavademo.db.model.Client;

import java.util.List;

/**
 * Created by hp on 1/1/2018.
 */

public class RecyclerViewAdapterForFilterData extends RecyclerView.Adapter<RecyclerViewAdapterForFilterData.RecyclerViewHolder> {

    private List<Client> clients;
    private Context context;

    public RecyclerViewAdapterForFilterData(List<Client> clients, Context context) {
        this.clients = clients;
        this.context = context;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = View.inflate(context, R.layout.recyclerview_row, null);
        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        int pos = holder.getAdapterPosition();
        Client client = clients.get(pos);

        holder.tvName.setText(client.getName());
        holder.tvAge.setText(client.getAge() + "");
    }

    @Override
    public int getItemCount() {
        return clients.size();
    }

    public void addAll(List<Client> clients) {
        this.clients.addAll(clients);
        notifyDataSetChanged();
    }

    public void add(Client client) {
        clients.add(client);
    }

    protected class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName, tvAge;

        public RecyclerViewHolder(View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvClientName);
            tvAge = itemView.findViewById(R.id.tvClientAge);
        }
    }
}
