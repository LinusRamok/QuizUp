package com.example.kamlesh.frd;

import android.app.Application;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import java.util.HashMap;
import java.util.Map;

import bolts.Task;

/**
 * Created by kamlesh on 18-01-2018.
 */

public class MyApp extends Application {

    private static final String TAG = MyApp.class.getSimpleName();
    public MyApp() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        final FirebaseRemoteConfig firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

        // set in-app defaults

        Map<String, Object> remoteConfigDefaults = new HashMap();
        remoteConfigDefaults.put(ForceUpdateChecker.KEY_UPDATE_REQUIRED, false);
        remoteConfigDefaults.put(ForceUpdateChecker.KEY_CURRENT_VERSION, "1.0.0");
        remoteConfigDefaults.put(ForceUpdateChecker.KEY_UPDATE_URL, "http://play.google.com/store/apps/details?id=com.SahuAppsPvtLtd.myTrainEnquiryApp");
        remoteConfigDefaults.put(UrlUpdateChecker.URL_UPDATE_REQUIRED,false);
        remoteConfigDefaults.put(UrlUpdateChecker.UPDATED_URL,"https://quizone-apis.appspot.com/_ah/api/myapi/v1/");

        firebaseRemoteConfig.setDefaults(remoteConfigDefaults);

        firebaseRemoteConfig.fetch(60) // fetch every minutes

                .addOnCompleteListener(new OnCompleteListener<Void>() {

                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
                        if (task.isSuccessful()) {

                            Log.d(TAG, "remote config is fetched.");

                            firebaseRemoteConfig.activateFetched();

                        }
                    }

                });
    }
}
