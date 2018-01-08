package com.quiz.up;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

public class GamePlay extends AppCompatActivity implements View.OnClickListener {
    String jsonString;
    JSONObject jsonObject=null;
    QuestionsApi obj;
    BufferedReader reader;
    int score=0;
    List<Questionlist> q_and_a=new ArrayList<>();
    int i=0;
    TextView optionAtv,optionBtv,optionCtv,optionDtv;
    TextView questiontv,question_notv;

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
        questiontv = (TextView)findViewById(R.id.question);
        question_notv=(TextView)findViewById(R.id.questionNumber);
        optionAtv =(TextView)findViewById(R.id.optionA);
        optionAtv.setOnClickListener(this);
        optionBtv =(TextView)findViewById(R.id.optionB);
        optionBtv.setOnClickListener(this);
        optionCtv =(TextView)findViewById(R.id.optionC);
        optionCtv.setOnClickListener(this);
        optionDtv =(TextView)findViewById(R.id.optionD);
        optionDtv.setOnClickListener(this);

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
                    setQuestionsAndOptions(++i);
                    break;
                case R.id.optionB:
                    set_answer_anim_and_score("B",i);
                    setQuestionsAndOptions(++i);
                    break;
                case R.id.optionC:
                    set_answer_anim_and_score("C",i);
                    setQuestionsAndOptions(++i);
                    break;
                case R.id.optionD:
                    set_answer_anim_and_score("D",i);
                    setQuestionsAndOptions(++i);
                    break;
        }


    }

    public void setQuestionsAndOptions (int k)
    {
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


}
