package com.example.kamlesh.frd;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.kamlesh.frd.CircularProgressBar.CircularProgressBar;
import com.example.kamlesh.frd.ScorePagePOJO.PlayerScore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;


import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ScorePageActivity extends AppCompatActivity {
    StringBuffer response;
    int ans[]=new int[10],score=0;
    String questionsAndAnswers;
    FirebaseStorage storage =FirebaseStorage.getInstance();
    String Topic_name;
    JSONObject respass=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.score_page);

        ans=getIntent().getIntArrayExtra("userAnswers");
        questionsAndAnswers=getIntent().getStringExtra("QuestionAndAnswers");
        ImageView topic_logo =findViewById(R.id.topic_logo);

        //setting topic name in textview
        final String topic_name=getIntent().getExtras().getString("topic_name");
        String topic_url=getIntent().getExtras().getString("topic_url");

        TextView t=findViewById(R.id.test2);
        t.setText(topic_name);


try {
    StorageReference storageRef = storage.getReference(topic_url);

    Glide.with(this)
            .load(storageRef)
            .into(topic_logo);
}catch (Exception e){
    System.out.println(e.getMessage());
}


        for(int i=0;i<7;i++)
        {
            if (ans[i]==0)
                score-=4;
            else if (ans[i]==1)
                score+=10;
            else if(ans[i]==9)
                score-=2;
        }
        if(ans[7]==1)
            score+=12;
        else if (ans[7]==0)
            score-=4;
        if (ans[8]==3)
            score-=2;


        System.out.println("Score issssssssssssssssssssssssssssss :"+score);
        int q=ans[0]+ans[1]+ans[2]+ans[3]+ans[4]+ans[5]+ans[6]+ans[7]+ans[8];
        int s=q-13;

//Score setting on textview
        TextView main_score;
        main_score=findViewById(R.id.score);
        main_score.setText(String.valueOf(score));

//ranking Button
        LinearLayout rankingButton= (LinearLayout) findViewById(R.id.ranking_click);
        rankingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ScorePageActivity.this, "Ranking Button clicked", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getApplicationContext(),RankingPageActivity.class);
                intent.putExtra("topicName",topic_name);
                startActivity(intent);
            }
        });
//replay button
        LinearLayout replay=(LinearLayout)findViewById(R.id.play_game);
        replay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ScorePageActivity.this, "replay", Toast.LENGTH_SHORT).show();
//                Intent intent1=new Intent(getApplicationContext(),topic_page.class);
//                startActivity(intent1);
            }
        });
//change topic
        LinearLayout change_topic=(LinearLayout)findViewById(R.id.change_topic);
        change_topic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ScorePageActivity.this, "change topic", Toast.LENGTH_SHORT).show();
//                Intent intent=new Intent(getApplicationContext(),Select_Topic.class);
//                startActivity(intent);
            }
        });
        //change topic
        LinearLayout quit=(LinearLayout)findViewById(R.id.quit);
        change_topic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ScorePageActivity.this, "quit game", Toast.LENGTH_SHORT).show();
//                Intent intent=new Intent(getApplicationContext(),Select_Topic.class);
//                startActivity(intent);
            }
        });

//volley.......................................................
//putting entered value into URL
        String URL = "https://quizgame-backend.appspot.com/_ah/api/myapi/v1/updateStats?PID=milind%40gmail.com&Q_Correct="+s+"&Score="+score+"&Topic="+topic_name;
//Data Downloader-Volley
        final StringRequest request = new StringRequest(URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String data1) {
                System.out.println(data1);
                PlayerScore data = new Gson().fromJson(data1, PlayerScore.class);
                System.out.println(data.getMessage().getAccuracy());

//Accuracy
                String a1=  data.getMessage().getAccuracy();
                float result = Float.parseFloat(a1);
                int intresult=(int)result;

                final CircularProgressBar c1= (CircularProgressBar) findViewById(R.id.accuracy);
                c1.setMax(100);
                c1.animateProgressTo(0, intresult, new CircularProgressBar.ProgressAnimationListener() {
                    @Override
                    public void onAnimationStart() {
                    }

                    @Override
                    public void onAnimationFinish() {

                    }

                    @Override
                    public void onAnimationProgress(int progress) {
                        c1.setTitle(String.valueOf(progress)+"%");
                    }
                });

//question completed
                final CircularProgressBar c2= (CircularProgressBar) findViewById(R.id.question_completed);
                c2.setMax(100);
                float a=data.getMessage().getCorrect();
                float b=data.getMessage().getQTotal();
                System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaa"+a+"bbbbbbbbbbbbbbbb"+b);
                float c=(a/b)*100;
                System.out.println(c);
                c2.animateProgressTo(0, (int) c, new CircularProgressBar.ProgressAnimationListener() {
                    @Override
                    public void onAnimationStart() {
                    }

                    @Override
                    public void onAnimationFinish() {

                    }

                    @Override
                    public void onAnimationProgress(int progress) {
                        c2.setTitle(String.valueOf(progress)+"%");
                    }
                });

                }
        },
//Error listener:Server error
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ScorePageActivity.this, "Server error Please retry", Toast.LENGTH_SHORT).show();
                    }
                });
//Adding request Queue
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);


        //     StartSmartAnimation.startAnimation( findViewById(R.id.test) , AnimationType.ZoomIn,1000, 0 , true );





    }
}