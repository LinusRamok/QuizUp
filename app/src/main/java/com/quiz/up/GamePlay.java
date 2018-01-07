package com.quiz.up;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Button;
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
    List<Questionlist> q_and_a=new ArrayList<>();
    int i=0;
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
        set_Q_and_a_view(0);

    }

    @Override
    public void onClick(View view) {

        Button tv=(Button)view;

        switch (tv.getId()) {
            case R.id.optionA:
                i++;
                set_Q_and_a_view(i);
                break;
            case R.id.optionB:
                i++;
                set_Q_and_a_view(i);
                break;
            case R.id.optionC:
                i++;
                set_Q_and_a_view(i);
                break;
            case R.id.optionD:
                i++;
                set_Q_and_a_view(i);
                break;
        }

    }

    public void set_Q_and_a_view(int k)
    {
        question_notv.setText(k+1+"/7");
        questiontv.setText(q_and_a.get(k).getQuestion());
        optionAtv.setText(q_and_a.get(k).getA());
        optionBtv.setText(q_and_a.get(k).getB());
        optionCtv.setText(q_and_a.get(k).getC());
        optionDtv.setText(q_and_a.get(k).getD());
    }
}
