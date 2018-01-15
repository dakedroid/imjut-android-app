package com.example.angel.imjut;

import android.content.res.Configuration;
import android.support.multidex.MultiDexApplication;
import android.support.v7.app.AppCompatDelegate;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Angel on 11/01/2018.
 */

public class Imjut extends MultiDexApplication {


    private FirebaseDatabase database;
    private static boolean s_persistenceInitialized = false;

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }


    @Override
    public void onCreate() {
        super.onCreate();

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        if (database == null) {
            database = FirebaseDatabase.getInstance();
            if (!s_persistenceInitialized) {
                database.setPersistenceEnabled(true);
                s_persistenceInitialized = true;
            }
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

}
