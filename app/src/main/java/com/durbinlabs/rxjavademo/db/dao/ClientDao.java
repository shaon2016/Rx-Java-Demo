package com.durbinlabs.rxjavademo.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.durbinlabs.rxjavademo.db.model.Client;

import java.util.List;

/**
 * Created by hp on 1/1/2018.
 */

@Dao
public interface ClientDao {
    @Query("select * from client")
    List<Client> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Client client);

    @Delete
    void delete(Client client);

    @Query("select * from client where age > :age")
    LiveData<List<Client>> getClientsGreaterThanAge(int age);

    @Query("select * from client where age < :age")
    LiveData<List<Client>> getClientsLessThanAge(int age);
}
