package com.durbinlabs.rxjavademo.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.durbinlabs.rxjavademo.db.dao.ClientDao;
import com.durbinlabs.rxjavademo.db.model.Client;

/**
 * Created by Shaon on 1/1/2018.
 */

@Database(entities = {Client.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static final String DB_NAME = "app_database";
    private static volatile AppDatabase instance;

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null)
            instance = create(context);
        return instance;
    }

    private static AppDatabase create(Context context) {
        return Room.databaseBuilder(context, AppDatabase.class, DB_NAME).build();
    }

    public abstract ClientDao clientDao();
}
