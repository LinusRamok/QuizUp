package com.example.kamlesh.frd;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
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
import com.example.kamlesh.frd.Models.Questionlist;
import com.example.kamlesh.frd.Models.Topic;
import com.example.kamlesh.frd.ScorePagePOJO.PlayerScore;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.podcopic.animationlib.library.AnimationType;
import com.podcopic.animationlib.library.StartSmartAnimation;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import me.rishabhkhanna.customtogglebutton.CustomToggleButton;

public class ScorePageActivity extends AppCompatActivity {
    TextView a1,a2,a3,a4,a5,a6,a7,a8,a9,a10,a11;
    RecyclerView lists;
    JSONObject object= null;
    JSONArray array=null;
    StringBuffer response;
    int ans[]=new int[10],score=0;
    double scoreArray[]=new double[9];
    int ans[]=new int[10];
    String questionsAndAnswers;
    double TimeForEachQues[]=new double[7];
    double TimeForEachQues[]=new double[7],scoreArray[]=new double[9];
    FirebaseStorage storage =FirebaseStorage.getInstance();
    String Topic_name;
    String topic_url;
    JSONObject respass=null;
    double finalscore=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.score_page);
        TimeForEachQues =getIntent().getDoubleArrayExtra("TimerValues");
        ans=getIntent().getIntArrayExtra("userAnswers");
        scoreArray=getIntent().getDoubleArrayExtra("scoreArray");
        questionsAndAnswers=getIntent().getStringExtra("QuestionAndAnswers");
        finalscore=getIntent().getDoubleExtra("Score",0);
        ImageView topic_logo =findViewById(R.id.topic_logo);

        //setting topic name in textview
        Topic_name=getIntent().getExtras().getString("topic_name");

        topic_url = getIntent().getExtras().getString("topic_url");

        TextView t=findViewById(R.id.test2);
        t.setText(Topic_name);


try {
    StorageReference storageRef = storage.getReference(topic_url);

    Glide.with(this)
            .load(storageRef)
            .into(topic_logo);
}catch (Exception e){
    System.out.println(e.getMessage());
}
scoreArray[7]=0;
scoreArray[8]=0;
        int cqn=0;
        for(int i=0;i<7;i++)
        {
             if (ans[i]==1)
             {  scoreArray[i]+=10;
                if(TimeForEachQues[i]>5)
                {
                    scoreArray[i]=scoreArray[i]-((TimeForEachQues[i]-5)*0.6);
                }
                if(scoreArray[i]<3)
                {
                    scoreArray[i]=3;
                }
                score+=10;}
            else if(ans[i]==9) {
                 score -= 2;
                 cqn=i;
                scoreArray[i]-=2;
             }
        }
        if(cqn==1)
        {
            if(TimeForEachQues[cqn]>5)
                scoreArray[7]=scoreArray[7]-((TimeForEachQues[cqn]-5)*0.6);
            if(scoreArray[7]<3)
                scoreArray[7]=3;
            score+=12;}
        if (ans[8]==3) {
            score -= 2;
            scoreArray[8]-=2;
        }
        System.out.println("score Array"+scoreArray[0]+" "+scoreArray[1]+" "+scoreArray[2]+" "+scoreArray[3]+" "+scoreArray[4]+" "+scoreArray[5]+" "+scoreArray[6]+" "+scoreArray[7]+" "+scoreArray[8]);
        finalscore=scoreArray[0]+scoreArray[1]+scoreArray[2]+scoreArray[3]+scoreArray[4]+scoreArray[5]+scoreArray[6]+scoreArray[7]+scoreArray[8];
        System.out.println("Score issssssssssssssssssssssssssssss :"+finalscore);
score=10;
        int q=ans[0]+ans[1]+ans[2]+ans[3]+ans[4]+ans[5]+ans[6]+ans[7];
        int s=q-9;
        System.out.println(s);
        float ca=(float)s;
        float caa=(ca/7)*100;
        float ro=Math.round(caa*100)/100;
        System.out.println("floattttttttttttttttttttt"+ro);
        TextView textView=findViewById(R.id.current_accuracy);
        textView.setText(String.valueOf(ro)+"%");

//Score setting on textview
        TextView main_score;
        main_score=findViewById(R.id.score);
        main_score.setText(String.valueOf(score));
        main_score.setText(String.valueOf(finalscore));

//ranking Button
        LinearLayout rankingButton= (LinearLayout) findViewById(R.id.ranking_click);
        rankingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            //    Toast.makeText(ScorePageActivity.this, "Ranking Button clicked", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getApplicationContext(),RankingPageActivity.class);
                intent.putExtra("topicName",Topic_name);
                startActivity(intent);
            }
        });
//replay button
        LinearLayout replay=(LinearLayout)findViewById(R.id.play_game);
        replay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            //    Toast.makeText(ScorePageActivity.this, "replay", Toast.LENGTH_SHORT).show();
                Intent intent1=new Intent(getApplicationContext(),topic_page.class);
                Topic topicdetails =new Topic(Topic_name,topic_url,"");
                intent1.putExtra("topic_details",new Gson().toJson(topicdetails));
                startActivity(intent1);
                finish();

            }
        });
//change topic
        LinearLayout change_topic=(LinearLayout)findViewById(R.id.change_topic);
        change_topic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
           //     Toast.makeText(ScorePageActivity.this, "change topic", Toast.LENGTH_SHORT).show();
