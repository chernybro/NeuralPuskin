package com.uralsiberianworks.neuralpushkin;

import android.app.Application;

import androidx.room.Room;

import com.uralsiberianworks.neuralpushkin.db.NeuralDatabase;


public class NeuralApp extends Application {
    
    private static NeuralDatabase db;

    public NeuralDatabase getDb() {
        return db;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        configureDatabase();
    }

    private void configureDatabase() {
        db =  Room.databaseBuilder(getApplicationContext(),
                NeuralDatabase.class, "database")
                .allowMainThreadQueries()
                .build();


    }
}
