package com.example.kamlesh.frd;

import android.content.Context;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

/**
 * Created by linus on 31-01-2018.
 */

public class UrlUpdateChecker {
    private static final String TAG = UrlUpdateChecker.class.getSimpleName();

    public static final String URL_UPDATE_REQUIRED = "url_update_required";
    public static final String UPDATED_URL = "updated_url";
    private UrlUpdateChecker.OnUrlUpdateNeededListener onUrlUpdateNeededListener;
    private Context context;

    public interface OnUrlUpdateNeededListener {
        void onUpdateNeeded(String updateUrl);
    }

    public static UrlUpdateChecker.Builder with(@NonNull Context context) {
        return new UrlUpdateChecker.Builder(context);
    }

    public UrlUpdateChecker(@NonNull Context context,
                              UrlUpdateChecker.OnUrlUpdateNeededListener onUrlUpdateNeededListener) {
        this.context = context;
        this.onUrlUpdateNeededListener = onUrlUpdateNeededListener;
    }
    public void check() {
        final FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.getInstance();
        System.out.println("function url");

        if (remoteConfig.getBoolean(URL_UPDATE_REQUIRED)) {
            String updateUrl = remoteConfig.getString(UPDATED_URL);
            System.out.println("url" + updateUrl);
                onUrlUpdateNeededListener.onUpdateNeeded(updateUrl);
        }
    }
    

    public static class Builder {

        private Context context;
        private UrlUpdateChecker.OnUrlUpdateNeededListener onUrlUpdateNeededListener;

        public Builder(Context context) {
            this.context = context;
        }

        public UrlUpdateChecker.Builder onUrlUpdateNeeded(UrlUpdateChecker.OnUrlUpdateNeededListener onUrlUpdateNeededListener) {
            this.onUrlUpdateNeededListener = onUrlUpdateNeededListener;
            return this;
        }

        public UrlUpdateChecker build() {
            return new UrlUpdateChecker(context, onUrlUpdateNeededListener);
        }

        public UrlUpdateChecker check() {
            UrlUpdateChecker UrlUpdateChecker = build();
            UrlUpdateChecker.check();

            return UrlUpdateChecker;
        }
    }
}
