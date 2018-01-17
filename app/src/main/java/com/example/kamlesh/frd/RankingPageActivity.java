package com.example.kamlesh.frd;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.akexorcist.roundcornerprogressbar.TextRoundCornerProgressBar;
import com.akexorcist.roundcornerprogressbar.common.BaseRoundCornerProgressBar;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.kamlesh.frd.CircularProgressBar.CircularProgressBar;
import com.example.kamlesh.frd.ScorePagePOJO.PlayerScore;
import com.google.gson.Gson;
import com.podcopic.animationlib.library.AnimationType;
import com.podcopic.animationlib.library.StartSmartAnimation;
import com.quiz.up.RankingPagePOJO.Top5;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by RAJA on 12-01-2018.
 */

public class RankingPageActivity extends AppCompatActivity {
    StringBuffer response;
    RecyclerView rankinglistss;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ranking_page);

        //Creating recycler view object
        rankinglistss = (RecyclerView) findViewById(R.id.recycler);
        rankinglistss.setLayoutManager(new LinearLayoutManager(this));
            StartSmartAnimation.startAnimation(findViewById(R.id.recycler) , AnimationType.SlideInUp , 2000 , 0 , true );
        String name=getIntent().getStringExtra("topicName");


//volley.......................................................
//putting entered value into URL
        String URL = "https://quizgame-backend.appspot.com/_ah/api/myapi/v1/ranking?Topic=sports&PID=sunil%40gmail.com";
//Data Downloader-Volley
        final StringRequest request = new StringRequest(URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String data) {
if(data!=null) {
    ProgressBar progressBar=findViewById(R.id.progress);
    progressBar.setVisibility(View.GONE);
    JSONObject object = null;
    try {
        object = new JSONObject(data);
    } catch (JSONException e) {
        e.printStackTrace();
    }
    JSONObject obj = null;
    try {
        obj = object.getJSONObject("message");
    } catch (JSONException e) {
        e.printStackTrace();
    }
    JSONArray array = null;
    try {
        array = obj.getJSONArray("top5");
    } catch (JSONException e) {
        e.printStackTrace();
    }

    Top5[] rankinglists = new Gson().fromJson(String.valueOf(array), Top5[].class);
    rankinglistss.setAdapter(new RankinglistAdapter(getApplicationContext(), rankinglists));
}
else {
    ProgressBar progressBar=findViewById(R.id.progress);
    progressBar.setVisibility(View.VISIBLE);
}
            }
        },
//Error listener:Server error
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
//Adding request Queue
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
        //     StartSmartAnimation.startAnimation( findViewById(R.id.test) , AnimationType.ZoomIn,1000, 0 , true );
    }


    }

