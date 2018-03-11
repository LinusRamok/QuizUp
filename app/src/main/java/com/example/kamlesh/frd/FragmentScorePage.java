package com.example.kamlesh.frd;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.customtabs.CustomTabsService;
import android.support.v4.app.Fragment;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.easing.linear.Linear;
import com.example.kamlesh.frd.CircularProgressBar.CircularProgressBar;
import com.example.kamlesh.frd.Models.Topic;
import com.example.kamlesh.frd.ScorePagePOJO.PlayerScore;
import com.github.lzyzsd.circleprogress.DonutProgress;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public class FragmentScorePage extends Fragment {

    // Store instance variables
    private int page;
    RelativeLayout home, replay, close;
    TextView []tv = new TextView[9];
    TextView tvTopicName, score, curAccuracy, totTime, ovAllAccuracy, quesSolved, corAns, incorAns;
    int finalScore;
    String topicName, topicURL;
    double totalTime;
    int[] ans = new int[10];
    DonutProgress donutProgress, donutProgress1, donutProgress2, donutProgress3;
    LinearLayout progressLayout, donutLayout, nextPage;
    ProgressBar progressBar;

    // newInstance constructor for creating fragment with arguments
    /*public static FragmentScorePage newInstance(int page) {
        FragmentScorePage fragmentFirst = new FragmentScorePage();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }*/

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
        finalScore = getArguments().getInt("finalScore");
        topicName = getArguments().getString("topicName");
        topicURL = getArguments().getString("topicURL");
        totalTime = getArguments().getDouble("totalTime");
        ans = getArguments().getIntArray("userAnswers");
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_score_page, container, false);

        home = (RelativeLayout) view.findViewById(R.id.home);
        replay = (RelativeLayout) view.findViewById(R.id.replay);
        close = (RelativeLayout) view.findViewById(R.id.close);
        tvTopicName = (TextView)view.findViewById(R.id.topic_name);
        score = (TextView)view.findViewById(R.id.score);
        totTime = (TextView)view.findViewById(R.id.totalTime);
        curAccuracy = (TextView)view.findViewById(R.id.currentAccuracy);
        ovAllAccuracy = (TextView)view.findViewById(R.id.overallAccuracy);
        quesSolved = (TextView)view.findViewById(R.id.quesSolved);
        corAns = (TextView)view.findViewById(R.id.correctAns);
        incorAns = (TextView)view.findViewById(R.id.incorrectAns);
        donutProgress = (DonutProgress)view.findViewById(R.id.donutProgress);
        donutProgress1 = (DonutProgress)view.findViewById(R.id.donutProgress1);
        donutProgress2 = (DonutProgress)view.findViewById(R.id.donutProgress2);
        donutProgress3 = (DonutProgress)view.findViewById(R.id.donutProgress3);
        progressBar = (ProgressBar)view.findViewById(R.id.progressBar);
        progressLayout = (LinearLayout) view.findViewById(R.id.progressLayout);
        donutLayout = (LinearLayout)view.findViewById(R.id.donutLayout);
        nextPage = (LinearLayout)view.findViewById(R.id.nextPage);
        for(int y=0; y<9; y++) {
            String buttonID = "textView" + y;
            int resID = getResources().getIdentifier(buttonID, "id", getActivity().getPackageName());
            tv[y] = ((TextView) view.findViewById(resID));
        }

        //setting typefaces
        Typeface ourBoldFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/primebold.otf");
        Typeface ourLightFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/primelight.otf");
        tvTopicName.setTypeface(ourBoldFont);
        score.setTypeface(ourBoldFont);
        totTime.setTypeface(ourBoldFont);
        curAccuracy.setTypeface(ourBoldFont);
        ovAllAccuracy.setTypeface(ourBoldFont);
        quesSolved.setTypeface(ourBoldFont);
        corAns.setTypeface(ourBoldFont);
        incorAns.setTypeface(ourBoldFont);
        for (int i=0; i<9; i++) {
            tv[i].setTypeface(ourLightFont);
        }

        //making progressbar visible and donutlayout invisible
        progressLayout.setVisibility(View.VISIBLE);
        donutLayout.setVisibility(View.INVISIBLE);

        //setting color of progress spinner of donutlayout
        progressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.currentGameStats), PorterDuff.Mode.MULTIPLY);

        //calculate current accuracy
        int s=ans[0]+ans[1]+ans[2]+ans[3]+ans[4]+ans[5]+ans[6];;
        System.out.println("s = "+s);
        float temp=(float)s;
        System.out.println("temp = "+temp);
        float temp1=(temp/7)*100;
        System.out.println("temp1 = "+temp1);
        float acc = Float.valueOf(String.format("%.1f",temp1));
        System.out.println("acc = "+acc);

        //printing test values
        System.out.println("currentAccuracy"+acc);
        System.out.println("finalscore = "+finalScore);
        System.out.println("topicName = "+topicName);
        System.out.println("totalTime = "+totalTime);
        System.out.println("ans  = "+ans[0]+" "+ans[1]+" "+ans[2]+" "+ans[3]+" "+ans[4]+" "+ans[5]+" "+ans[6]);

        //setting values in layout textViews
        tvTopicName.setText(topicName);
        score.setText(String.valueOf(finalScore));
        totTime.setText(String.valueOf(totalTime)+"s");
        curAccuracy.setText(String.valueOf(acc)+"%");

        //setting on click listener of home button
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),Select_Topic.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        //setting on click listener of replay button
        replay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1=new Intent(getActivity(),TopicPage.class);
                Topic topicdetails =new Topic(topicName,topicURL,"");
                intent1.putExtra("topic_details",new Gson().toJson(topicdetails));
                startActivity(intent1);
                getActivity().finish();
            }
        });

        //setting on click listener of close button
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                homeIntent.addCategory( Intent.CATEGORY_HOME );
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
            }
        });

        //setting on click listener for nextPage
        nextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((AfterGame)getActivity()).setCurrentitem(1,true);
            }
        });

        //putting entered values into url
        String PID= FirebaseAuth.getInstance().getCurrentUser().getUid();
        String TopicKey = null;
        try {

            TopicKey = URLEncoder.encode(topicName, "UTF-8").replaceAll("\\+", "%20");
            System.out.println("here is encoded key :" + TopicKey);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String URL = "https://quizgame-backend.appspot.com/_ah/api/myapi/v1/updateStats?PID="+PID+"&Q_Correct="+s+"&Score="+finalScore+"&Topic="+TopicKey;
        System.out.println("url :"+URL);

        //Data Downloader-Volley
        final StringRequest request = new StringRequest(URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String data1) {
                System.out.println(data1);
                PlayerScore data = new Gson().fromJson(data1, PlayerScore.class);
                System.out.println(data.getMessage().getAccuracy());

                if (data1 != null) {
                    donutLayout.setVisibility(View.VISIBLE);
                    progressLayout.setVisibility(View.GONE);

                    //Overall Accuracy
                    String a1 = data.getMessage().getAccuracy();
                    Float a2 = Float.valueOf(a1);
                    System.out.println("a2 = "+a2);
                    float result = Float.valueOf(String.format("%.1f",a2));
                    System.out.println("overallAccuracy = "+result);
                    donutProgress.setMax(100);
                    donutProgress.animate();
                    donutProgress.setProgress(result);
                    ovAllAccuracy.setText(((int)(result*10)+"/1000"));

                    //Questions Solved
                    float a = data.getMessage().getQSolved();
                    float b = data.getMessage().getQTotal();
                    System.out.println("a = " + a + " b = " + b);
                    float a3 = Float.valueOf((a / b) * 100);
                    System.out.println("a3 = "+a3);
                    float c = Float.valueOf(String.format("%.1f",a3));
                    System.out.println("c = "+c);
                    donutProgress1.setMax(100);
                    donutProgress1.animate();
                    donutProgress1.setProgress(c);
                    donutProgress1.setText(String.valueOf(c)+"%");
                    quesSolved.setText((int)a+"/"+(int)b);

                    //Correct Answers and Incorrect Answers
                    float d = data.getMessage().getCorrect();
                    System.out.println("d = " + d);
                    donutProgress2.setMax((int)a);
                    donutProgress3.setMax((int)a);
                    donutProgress2.animate();
                    donutProgress3.animate();
                    donutProgress2.setProgress((int)d);
                    donutProgress2.setText(String.valueOf((int)d));
                    donutProgress3.setProgress((int)(a-d));
                    donutProgress3.setText(String.valueOf((int)(a-d)));
                    corAns.setText((int)d+"/"+(int)a);
                    incorAns.setText((int)(a-d)+"/"+(int)a);
                }
                else {

                }
            }
        },
                //Error listener:Server error
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        getActivity().finish();
                        startActivity(getActivity().getIntent());
                        Toast.makeText(getActivity().getApplicationContext(), "Server error Please retry", Toast.LENGTH_SHORT).show();
                    }
                });

        //Adding request Queue
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        queue.add(request);

        return view;
    }

}
