package com.example.kamlesh.frd;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;


public class logout_page extends AppCompatActivity {
    public FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);
        mAuth = FirebaseAuth.getInstance();
        Button signout = findViewById(R.id.signout);
        TextView name = findViewById(R.id.name);
        TextView email =findViewById(R.id.email);
        TextView photourl =findViewById(R.id.pic_url);

        System.out.println("here is namme :"+mAuth.getCurrentUser().getDisplayName());
        System.out.println("here is email :"+mAuth.getCurrentUser().getEmail());
        System.out.println("here is picurl:"+mAuth.getCurrentUser().getPhotoUrl());
        name.setText(mAuth.getCurrentUser().getDisplayName());
        email.setText(mAuth.getCurrentUser().getEmail());
        photourl.setText((CharSequence) mAuth.getCurrentUser().getPhotoUrl().getPath());

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                LoginManager.getInstance().logOut();
                startActivity(new Intent(logout_page.this,LoginActivity.class));
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
