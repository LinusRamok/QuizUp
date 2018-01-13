package com.example.kamlesh.frd;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;
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


//adding BufferedReader

        try {
            BufferedReader buffer = new BufferedReader(new InputStreamReader(getApplicationContext().getAssets().open("Ranking.json")));
            response=new StringBuffer();
            String line;
            while ((line=buffer.readLine())!=null){
                response.append(line);
                response.append("\n");
            }
        } catch (IOException e) {
        }
        //data is in string form:json format assingned in responsed
        String responsed=response.toString();
        System.out.println(responsed);
        //Creating recycler view object
        rankinglistss = (RecyclerView) findViewById(R.id.recycler);
        rankinglistss.setLayoutManager(new LinearLayoutManager(this));


        JSONObject object = null;
        try {
            object = new JSONObject(responsed);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject obj= null;
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
}
