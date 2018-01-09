package com.quiz.up;

import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GamePlay extends AppCompatActivity implements View.OnClickListener {
    String jsonString;
    JSONObject jsonObject=null;
    QuestionsApi obj;
    BufferedReader reader;
    int score=0;
    List<Questionlist> q_and_a=new ArrayList<>();
    int i=0;

    TextView optionA, optionB, optionC, optionD, perk1, perk2;
    Boolean perk_one_isclicked=false,perk_two_isclicked=false,view_changed=false;
    TextView question, questionNumber;

    @Override
    protected void onResume() {
        super.onResume();

        View dv=getWindow().getDecorView();
        int ui=View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        dv.setSystemUiVisibility(ui);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View dv=getWindow().getDecorView();
        int ui=View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        dv.setSystemUiVisibility(ui);

        setContentView(R.layout.activity_game_play);
        question = (TextView)findViewById(R.id.question);
        questionNumber =(TextView)findViewById(R.id.questionNumber);
        optionA =(TextView)findViewById(R.id.optionA);
        optionA.setOnClickListener(this);
        optionB =(TextView)findViewById(R.id.optionB);
        optionB.setOnClickListener(this);
        optionC =(TextView)findViewById(R.id.optionC);
        optionC.setOnClickListener(this);
        optionD =(TextView)findViewById(R.id.optionD);
        optionD.setOnClickListener(this);
        perk1 =(TextView)findViewById(R.id.perk1);
        perk1.setOnClickListener(this);
        perk2 =(TextView)findViewById(R.id.perk2);
        perk2.setOnClickListener(this);

        Typeface ourBoldFont = Typeface.createFromAsset(getAssets(), "fonts/primebold.otf");
        Typeface ourLightFont = Typeface.createFromAsset(getAssets(), "fonts/primelight.otf");

        question.setTypeface(ourBoldFont);
        questionNumber.setTypeface(ourLightFont);
        optionA.setTypeface(ourLightFont);
        optionB.setTypeface(ourLightFont);
        optionC.setTypeface(ourLightFont);
        optionD.setTypeface(ourLightFont);
        perk1.setTypeface(ourBoldFont);
        perk2.setTypeface(ourBoldFont);

        jsonString = getIntent().getStringExtra("jsonobj");
        obj=new Gson().fromJson(jsonString , QuestionsApi.class);
        q_and_a= obj.getQuestionlist();

        //setting the first question and options beforehand
        setQuestionsAndOptions(0);

    }

    @Override
    public void onClick(View view) {

        TextView tv=(TextView)view;

            switch (tv.getId()) {
                case R.id.optionA:
                    set_answer_anim_and_score("A",i);
                    break;
                case R.id.optionB:
                    set_answer_anim_and_score("B",i);
                    break;
                case R.id.optionC:
                    set_answer_anim_and_score("C",i);
                    break;
                case R.id.optionD:
                    set_answer_anim_and_score("D",i);
                    break;
                case R.id.perk1:
                    if(!perk_one_isclicked)
                    perk_one(i);
                    break;
                case R.id.perk2:
                    if(!perk_two_isclicked)
                    perk_two();
                    break;
        }
    }

    public void setQuestionsAndOptions (int k)
    {
        if(perk_one_isclicked && !view_changed)
        {
            // making the textviews of options VISIBLE again which were set to gone in perk one
            view_changed=true;

            optionA.setVisibility(View.VISIBLE);
            optionB.setVisibility(View.VISIBLE);
            optionC.setVisibility(View.VISIBLE);
            optionD.setVisibility(View.VISIBLE);
        }
        if(k<7) {
            // logic for setting the question and options relative to the index passed

            questionNumber.setText(k + 1 + "/7");
            question.setText(q_and_a.get(k).getQuestion());
            optionA.setText(q_and_a.get(k).getA());
            optionB.setText(q_and_a.get(k).getB());
            optionC.setText(q_and_a.get(k).getC());
            optionD.setText(q_and_a.get(k).getD());
        }
        else{
            // logic to be applied after seven questions, i.e., passing score to the next activity, calling the next activity

        }

    }
    public void set_answer_anim_and_score(final String ans, final int k)
    {
        if(k<7)
        if (ans.equals(q_and_a.get(k).getAnswer()))
        {
            //whatever animation to be applied if answer is correct
            score++;
            if(ans=="A")
            {
                optionA.setBackground(getResources().getDrawable(R.drawable.correct_answer_background));
            }
            else if(ans=="B")
            {
                optionB.setBackground(getResources().getDrawable(R.drawable.correct_answer_background));
            }
            else if(ans=="C")
            {
                optionC.setBackground(getResources().getDrawable(R.drawable.correct_answer_background));
            }
            else if(ans=="D")
            {
                optionD.setBackground(getResources().getDrawable(R.drawable.correct_answer_background));
            }

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    setQuestionsAndOptions(++i);
                    optionA.setBackground(getResources().getDrawable(R.drawable.option_background));
                    optionB.setBackground(getResources().getDrawable(R.drawable.option_background));
                    optionC.setBackground(getResources().getDrawable(R.drawable.option_background));
                    optionD.setBackground(getResources().getDrawable(R.drawable.option_background));
                }
            },1000);
        }
        else{
            //whatever animation to be applied if animation is wrong
            if(ans=="A")
            {
                optionA.setBackground(getResources().getDrawable(R.drawable.wrong_answer_background));
            }
            else if(ans=="B")
            {
                optionB.setBackground(getResources().getDrawable(R.drawable.wrong_answer_background));
            }
            else if(ans=="C")
            {
                optionC.setBackground(getResources().getDrawable(R.drawable.wrong_answer_background));
            }
            else if(ans=="D")
            {
                optionD.setBackground(getResources().getDrawable(R.drawable.wrong_answer_background));
            }

            if(q_and_a.get(k).getAnswer().equals("A"))
            {
                optionA.setBackground(getResources().getDrawable(R.drawable.correct_answer_background));
            }
            else if(q_and_a.get(k).getAnswer().equals("B"))
            {
                optionB.setBackground(getResources().getDrawable(R.drawable.correct_answer_background));
            }
            else if(q_and_a.get(k).getAnswer().equals("C"))
            {
                optionC.setBackground(getResources().getDrawable(R.drawable.correct_answer_background));
            }
            else if(q_and_a.get(k).getAnswer().equals("D"))
            {
                optionD.setBackground(getResources().getDrawable(R.drawable.correct_answer_background));
            }

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    setQuestionsAndOptions(++i);
                    optionA.setBackground(getResources().getDrawable(R.drawable.option_background));
                    optionB.setBackground(getResources().getDrawable(R.drawable.option_background));
                    optionC.setBackground(getResources().getDrawable(R.drawable.option_background));
                    optionD.setBackground(getResources().getDrawable(R.drawable.option_background));
                }
            },2000);

        }
    }

    public void perk_one(int k)
    {
        //logic for perk one, i.e. perk 50-50
        perk_one_isclicked=true;
        char check='o';
        int p=2,a;
        String s;
        while (p>0)
        {
            Random R= new Random();
            a=65+R.nextInt(4);
            s=Character.toString((char)a);
            if (!s.equals(q_and_a.get(k).getAnswer()))
            {
                    if(s.equals("A") && check!='A')
                    {optionA.setVisibility(View.GONE);
                        check='A';}
                    else if (s.equals("B") && check!='B')
                    {optionB.setVisibility(View.GONE);
                        check='B';}
                    else if (s.equals("C") && check!='C')
                    {   optionC.setVisibility(View.GONE);
                        check='C';}
                    else if (s.equals("D") && check!='D')
                    {   optionD.setVisibility(View.GONE);
                        check='D';}
                    p--;
            }
        }
    }
    public void perk_two()
    {
        //logic for perk two,i.e. change question

        question.setText(q_and_a.get(7).getQuestion());
        optionA.setText(q_and_a.get(7).getA());
        optionB.setText(q_and_a.get(7).getB());
        optionC.setText(q_and_a.get(7).getC());
        optionD.setText(q_and_a.get(7).getD());

        perk_two_isclicked=true;
    }

   /* public void shrinkAnimation(TextView textView, int miliSec, float from, float to)
    {
        ScaleAnimation shrink =  new ScaleAnimation(from, to, from, to, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        shrink.setDuration(miliSec);
        shrink.setFillAfter(true);
        textView.startAnimation(shrink);
    }

    public void expandAnimation(TextView textView, int miliSec, float from, float to)
    {
        ScaleAnimation expand =  new ScaleAnimation(from, to, from, to, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        expand.setDuration(miliSec);
        expand.setFillAfter(true);
        textView.startAnimation(expand);
    }*/

}
