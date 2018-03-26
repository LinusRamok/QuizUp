package com.alias.quizone;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class MultiplayerGameplay extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer_gameplay);
        Toast.makeText(this, "multimulti", Toast.LENGTH_SHORT).show();
    }
}
