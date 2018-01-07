package com.quiz.up;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLOutput;

public class GamePlay extends AppCompatActivity {
        JSONObject Questions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Applying immersive mode
        View dv=getWindow().getDecorView();
        int ui=View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        dv.setSystemUiVisibility(ui);

        setContentView(R.layout.activity_game_play);


        try
        {
            Questions = new JSONObject( getIntent().getStringExtra("jsonobj"));
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }
        

      
    }
}
