package com.example.svarog.project01;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import com.facebook.stetho.Stetho;

public class MyApp extends Application {

    private final static String MOST_POPULAR = "popular";
    private final static String TAG = "Application Class";
    @Override
    public void onCreate() {
        super.onCreate();

        Stetho.initializeWithDefaults(this);
        Log.i(TAG, "shared pref.  " + getSharedPreferences("theApp", MODE_PRIVATE).contains("option") + " option is : " + getSharedPreferences("theApp", MODE_PRIVATE).getString("option", null));

        if (!getSharedPreferences("theApp", MODE_PRIVATE).contains("option")) {
            Log.i(TAG, "shared pref. is not found");
            SharedPreferences pref = getApplicationContext().getSharedPreferences("theApp", MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("option",MOST_POPULAR);
            editor.commit();
        }
    }
}