//                Intent intent=new Intent(getApplicationContext(),Select_Topic.class);
//                startActivity(intent);
                Intent intent  =new Intent(ScorePageActivity.this,Select_Topic.class);
                startActivity(intent);
                finish();
            }
        });
        //change topic
        LinearLayout quit=(LinearLayout)findViewById(R.id.quit);
        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             //   Toast.makeText(ScorePageActivity.this, "quit game", Toast.LENGTH_SHORT).show();
//                Intent intent=new Intent(getApplicationContext(),Select_Topic.class);
//                startActivity(intent);
                Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                homeIntent.addCategory( Intent.CATEGORY_HOME );
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);

            }
        });

//volley.......................................................
//putting entered value into URL
        String PID= FirebaseAuth.getInstance().getCurrentUser().getUid();
        String TopicKey = null;
        try {

            TopicKey = URLEncoder.encode(Topic_name, "UTF-8").replaceAll("\\+", "%20");
            System.out.println("here is encoded key :" + TopicKey);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();

        }


        String URL = "https://quizgame-backend.appspot.com/_ah/api/myapi/v1/updateStats?PID="+PID+"&Q_Correct="+s+"&Score="+score+"&Topic="+TopicKey;
        String URL = "https://quizgame-backend.appspot.com/_ah/api/myapi/v1/updateStats?PID="+PID+"&Q_Correct="+s+"&Score="+finalscore+"&Topic="+TopicKey;
        System.out.println("url :"+URL);

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

//font
        a1=findViewById(R.id.test2);
        a2=findViewById(R.id.score_text);
        a3=findViewById(R.id.acc_text);
        a4=findViewById(R.id.overall_text);
        a5=findViewById(R.id.ques_text);
        a6=findViewById(R.id.ranking);
        a7=findViewById(R.id.t_text);
        a8=findViewById(R.id.p_g_text);
        a9=findViewById(R.id.quit_text);
        a10=findViewById(R.id.quest);

        CustomToggleButton toggleButton=findViewById(R.id.toggle);
        Typeface ourBoldFont = Typeface.createFromAsset(getAssets(), "fonts/primebold.otf");
        Typeface ourLightFont = Typeface.createFromAsset(getAssets(), "fonts/primelight.otf");

        a1.setTypeface(ourBoldFont);
        a2.setTypeface(ourBoldFont);
        a3.setTypeface(ourBoldFont);
        a4.setTypeface(ourBoldFont);
        a5.setTypeface(ourBoldFont);
        a6.setTypeface(ourBoldFont);
        a7.setTypeface(ourBoldFont);
        a8.setTypeface(ourBoldFont);
        a9.setTypeface(ourBoldFont);
        toggleButton.setTypeface(ourBoldFont);

        //Creating recycler view object
        lists = (RecyclerView) findViewById(R.id.recycler1);
        lists.setLayoutManager(new LinearLayoutManager(this));
        StartSmartAnimation.startAnimation( findViewById(R.id.recycler1) , AnimationType.SlideInUp,5000, 00 ,true );
//listview questions and answers

        System.out.println("DATA................."+questionsAndAnswers);

        try {
            object = new JSONObject(questionsAndAnswers);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            array=object.getJSONArray("questionlist");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Questionlist[] list = new Gson().fromJson(String.valueOf(array), Questionlist[].class);
        lists.setAdapter(new QuestionlistAdapter(getApplicationContext(), list,ans));

        findViewById(R.id.q_a).setVisibility(View.GONE);
        final CustomToggleButton toggle = (CustomToggleButton) findViewById(R.id.toggle);
        toggle.setBackgroundDrawable(getResources().getDrawable(R.drawable.toggle_background_off));
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //Toast.makeText(getApplicationContext(), "on", Toast.LENGTH_SHORT).show();
                    toggle.setBackgroundDrawable(getResources().getDrawable(R.drawable.toggle_background_on));
                    findViewById(R.id.q_a).setVisibility(View.VISIBLE);
                    findViewById(R.id.circle_progress).setVisibility(View.GONE);
                    findViewById(R.id.ranking_click).setVisibility(View.GONE);
//                    StartSmartAnimation.startAnimation( findViewById(R.id.q_a) , AnimationType.SlideInDown,1000, 00 ,true );
                    StartSmartAnimation.startAnimation( findViewById(R.id.recycler1) , AnimationType.SlideInUp,300, 0 ,true );
                } else {
                    //Toast.makeText(getApplicationContext(), "off", Toast.LENGTH_SHORT).show();
                    toggle.setBackgroundDrawable(getResources().getDrawable(R.drawable.toggle_background_off));
                    //        StartSmartAnimation.startAnimation( findViewById(R.id.q_a) , AnimationType.SlideInUp,500, 0 ,true );
                    findViewById(R.id.q_a).setVisibility(View.GONE);
                    findViewById(R.id.circle_progress).setVisibility(View.VISIBLE);
                    findViewById(R.id.ranking_click).setVisibility(View.VISIBLE);
                }
            }
        });



    }

    @Override
    public void onBackPressed() {
        Intent intent1=new Intent(getApplicationContext(),topic_page.class);
        Topic topicdetails =new Topic(Topic_name,topic_url,"");
        intent1.putExtra("topic_details",new Gson().toJson(topicdetails));
        startActivity(intent1);
        finish();
    }
}