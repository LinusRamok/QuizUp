package com.quiz.up;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

import com.google.gson.Gson;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GamePlay extends AppCompatActivity implements View.OnClickListener {
    String jsonString;
    QuestionsApi obj;
    BufferedReader reader;
    List<Questionlist> q_and_a=new ArrayList<>();
    int i=0;
    int qadetails[]=new int[9],changedqno=10;
    //qa details is an array which passes the details of attempted questions and their answers given by user to the next activity via intent
    //0 is for wrong answer
    //1 is for right answer
    //9 is for unattempted question or question changed by using perk two
    //3 is for that 50-50perk is used
    TextView optionA, optionB, optionC, optionD, perk1, perk2;
    boolean perk_one_isclickable=true, perk_one_isCliked=false, perk_two_isClicked =false, perk_two_isclickable=true;
    boolean optionA_isClickable=true, optionB_isClickable=true, optionC_isClickable=true, optionD_isClickable=true;
    TextView question, questionNumber, timerTextView;
    Handler timer = new Handler();
    boolean isTimerStarted=false;
    double secondsPassed = 0.0;

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
        timerTextView =(TextView)findViewById(R.id.timer);


        Typeface ourBoldFont = Typeface.createFromAsset(getAssets(), "fonts/primebold.otf");
        Typeface ourLightFont = Typeface.createFromAsset(getAssets(), "fonts/primelight.otf");

        question.setTypeface(ourBoldFont);
        questionNumber.setTypeface(ourLightFont);
        optionA.setTypeface(ourLightFont);
        optionB.setTypeface(ourLightFont);
        optionC.setTypeface(ourLightFont);
        optionD.setTypeface(ourLightFont);
        perk1.setTypeface(ourLightFont);
        perk2.setTypeface(ourLightFont);
        timerTextView.setTypeface(ourBoldFont);

        jsonString = getIntent().getStringExtra("jsonobj");
        obj=new Gson().fromJson(jsonString , QuestionsApi.class);
        q_and_a= obj.getQuestionlist();

        qadetails[7]=9;
        qadetails[8]=4;
        //setting the first question and options beforehand
        setQuestionsAndOptions(0);
        startTimer();

    }

    @Override
    public void onClick(View view) {

        TextView tv=(TextView)view;

            switch (tv.getId()) {
                case R.id.optionA:
                    if (optionA_isClickable)
                    optionsAfterClickedMethod("A",i);
                    break;
                case R.id.optionB:
                    if (optionB_isClickable)
                    optionsAfterClickedMethod("B",i);
                    break;
                case R.id.optionC:
                    if (optionC_isClickable)
                    optionsAfterClickedMethod("C",i);
                    break;
                case R.id.optionD:
                    if (optionD_isClickable)
                    optionsAfterClickedMethod("D",i);
                    break;
                case R.id.perk1:
                    if(perk_one_isclickable)
                    perk_one(i);
                    break;
                case R.id.perk2:
                    if(perk_two_isclickable)
                    perk_two(i);
                    break;
        }
    }

    public void set_visibility_after_perk_one()
    {
            // making the textviews of options VISIBLE again which were set to gone in perk one

        if(!optionA_isClickable)
        {optionA.setVisibility(View.VISIBLE);
            expandAnimation(optionA,250,0,1f);}
        if(!optionB_isClickable)
        {optionB.setVisibility(View.VISIBLE);
            expandAnimation(optionB,250,0,1f);}
        if(!optionC_isClickable)
        {optionC.setVisibility(View.VISIBLE);
            expandAnimation(optionC,250,0,1f);}
        if(!optionD_isClickable)
        {optionD.setVisibility(View.VISIBLE);
            expandAnimation(optionD,250,0,1f);}

            setAllOptionsClickable(true);

    }
    public void setAllOptionsClickable(Boolean q)    //to set all options clickable true or false
    {
        optionA_isClickable=q;
        optionB_isClickable=q;
        optionC_isClickable=q;
        optionD_isClickable=q;
    }

    public void setQuestionsAndOptions (int k)
    {
        set_visibility_after_perk_one();

        if(k<7) {
            // logic for setting the question and options relative to the index passed

            questionNumber.setText(k + 1 + "/7");
            question.setText(q_and_a.get(k).getQuestion());
            optionA.setText(q_and_a.get(k).getA());
            optionB.setText(q_and_a.get(k).getB());
            optionC.setText(q_and_a.get(k).getC());
            optionD.setText(q_and_a.get(k).getD());
        }

    }
    public void optionsAfterClickedMethod(final String ans, final int k)
    {
        if(k<7)
        if (ans.equals(q_and_a.get(k).getAnswer()))
        {
            //whatever animation to be applied if answer is correct
            qadetails[k]=1;
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
            setAllOptionsClickable(false);

            if(!perk_one_isCliked)
            {
                perk_one_isclickable=false;
            }
            if(!perk_two_isClicked)
            {
                perk_two_isclickable=false;
            }
            if ((++i)<7) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setQuestionsAndOptions(i);
                        optionA.setBackground(getResources().getDrawable(R.drawable.option_background));
                        optionB.setBackground(getResources().getDrawable(R.drawable.option_background));
                        optionC.setBackground(getResources().getDrawable(R.drawable.option_background));
                        optionD.setBackground(getResources().getDrawable(R.drawable.option_background));
                        setAllOptionsClickable(true);
                        if (!perk_one_isCliked) {
                            perk_one_isclickable = true;
                        }
                        if (!perk_two_isClicked) {
                            perk_two_isclickable = true;
                        }
                    }
                }, 1500);
            }
            else{
            stopTimer();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(changedqno!=10)
                    {
                        int temp=qadetails[changedqno];
                        qadetails[changedqno]=qadetails[7];
                        qadetails[7]=temp;
                    }
                    Intent i=new Intent(GamePlay.this,ScoreBoard.class);
                    i.putExtra("QuestionAndAnswers",jsonString);
                    i.putExtra("userAnswers",qadetails);
                    startActivity(i);
                }
            }, 1500);
        }}

        else {
            //whatever animation to be applied if animation is wrong
            qadetails[k]=0;
            if (ans == "A") {
                optionA.setBackground(getResources().getDrawable(R.drawable.wrong_answer_background));
            } else if (ans == "B") {
                optionB.setBackground(getResources().getDrawable(R.drawable.wrong_answer_background));
            } else if (ans == "C") {
                optionC.setBackground(getResources().getDrawable(R.drawable.wrong_answer_background));
            } else if (ans == "D") {
                optionD.setBackground(getResources().getDrawable(R.drawable.wrong_answer_background));
            }


            if (q_and_a.get(k).getAnswer().contains("A")) {
                optionA.setBackground(getResources().getDrawable(R.drawable.correct_answer_background));
            } else if (q_and_a.get(k).getAnswer().contains("B")) {
                optionB.setBackground(getResources().getDrawable(R.drawable.correct_answer_background));
            } else if (q_and_a.get(k).getAnswer().contains("C")) {
                optionC.setBackground(getResources().getDrawable(R.drawable.correct_answer_background));
            } else if (q_and_a.get(k).getAnswer().contains("D")) {
                optionD.setBackground(getResources().getDrawable(R.drawable.correct_answer_background));
            }
            setAllOptionsClickable(false);
            if (!perk_one_isCliked) {
                perk_one_isclickable = false;
            }
            if (!perk_two_isClicked) {
                perk_two_isclickable = false;
            }
            if ((++i) < 7) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setQuestionsAndOptions(i);
                        optionA.setBackground(getResources().getDrawable(R.drawable.option_background));
                        optionB.setBackground(getResources().getDrawable(R.drawable.option_background));
                        optionC.setBackground(getResources().getDrawable(R.drawable.option_background));
                        optionD.setBackground(getResources().getDrawable(R.drawable.option_background));
                        setAllOptionsClickable(true);
                        if (!perk_one_isCliked) {
                            perk_one_isclickable = true;
                        }
                        if (!perk_two_isClicked) {
                            perk_two_isclickable = true;
                        }
                    }
                }, 1500);
            } else{
                stopTimer();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(changedqno!=10)
                    {
                        int temp=qadetails[changedqno];
                        qadetails[changedqno]=qadetails[7];
                        qadetails[7]=temp;
                    }
                    Intent i=new Intent(GamePlay.this,ScoreBoard.class);
                    i.putExtra("QuestionAndAnswers",jsonString);
                    i.putExtra("userAnswers",qadetails);
                    startActivity(i);
                }
            }, 1500);
        }
    }}

    public void perk_one(int k)
    {
        //logic for perk one, i.e. perk 50-50
        perk_one_isclickable=false;
        perk_one_isCliked=true;
        qadetails[8]=3;
        setDisabledBackground(perk1);
        char check='o';
        int p=2,a;
        String s;
        while (p>0)
        {
            Random R= new Random();
            a=65+R.nextInt(4);
            s=Character.toString((char)a);
            if (!s.contains(q_and_a.get(k).getAnswer()))
            {
                    if (s.contains("A") && check!='A')
                    {   shrinkAnimation(optionA,250,1f,0);
                        optionA_isClickable=false;
                        optionA.setVisibility(View.INVISIBLE);
                        check='A';
                        p--;}
                    else if (s.contains("B") && check!='B')
                    {   shrinkAnimation(optionB,250,1f,0);
                        optionB.setVisibility(View.INVISIBLE);
                        optionB_isClickable=false;
                        check='B';
                        p--;}
                    else if (s.contains("C") && check!='C')
                    {   shrinkAnimation(optionC,250,1f,0);
                        optionC.setVisibility(View.INVISIBLE);
                        optionC_isClickable=false;
                        check='C';
                        p--;}
                    else if (s.contains("D") && check!='D')
                    {   shrinkAnimation(optionD,250,1f,0);
                        optionD.setVisibility(View.INVISIBLE);
                        optionD_isClickable=false;
                        check='D';
                        p--;}

            }
        }
    }
    public void shrinkAllOptions()
    {
        shrinkAnimation(optionA,250,1f,0);
        shrinkAnimation(optionB,250,1f,0);
        shrinkAnimation(optionC,250,1f,0);
        shrinkAnimation(optionD,250,1f,0);
    }
    public void perk_two(int k)
    {
        //logic for perk two,i.e. change question
            changedqno=k;
        set_visibility_after_perk_one();

        question.setText(q_and_a.get(7).getQuestion());
        optionA.setText(q_and_a.get(7).getA());
        optionB.setText(q_and_a.get(7).getB());
        optionC.setText(q_and_a.get(7).getC());
        optionD.setText(q_and_a.get(7).getD());
        perk_two_isClicked =true;
        perk_two_isclickable=false;
        setDisabledBackground(perk2);
    }

    public void shrinkAnimation(TextView textView, int miliSec, float from, float to)
    {
        //a method to shrink textView in first parameter
        //miliSec parameter is the time in mili seconds for the animation to complete
        //from and to parameters are starting and ending zoomed states of textView
        ScaleAnimation shrink =  new ScaleAnimation(from, to, from, to, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        shrink.setDuration(miliSec);
        shrink.setFillAfter(true);
        textView.startAnimation(shrink);
    }

    public void expandAnimation(TextView textView, int miliSec, float from, float to)
    {
        //a method to shrink textView in first parameter
        //miliSec parameter is the time in mili seconds for the animation to complete
        //from and to parameters are starting and ending zoomed states of textView
        ScaleAnimation expand =  new ScaleAnimation(from, to, from, to, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        expand.setDuration(miliSec);
        expand.setFillAfter(true);
        textView.startAnimation(expand);
    }

    public void setDisabledBackground(TextView textView)
    {
        //this method is used to give the parameter textView a background without any focus, to show that the textView is not clickable any further

        if (textView == optionA || textView == optionB ||textView == optionC ||textView == optionD)
        {
            textView.setBackground(getResources().getDrawable(R.drawable.option_background_disabled));
        }

        else if (textView == perk1)
        {
            textView.setBackground(getResources().getDrawable(R.drawable.perk_left_background_disabled));
            textView.setAlpha(0.4f);
        }

        else if (textView == perk2)
        {
            textView.setBackground(getResources().getDrawable(R.drawable.perk_right_background_disabled));
            textView.setAlpha(0.4f);
        }

    }

    public void setEnabledBackground(TextView textView)
    {
        //this method is used to give the parameter textView a background with focus effects, to show that the textView is clickable now.
        //this should be used on a TextView only when setDisabledBackground had been applied to the same TextView

        if (textView == optionA || textView == optionB ||textView == optionC ||textView == optionD)
        {

            if (textView == optionA || textView == optionB ||textView == optionC ||textView == optionD)
            {
                textView.setBackground(getResources().getDrawable(R.drawable.option_background));
            }

            else if (textView == perk1)
            {
                textView.setBackground(getResources().getDrawable(R.drawable.perk_left_background));
                textView.setAlpha(1f);
            }

            else if (textView == perk2)
            {
                textView.setBackground(getResources().getDrawable(R.drawable.perk_right_background_disabled));
                textView.setAlpha(1f);
            }

        }
    }

    public Runnable ute=new Runnable() {                //This runnable is used to run a timer and display it in TextView named timerTextView
        @Override
        public void run() {
            long currentMilliseconds = System.currentTimeMillis();
            secondsPassed=secondsPassed+0.1;
            double temp=roundingOfDouble(secondsPassed,1);

            timerTextView.setText(Double.toString(temp));
            timer.postAtTime(this, currentMilliseconds);
            timer.postDelayed(ute, 100);
        }
    };

    public void startTimer() {
        //a method to start timer
        if (isTimerStarted==false)
        {
            timer.removeCallbacks(ute);
            timer.postDelayed(ute, 100);
            isTimerStarted=true;
        }

    }

    public void stopTimer() {
        //a method to stop timer
        timer.removeCallbacks(ute);
        isTimerStarted=false;
    }

    public static double roundingOfDouble(double value, int places) {
        //this method is used to round of a double 'value'(parameter one) to some specified decimal 'places'(parameter two)
        //without the use of this method, the double value will have many numbers after the decimal point
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

}
