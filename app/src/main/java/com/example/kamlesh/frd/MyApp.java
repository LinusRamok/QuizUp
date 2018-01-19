package com.example.kamlesh.frd;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by kamlesh on 18-01-2018.
 */

public class MyApp extends Application {

    public MyApp() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
