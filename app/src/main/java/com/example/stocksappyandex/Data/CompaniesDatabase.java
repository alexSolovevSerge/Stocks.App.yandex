package com.example.stocksappyandex.Data;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


@Database(entities = {Company.class},version = 1,exportSchema = false)
public abstract class CompaniesDatabase extends RoomDatabase {
    private static CompaniesDatabase database;
    private static final String DB_NAME = "companies.db";
    private static final Object lock = new Object();

    public static CompaniesDatabase getInstance(Context context){
        synchronized (lock) {
            if (database == null) {
                database = Room.databaseBuilder(context, CompaniesDatabase.class, DB_NAME).build();
            }
        }
        return database;

    }
    public abstract CompaniesDao companiesDao();

}