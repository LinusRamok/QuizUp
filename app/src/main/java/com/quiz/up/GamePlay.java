package com.quiz.up;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
    Boolean perk_one_isclicked=false,perk_two_isclicked=false,view_changed=false;
    Button optionAtv,optionBtv,optionCtv,optionDtv;
    TextView questiontv,question_notv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View dv=getWindow().getDecorView();
        int ui=View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        dv.setSystemUiVisibility(ui);
        setContentView(R.layout.activity_game_play);
        questiontv = (TextView)findViewById(R.id.question);
        question_notv=(TextView)findViewById(R.id.questionNumber);
        optionAtv =(Button)findViewById(R.id.optionA);
        optionAtv.setOnClickListener(this);
        optionBtv =(Button)findViewById(R.id.optionB);
        optionBtv.setOnClickListener(this);
        optionCtv =(Button)findViewById(R.id.optionC);
        optionCtv.setOnClickListener(this);
        optionDtv =(Button)findViewById(R.id.optionD);
        optionDtv.setOnClickListener(this);

        jsonString = getIntent().getStringExtra("jsonobj");
        obj=new Gson().fromJson(jsonString , QuestionsApi.class);
        q_and_a= obj.getQuestionlist();

        //setting the first question and options beforehand
        set_questions_and_options(0);

    }

    @Override
    public void onClick(View view) {

        Button tv=(Button)view;

            switch (tv.getId()) {
                case R.id.optionA:
                    set_answer_anim_and_score("A",i);
                    set_questions_and_options(++i);
                    break;
                case R.id.optionB:
                    set_answer_anim_and_score("B",i);
                    set_questions_and_options(++i);
                    break;
                case R.id.optionC:
                    set_answer_anim_and_score("C",i);
                    set_questions_and_options(++i);
                    break;
                case R.id.optionD:
                    set_answer_anim_and_score("D",i);
                    set_questions_and_options(++i);
                    break;
        }


    }

    public void set_questions_and_options(int k)
    {
        if(perk_one_isclicked==true && view_changed==false)
        {
            // making the textviews of options VISIBLE again which were set to gone in perk one
            view_changed=true;

            optionAtv.setVisibility(View.VISIBLE);
            optionBtv.setVisibility(View.VISIBLE);
            optionCtv.setVisibility(View.VISIBLE);
            optionDtv.setVisibility(View.VISIBLE);
        }
        if(k<7) {
            // logic for setting the question and options relative to the index passed

            question_notv.setText(k + 1 + "/7");
            questiontv.setText(q_and_a.get(k).getQuestion());
            optionAtv.setText(q_and_a.get(k).getA());
            optionBtv.setText(q_and_a.get(k).getB());
            optionCtv.setText(q_and_a.get(k).getC());
            optionDtv.setText(q_and_a.get(k).getD());
        }
        else{
            // logic to be applied after seven questions, i.e., passing score to the next activity, calling the next activity

            Toast.makeText(this, "Questions khatam ho gaye BC", Toast.LENGTH_LONG).show();
        }

    }
    public void set_answer_anim_and_score(String ans,int k)
    {
        if(k<7)
        if (ans.equals(q_and_a.get(k).getAnswer()))
        {
            //whatever animation to be applied if answer is correct

            Toast.makeText(this, "correct answer", Toast.LENGTH_SHORT).show();
            score++;
        }
        else{
            //whatever animation to be applied if animation is wrong

            Toast.makeText(this, "wrong answer", Toast.LENGTH_SHORT).show();
        }
    }
    public void perk_one(int k)
    {
        //logic for perk one, i.e. perk 50-50
        perk_one_isclicked=true;
        int p=2,a;
        String s;
        while (p>0)
        {
            Random R= new Random();
            a=65+R.nextInt(4);
            s=Integer.toString(a);
            if (!s.equals(q_and_a.get(k).getAnswer()))
            {
                    if(s.equals("A"))
                        optionAtv.setVisibility(View.GONE);
                    else if (s.equals("B"))
                        optionBtv.setVisibility(View.GONE);
                    else if (s.equals("C"))
                        optionCtv.setVisibility(View.GONE);
                    else if (s.equals("D"))
                        optionDtv.setVisibility(View.GONE);
                    p--;
            }
        }
    }
    public void perk_two()
    {
        //logic for perk two,i.e. change question

        questiontv.setText(q_and_a.get(8).getQuestion());
        optionAtv.setText(q_and_a.get(8).getA());
        optionBtv.setText(q_and_a.get(8).getB());
        optionCtv.setText(q_and_a.get(8).getC());
        optionDtv.setText(q_and_a.get(8).getD());

        perk_two_isclicked=true;
    }
}
