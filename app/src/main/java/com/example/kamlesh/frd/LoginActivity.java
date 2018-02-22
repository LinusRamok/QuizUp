package com.example.kamlesh.frd;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Set;

import me.relex.circleindicator.CircleIndicator;


public class LoginActivity extends AppCompatActivity implements UrlUpdateChecker.OnUrlUpdateNeededListener {

    LinearLayout facebookLogin, googleLogin, guestLogin;
    public FirebaseAuth mAuth;
    public CallbackManager mCallbackManager;
    FirebaseAuth.AuthStateListener mAuthListener;
    final static int RC_SIGN_IN = 2;
    static GoogleApiClient mGoogleSignInClient;
    SharedPreferences prefs;
    ProgressDialog dialog;
    static String URLprefix;
    @Override
    public void onUpdateNeeded(String updateUrl) {
        URLprefix=updateUrl;
        System.out.println("url update"+URLprefix);
    }

    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);

    }

    @Override
    protected void onResume() {
        super.onResume();

        View dv=getWindow().getDecorView();
        int ui= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        dv.setSystemUiVisibility(ui);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            View decorView= getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View dv = getWindow().getDecorView();
        int ui = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        dv.setSystemUiVisibility(ui);

        setContentView(R.layout.activity_login);
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        viewPager.setPageTransformer(false, new CustomLoginPageTransformer());
        viewPager.setAdapter(new CustomLoginPagerAdapter(this));
        indicator.setViewPager(viewPager);
        viewPager.setPageMargin(12);
        viewPager.setOffscreenPageLimit(2);

        UrlUpdateChecker.with(this).onUrlUpdateNeeded(this).check();

        facebookLogin = (LinearLayout) findViewById(R.id.facebookLogin);
        googleLogin = (LinearLayout) findViewById(R.id.googleLogin);
        guestLogin = (LinearLayout) findViewById(R.id.guestLogin);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        dialog = new ProgressDialog(LoginActivity.this);

        mAuth = FirebaseAuth.getInstance();


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .requestProfile()
                .requestId()
                .build();

        mGoogleSignInClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(LoginActivity.this, "something went wrong", Toast.LENGTH_LONG).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    System.out.println("oth state change");
                    if (prefs.getBoolean("Islogin", false)) {
                        startActivity(new Intent(LoginActivity.this, Select_Topic.class));
                        finish();
                    } else {

                        String DispName = null;
                        try {

                            DispName = URLEncoder.encode(user.getDisplayName(), "UTF-8").replaceAll("\\+", "%20");
                            System.out.println("here is encoded key :" + DispName);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        String regPlayer = "https://quizgame-backend.appspot.com/_ah/api/myapi/v1/RegPlayer?PID=" + user.getUid() + "&Name=" + DispName + "&pic_url=" + user.getPhotoUrl();


                        RegisterPlayerAsync reg = new RegisterPlayerAsync();
                        reg.execute(regPlayer);

                    }
                }

            }

        };


        mCallbackManager = CallbackManager.Factory.create();
        final LoginButton loginButton = findViewById(R.id.login_button);

        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("TAG", "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult);
                System.out.println("login result " + loginResult.getAccessToken().getPermissions().toString());
//                System.out.println(loginResult.getRecentlyGrantedPermissions());

                Set<String> setIterable = loginResult.getAccessToken().getPermissions();
                Iterable iterable = setIterable;
                Iterator iterator = iterable.iterator();
                System.out.println(" granted : " + iterator.next().toString());

            }


            @Override
            public void onCancel() {
                Log.d("TAG", "facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("TAG", "facebook:onError", error);
                // ...
            }
        });

        facebookLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 loginButton.performClick();
            }
        });

        googleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        guestLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LoginActivity.this, "coming soon",
                        Toast.LENGTH_SHORT).show();
            }
        });


    }


    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleSignInClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
                System.out.println("sign in successfull");
                System.out.println("disp name " + account.getDisplayName());
                System.out.println("email " + account.getEmail());
                System.out.println("photourl " + account.getPhotoUrl());
                System.out.println("given name " + account.getGivenName());
                System.out.println("family name " + account.getFamilyName());
                System.out.println(account);
            } catch (Exception e) {
                // Google Sign In failed, update UI appropriately
                Log.w("TAG", "Google sign in failed", e);
                // ...
            }

        } else {
            System.out.println("mcallback manager...");
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }


    }


    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
     //   Log.d("TAG", "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //  updateUI(user);
                            prefs.edit().putBoolean("Islogin", false).commit();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            //  Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            //  updateUI(null);
                        }

                        // ...
                    }
                });
    }


    private void handleFacebookAccessToken(LoginResult loginResult) {
        //  Log.d("TAG", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(loginResult.getAccessToken().getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();

//                            System.out.println(user.getEmail());
                            System.out.println(user.getDisplayName());
                            System.out.println(user.getPhotoUrl());
                            //    updateUI(user);
                            prefs.edit().putBoolean("Islogin", false).commit();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //  updateUI(null);
                        }

                        // ...
                    }
                });
    }


    public class RegisterPlayerAsync extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            if (!isFinishing()) {
                dialog.setTitle("Registering user...");
                dialog.show();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(params[0]);

                urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(10000);
                urlConnection.setConnectTimeout(15000);

                urlConnection.setDoOutput(true);

                urlConnection.connect();

                BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));

                char[] buffer = new char[1024];

                String jsonString = new String();

                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
                br.close();

                jsonString = sb.toString();

                System.out.println("JSON: " + jsonString);
                urlConnection.disconnect();

                //return new JSONObject(jsonString);

                return jsonString;

            } catch (Exception e) {
                e.fillInStackTrace();
                return e.toString();
            }
        }
        @Override
        protected void onPostExecute(String s) {
            System.out.println("after player registr call...");
            dialog.dismiss();
            if(s.contains("success") ||s.contains("exists")){
                System.out.println("yeh register success");

                prefs.edit().putBoolean("Islogin",true).commit();
                startActivity(new Intent(LoginActivity.this, Select_Topic.class));
                finish();
            }else{
                Toast.makeText(LoginActivity.this,"error pls retry", Toast.LENGTH_LONG);
                System.out.println("player register error pls retry....");
            }
        }
    }

        @Override
        public void onBackPressed() {
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
        }

}