package com.example.kamlesh.frd;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kamlesh.frd.CircularProgressBar.CircularProgressBar;
import com.example.kamlesh.frd.ScorePagePOJO.PlayerScore;
import com.google.gson.Gson;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ScorePageActivity extends AppCompatActivity {
    TextView a1,a2,a3,a4,a5,a6,a7,a8,a9,a10;
    StringBuffer response;
    int ans[]=new int[10],score=0;
    String questionsAndAnswers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.score_page);

        ans=getIntent().getIntArrayExtra("userAnswers");
        questionsAndAnswers=getIntent().getStringExtra("QuestionAndAnswers");

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
        System.out.println("Score is :"+score);

        //Toast.makeText(this, ans[0]+""+ans[1]+""+ans[2]+""+ans[3]+""+ans[4]+""+ans[5]+""+ans[6]+""+ans[7]+""+ans[8]+score, Toast.LENGTH_LONG).show();

        //adding BufferedReader
        try {
            BufferedReader buffer = new BufferedReader(new InputStreamReader(getApplicationContext().getAssets().open("Score.json")));
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
           PlayerScore data = new Gson().fromJson(responsed, PlayerScore.class);
           System.out.println(data.getMessage().getAccuracy());

//        "topic": "sports",
//                "totalScore": 235,
//                "accuracy": "171.43",
//                "q_Total": 200,
//                "q_Solved": 7,
//                "correct": 12,


        TextView topic,accuracy,q_solved,correct;
        topic= (TextView) findViewById(R.id.topic);
      //  accuracy= (TextView) findViewById(R.id.accuracy);
        topic.setText(data.getMessage().getTopic());

    //Accuracy
        String a=  data.getMessage().getAccuracy();

        final CircularProgressBar c1= (CircularProgressBar) findViewById(R.id.accuracy);
        c1.animateProgressTo(0, 100, new CircularProgressBar.ProgressAnimationListener() {
            @Override
            public void onAnimationStart() {
            }

            @Override
            public void onAnimationFinish() {

            }

            @Override
            public void onAnimationProgress(int progress) {
                c1.setTitle(String.valueOf(progress));
            }
        });


        final CircularProgressBar c2= (CircularProgressBar) findViewById(R.id.question_completed);
        c2.setMax(data.getMessage().getQTotal());
        c2.animateProgressTo(0, data.getMessage().getQSolved(), new CircularProgressBar.ProgressAnimationListener() {
            @Override
            public void onAnimationStart() {
            }

            @Override
            public void onAnimationFinish() {

            }

            @Override
            public void onAnimationProgress(int progress) {
                c2.setTitle(String.valueOf(progress));
            }
        });

//ranking Button
        LinearLayout rankingButton= (LinearLayout) findViewById(R.id.ranking_click);
        rankingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ScorePageActivity.this, "Ranking Button clicked", Toast.LENGTH_SHORT).show();
            }
        });

//volley.......................................................
////putting entered value into URL
//        String URL = "https://";
////Data Downloader-Volley
//        final StringRequest request = new StringRequest(URL, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String data) {
//                PlayerData data1 = new Gson().fromJson(data, PlayerData.class);
//                }
//        },
////Error listener:Server error
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(ScorePageActivity.this, "Server error Please retry", Toast.LENGTH_SHORT).show();
//                    }
//                });
////Adding request Queue
//        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
//        queue.add(request);


//adding font
//        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "rishabh.ttf");
//        a1 = (TextView)findViewById(R.id.ranking);
//        a2 = (TextView)findViewById(R.id.topic);
//        a3 = (TextView)findViewById(R.id.score);
//        a4 = (TextView)findViewById(R.id.score_text);
//        a5 = (TextView)findViewById(R.id.accuracy);
//        a6 = (TextView)findViewById(R.id.slash);
//        a7 = (TextView)findViewById(R.id.total_accuracy);
//        a8 = (TextView)findViewById(R.id.solved_question);
//        a9 = (TextView)findViewById(R.id.slash1);
//        a10 = (TextView)findViewById(R.id.total_question);
//        a1.setTypeface(custom_font);
//        a2.setTypeface(custom_font);
//        a3.setTypeface(custom_font);
//        a4.setTypeface(custom_font);
//        a5.setTypeface(custom_font);
//        a6.setTypeface(custom_font);
//        a7.setTypeface(custom_font);
//        a8.setTypeface(custom_font);
//        a9.setTypeface(custom_font);
//        a10.setTypeface(custom_font);

   //     StartSmartAnimation.startAnimation( findViewById(R.id.test) , AnimationType.ZoomIn,1000, 0 , true );
    }
}