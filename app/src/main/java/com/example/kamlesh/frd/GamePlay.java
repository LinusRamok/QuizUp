package com.example.kamlesh.frd;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.kamlesh.frd.Models.Questionlist;
import com.example.kamlesh.frd.Models.QuestionsApi;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GamePlay extends AppCompatActivity implements View.OnClickListener {
    String top,url;
    String jsonString;
    QuestionsApi obj;
    BufferedReader reader;
    List<Questionlist> q_and_a=new ArrayList<>();
    int i=0;
    int scoreArray[]=new int[7];
    int qadetails[]=new int[7];
    int qnoForPerk[]=new int[2];
    int finalscore=0;
    //qa details is an array which passes the details of attempted questions and their answers given by user to the next activity via intent
    //0 is for wrong answer
    //1 is for right answer
    //9 is for unattempted question or question changed by using perk two
    //10 is for that 50-50perk is not used otherwise qadetails[8] shows the no. of que in which this perk was used
    TextView optionA, optionB, optionC, optionD, perk1, perk2;
    boolean perk_one_isclickable=true, perk_one_isCliked=false, perk_two_isClicked =false, perk_two_isclickable=true;
    boolean optionA_isClickable=true, optionB_isClickable=true, optionC_isClickable=true, optionD_isClickable=true;
    TextView question, questionNumber, secondsText, score, scoreText, timerText, changeInScore;
    Button quitGame;
    boolean isDialogOpen=false;
    Handler timer = new Handler();
    boolean isTimerStarted=false;
    double secondsPassed = 0.0;
    double timeForEachQues[] = new double[7], totalTimeTaken;
    double beginVar = 0.0, endVar;  int tempVar=0; //these two variables are only necessary for method calculateTimeForEachQues()
    int sumScoreInitial=0, sumScoreFinal=0; // these two variables are only necessary for method displayScore()
    Button dots[] = new Button[7];
    View lines[] = new View[6];

    @Override
    protected void onResume() {
        super.onResume();

        View dv=getWindow().getDecorView();
        int ui= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        dv.setSystemUiVisibility(ui);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            View decorView= getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    @Override
    public void onBackPressed() {
        if (isDialogOpen == false)
            quitGameDialogMethod();
        else
            super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View dv=getWindow().getDecorView();
        int ui= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        dv.setSystemUiVisibility(ui);


        //getintent Topic name and url
        top=getIntent().getExtras().getString("topic_name");
        url=getIntent().getExtras().getString("topic_url");

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
        secondsText =(TextView)findViewById(R.id.timer);
        timerText = (TextView) findViewById(R.id.timerText);
        score = (TextView)findViewById(R.id.score);
        scoreText = (TextView)findViewById(R.id.scoreText);
        quitGame = (Button)findViewById(R.id.quitGame);
        quitGame.setOnClickListener(this);
        changeInScore = (TextView)findViewById(R.id.difference);

        for(int y=0; y<7; y++)
        {
            String buttonID = "dot" + y;
            int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
            dots[y] = ((Button) findViewById(resID));
        }
        for(int y=0; y<6; y++)
        {
            String viewID = "line" + y;
            int resID = getResources().getIdentifier(viewID, "id", getPackageName());
            lines[y] = ((View) findViewById(resID));
        }

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
        secondsText.setTypeface(ourLightFont);
        score.setTypeface(ourLightFont);
        timerText.setTypeface(ourBoldFont);
        scoreText.setTypeface(ourBoldFont);
        changeInScore.setTypeface(ourLightFont);


        jsonString = getIntent().getStringExtra("jsonobj");
        obj=new Gson().fromJson(jsonString , QuestionsApi.class);
        q_and_a= obj.getQuestionlist();
        qnoForPerk[0]=9;
        qnoForPerk[1]=9;
        for(int x=0; x<7; x++)
        {
            scoreArray[x]=0;
            qadetails[x]=0;
        }
        //setting the first question and options beforehand
        setQuestionsAndOptions(0);
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
                {
                    perk_one(i);
                    calculateScore(i, "Perk Clicked");
                    calculateQnoForPerks(i, "Perk One");
                    setDisabledBackground(perk1);
                    dotsAndLinesSetup("Perk Clicked");
                }
                break;
            case R.id.perk2:
                if(perk_two_isclickable)
                {
                    perk_two(i);
                    calculateScore(i, "Perk Clicked");
                    calculateQnoForPerks(i, "Perk Two");
                    setDisabledBackground(perk2);
                    dotsAndLinesSetup("Perk Clicked");
                }
                break;
            case R.id.quitGame:
                quitGameDialogMethod();
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
            startTimer();
        }

    }

    public void optionsAfterClickedMethod(final String ans, final int k)
    {
        stopTimer();
        if(isTimerStarted==false && secondsPassed != 0.0) {
            double currentStoppedTime = Double.parseDouble(secondsText.getText().toString());
            calculateTimeForEachQues(currentStoppedTime);
        }


        if(k<7)
            if (ans.equals(q_and_a.get(k).getAnswer()))
            {
                //whatever animation to be applied if answer is correct
                calculateScore(i, "Correct Answer");
                dotsAndLinesSetup("Correct Answer");
                checkForCorrectAnswer(i, "Correct Answer");

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
                else
                {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finalscore= scoreArray[0]+scoreArray[1]+scoreArray[2]+scoreArray[3]+scoreArray[4]+scoreArray[5]+scoreArray[6];
                            if(finalscore<0)
                                finalscore=0;
                            Intent i=new Intent(GamePlay.this,ScorePageActivity.class);
                            i.putExtra("QuestionAndAnswers",jsonString);
                            i.putExtra("userAnswers",qadetails);
                            i.putExtra("TimerValues",timeForEachQues);
                            i.putExtra("scoreArray",scoreArray);
                            i.putExtra("Score",finalscore);
                            i.putExtra("perkvalues",qnoForPerk);
                            //intent Topic name/
                            i.putExtra("topic_name",top);
                            i.putExtra("topic_url",url);
                            System.out.println("the final score is :"+finalscore);
                            System.out.println("the qadetails array is:"+qadetails[0]+" "+qadetails[1]+" "+qadetails[2]+" "+qadetails[3]+" "+qadetails[4]+" "+qadetails[5]+" "+qadetails[6]+" ");
                            System.out.println("the score Array is :"+scoreArray[0]+" "+scoreArray[1]+" "+scoreArray[2]+" "+scoreArray[3]+" "+scoreArray[4]+" "+scoreArray[5]+" "+scoreArray[6]+" ");
                            System.out.println("the timer array is :" +timeForEachQues[0]+" "+timeForEachQues[1]+" "+timeForEachQues[2]+" "+timeForEachQues[3]+" "+timeForEachQues[4]+" "+timeForEachQues[5]+" "+timeForEachQues[6]+" ");
                            startActivity(i);
                        }
                    }, 1500);
                }}

            else
            {
                //whatever animation to be applied if animation is wrong
                checkForCorrectAnswer(i, "Wrong Answer");
                dotsAndLinesSetup("Wrong Answer");

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
                } else
                {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            finalscore= scoreArray[0]+scoreArray[1]+scoreArray[2]+scoreArray[3]+scoreArray[4]+scoreArray[5]+scoreArray[6];
                            if(finalscore<0)
                                    finalscore=0;
                            Intent i=new Intent(GamePlay.this,ScorePageActivity.class);
                            i.putExtra("QuestionAndAnswers",jsonString);
                            i.putExtra("userAnswers",qadetails);
                            i.putExtra("TimerValues",timeForEachQues);
                            i.putExtra("scoreArray",scoreArray);
                            i.putExtra("Score",finalscore);
                            i.putExtra("perkvalues",qnoForPerk);
                            //Intent Topic name
                            i.putExtra("topic_name",top);
                            i.putExtra("topic_url",url);
                            System.out.println("the final score is :"+finalscore);
                            System.out.println("the qadetails array is:"+qadetails[0]+" "+qadetails[1]+" "+qadetails[2]+" "+qadetails[3]+" "+qadetails[4]+" "+qadetails[5]+" "+qadetails[6]+" ");
                            System.out.println("the score Array is :"+scoreArray[0]+" "+scoreArray[1]+" "+scoreArray[2]+" "+scoreArray[3]+" "+scoreArray[4]+" "+scoreArray[5]+" "+scoreArray[6]+" ");
                            System.out.println("the timer array is :" +timeForEachQues[0]+" "+timeForEachQues[1]+" "+timeForEachQues[2]+" "+timeForEachQues[3]+" "+timeForEachQues[4]+" "+timeForEachQues[5]+" "+timeForEachQues[6]+" ");
                            startActivity(i);
                        }
                    }, 1500);
                }
            }
    }

    public void perk_one(int k)
    {
        //logic for perk one, i.e. perk 50-50
        perk_one_isclickable=false;
        perk_one_isCliked=true;
        char check='o';
        int p=2,a;
        String s;
        while (p>0)
        {
            Random R= new Random();
            a=65+R.nextInt(4);
            s= Character.toString((char)a);
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

    public void perk_two(int k)
    {
        //logic for perk two,i.e. change question
        set_visibility_after_perk_one();
        question.setText(q_and_a.get(7).getQuestion());
        optionA.setText(q_and_a.get(7).getA());
        optionB.setText(q_and_a.get(7).getB());
        optionC.setText(q_and_a.get(7).getC());
        optionD.setText(q_and_a.get(7).getD());
        perk_two_isClicked =true;
        perk_two_isclickable=false;

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
            textView.setTextColor(getResources().getColor(R.color.textColorPrimary));
        }

        else if (textView == perk2)
        {
            textView.setBackground(getResources().getDrawable(R.drawable.perk_right_background_disabled));
            textView.setAlpha(0.4f);
            textView.setTextColor(getResources().getColor(R.color.textColorPrimary));
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

    public Runnable ute=new Runnable() {                //This runnable is used to run a timer and display it in TextView named secondsText
        @Override
        public void run() {
            //the actual implementation of timer using runnable
            long currentMilliseconds = System.currentTimeMillis();
            secondsPassed=secondsPassed+0.1;
            double temp=roundingOfDouble(secondsPassed,1);

            secondsText.setText(Double.toString(temp));
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

    public double roundingOfDouble(double value, int places) {
        //this method is used to round of a double 'value'(parameter one) to some specified decimal 'places'(parameter two)
        //without the use of this method, the double value will have many numbers after the decimal point
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public void calculateTimeForEachQues(double currentStoppedTime) {
        //This function calculates the time taken to complete each question(first 'if' condition),
        //as well as the time taken to complete the whole game(secon if condition)

        if (tempVar<7)
        {
            endVar=roundingOfDouble(currentStoppedTime,1);
            System.out.println("endVar="+endVar);

            timeForEachQues[tempVar]=roundingOfDouble(endVar - beginVar,1);
            System.out.println("timeForEachQues["+tempVar+"]="+timeForEachQues[tempVar]);

            beginVar =roundingOfDouble(currentStoppedTime,1);
            System.out.println("beginVar="+ beginVar);

            ++tempVar;
        }

        else if(tempVar == 7)
        {
            totalTimeTaken = currentStoppedTime;
        }
    }

    public void quitGameDialogMethod()
    {
        TextView boxTitle, alertMessage, positiveButton, negativeButton;

        final Dialog quitDialog = new Dialog(this);
        quitDialog.setContentView(R.layout.alertdialog_yes_no);
        quitDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));  //to make background of dialog transparent, hence allowing curved borders to be visible
        quitDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE); //to make dialog not focussible, so that immersive mode can persist
        quitDialog.setCanceledOnTouchOutside(false);

        boxTitle = quitDialog.findViewById(R.id.boxTitle);
        alertMessage = quitDialog.findViewById(R.id.alertMessage);
        positiveButton = quitDialog.findViewById(R.id.positiveButton);
        negativeButton = quitDialog.findViewById(R.id.negativeButton);

        boxTitle.setText("Quit");
        alertMessage.setText("Are you sure you want to quit the game? All the progress of your current game will be lost.");
        positiveButton.setText("Keep Playing");
        negativeButton.setText("Quit Game");

        Typeface ourBoldFont = Typeface.createFromAsset(getAssets(), "fonts/primebold.otf");
        Typeface ourLightFont = Typeface.createFromAsset(getAssets(), "fonts/primelight.otf");
        boxTitle.setTypeface(ourBoldFont);
        alertMessage.setTypeface(ourLightFont);
        positiveButton.setTypeface(ourBoldFont);
        negativeButton.setTypeface(ourBoldFont);

        quitDialog.show();
        quitDialog.getWindow().getDecorView().setSystemUiVisibility(this.getWindow().getDecorView().getSystemUiVisibility());
        quitDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE); //clear not focussible flags
        isDialogOpen = true;
        System.out.println("Dialog is opened");

        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isDialogOpen = false;
                quitDialog.dismiss();
                System.out.println("Dialog is closed");
            }
        });
        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isDialogOpen = false;
                quitDialog.dismiss();
                System.out.println("Dialog is closed");
                finish();
            }
        });

        quitDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                System.out.println("Dialog is closed");
                isDialogOpen=false;
                quitDialog.dismiss();
            }
        });
    }

    public void calculateScore(int currentQuesNum, String onAction)
    {
        //currentQuesNum values lies between 0 to maxQuestion-1. it tells the current question number user is playing.
        //onAction tells at what action this function is being called. It values can be "Perk Clicked", "Correct Answer", "Wrong Answer" (if negative marks are to be calulated)
        switch (onAction)
        {
            case "Correct Answer" :
                scoreArray[currentQuesNum]+=20;
                if(timeForEachQues[currentQuesNum]>5)
                {
                    Long L=Math.round(scoreArray[currentQuesNum]-((timeForEachQues[currentQuesNum]-5)*0.6));
                    scoreArray[currentQuesNum]=Integer.valueOf(L.intValue());
                }
                if(scoreArray[currentQuesNum]<3)
                {
                    scoreArray[currentQuesNum]=5;
                }
                System.out.println("Score of "+currentQuesNum+ "is :"+scoreArray[currentQuesNum] );
                displayScore(currentQuesNum);
                break;

            case "Perk Clicked" :
                scoreArray[currentQuesNum]-=2;
                System.out.println("Score of "+currentQuesNum+ "is :"+scoreArray[currentQuesNum] );
                displayScore(currentQuesNum);
                break;

            default:
                System.out.println("Wrong choice");
                break;
        }

    }

    public void checkForCorrectAnswer(int currentQuesNum, String onAction)
    {
        //currentQuesNum values lies between 0 to maxQuestion-1. it tells the current question number user is playing.
        //onAction tells at what action this function is being called. It values can be "Correct Answer", "Wrong Answer" (if negative marks are to be calulated)
        switch (onAction)
        {
            case "Correct Answer" :
                qadetails[currentQuesNum]=1;
                System.out.println("qadetails of "+currentQuesNum+ "is :"+qadetails[currentQuesNum] );
                break;

            case "Wrong Answer" :
                qadetails[currentQuesNum]=0;
                System.out.println("qadetails of "+currentQuesNum+ "is :"+qadetails[currentQuesNum] );
                break;

            default:
                System.out.println("Wrong choice");
                break;
        }
    }

    public void calculateQnoForPerks(int currentQuesNum, String onAction)
    {
        //currentQuesNum values lies between 0 and 1. it tells the current question number user is playing.
        //onAction tells which perk is used. It's values can be "Perk One" or "Perk Two"
        switch (onAction)
        {
            case "Perk One" :
                qnoForPerk[0]=currentQuesNum;
                System.out.println("Perk one is used in the position "+qnoForPerk[0] );
                break;

            case "Perk Two" :
                qnoForPerk[1]=currentQuesNum;
                System.out.println("Perk two is used in the position "+qnoForPerk[1] );
                break;

            default:
                System.out.println("Wrong choice");
                break;
        }
    }

    public void displayScore(int currentQuesNo)
    {
        sumScoreFinal= scoreArray[0]+scoreArray[1]+scoreArray[2]+scoreArray[3]+scoreArray[4]+scoreArray[5]+scoreArray[6];
        animateTextViewValue(sumScoreInitial, sumScoreFinal, 500, score);

        int diff = sumScoreFinal-sumScoreInitial;
        if(diff>0) {
            String s1="+";
            String s2=String.valueOf(diff);
            s1=s1.concat(s2);
            changeInScore.setText(s1);
            changeInScore.setTextColor(getResources().getColor(R.color.correctAnswer));
        }
        else {
            changeInScore.setText(String.valueOf(diff));
            changeInScore.setTextColor(getResources().getColor(R.color.wrongAnswer));
        }
        changeInScore.setVisibility(View.VISIBLE);
        YoYo.with(Techniques.FadeOutRight).duration(1500).playOn(changeInScore);
        sumScoreInitial=sumScoreFinal;

        if(currentQuesNo>6 && sumScoreFinal<0) {
            animateTextViewValue(sumScoreFinal,0,500, score);
        }
    }

    public void animateTextViewValue(int initialValue, int finalValue, int duration, final TextView textView)
    {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(initialValue,finalValue);
        valueAnimator.setDuration(duration);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                textView.setText(valueAnimator.getAnimatedValue().toString());
            }
        });
        valueAnimator.start();
        animateTextViewColor(finalValue,duration,textView);
    }

    public void animateTextViewColor(int value, int duration, final TextView textView)
    {
        int[] colors = getResources().getIntArray(R.array.scoreColor);
        int colorFrom = score.getCurrentTextColor();
        int colorTo = colors[0];

        if(value<=10)
            colorTo = colors[0];
        else if (value>10 && value<=20)
            colorTo = colors[1];
        else if (value>20 && value<=30)
            colorTo = colors[2];
        else if (value>30 && value<=40)
            colorTo = colors[3];
        else if (value>40 && value<=50)
            colorTo = colors[4];
        else if (value>50 && value<=60)
            colorTo = colors[5];
        else if (value>60 && value<=70)
            colorTo = colors[6];
        else if (value>70 && value<=80)
            colorTo = colors[7];
        else if (value>80 && value<=90)
            colorTo = colors[8];
        else if (value>90 && value<=100)
            colorTo = colors[9];
        else if (value>100 && value<=110)
            colorTo = colors[10];
        else if (value>110 && value<=120)
            colorTo = colors[11];
        else if (value>120 && value<=130)
            colorTo = colors[12];
        else if (value>130 && value<=140)
            colorTo = colors[13];

        ValueAnimator valueAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                textView.setTextColor((Integer)animator.getAnimatedValue());
            }
        });
        valueAnimator.start();
    }

    public void dotsAndLinesSetup(String str)
    {
        switch (str)
        {
            case "Perk Clicked":
                buttonDrawableTransition(dots[i], getResources().getDrawable(R.drawable.dots_focussed_perk));
                break;
            case "Correct Answer":
                if(i==qnoForPerk[0] || i==qnoForPerk[1]){
                    buttonDrawableTransition(dots[i], getResources().getDrawable(R.drawable.dots_correct_perk));
                }
                else {
                    buttonDrawableTransition(dots[i], getResources().getDrawable(R.drawable.dots_correct_background));
                }
                if(i<6) {
                    lines[i].setVisibility(View.VISIBLE);
                    YoYo.with(Techniques.FadeIn).duration(1500).playOn(lines[i]);
                    final int z=i+1;
                    Handler handler =new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            buttonDrawableTransition(dots[z], getResources().getDrawable(R.drawable.dots_focussed_background));
                        }
                    },1500);
                }
                break;
            case "Wrong Answer":
                if(i==qnoForPerk[0] || i==qnoForPerk[1]){
                    buttonDrawableTransition(dots[i], getResources().getDrawable(R.drawable.dots_wrong_perk));
                }
                else {
                    buttonDrawableTransition(dots[i], getResources().getDrawable(R.drawable.dots_wrong_background));
                }
                if(i<6) {
                    lines[i].setVisibility(View.VISIBLE);
                    YoYo.with(Techniques.FadeIn).duration(1500).playOn(lines[i]);
                    final int z=i+1;
                    Handler handler =new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            buttonDrawableTransition(dots[z], getResources().getDrawable(R.drawable.dots_focussed_background));
                        }
                    },1500);
                }
                break;
            default:
                System.out.println("Wrong choice");
        }

    }

    public void buttonDrawableTransition(Button button, Drawable drawable)
    {
        Drawable backgrounds[]=new Drawable[2];
        backgrounds[0] = button.getBackground();
        backgrounds[1] = drawable;

        TransitionDrawable transitionDrawable = new TransitionDrawable(backgrounds);
        button.setBackground(transitionDrawable);
        transitionDrawable.setCrossFadeEnabled(true);
        transitionDrawable.startTransition(250);
    }
}