package com.example.kamlesh.frd;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.kamlesh.frd.Models.Questionlist;
import com.example.kamlesh.frd.Models.QuestionsApi;
import com.example.kamlesh.frd.Models.Topic;
import com.example.kamlesh.frd.ScorePagePOJO.CustomVerticalViewPager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class AfterGame extends AppCompatActivity {

    FragmentPagerAdapter adapterViewPager;
    int ans[]=new int[7],scoreArray[]=new int[7];;
    String questionsAndAnswers;
    double TimeForEachQues[]=new double[7];
    double totalTime;
    int finalscore,qnoforperk[]=new int[2];
    String Topic_name;
    String topic_url;
    public CustomVerticalViewPager vpPager;
    List<Questionlist> q_and_a=new ArrayList<>();
    QuestionsApi obj;

    @Override
    protected void onResume() {
        super.onResume();

        View dv=getWindow().getDecorView();
        int ui= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        dv. setSystemUiVisibility(ui);
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_game);

        View dv = getWindow().getDecorView();
        int ui = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        dv.setSystemUiVisibility(ui);

        //getting everything passed through intent
        TimeForEachQues = getIntent().getDoubleArrayExtra("TimerValues");
        ans = getIntent().getIntArrayExtra("userAnswers");
        scoreArray = getIntent().getIntArrayExtra("scoreArray");
        questionsAndAnswers = getIntent().getStringExtra("QuestionAndAnswers");
        finalscore = getIntent().getIntExtra("Score", 0);
        qnoforperk = getIntent().getIntArrayExtra("perkvalues");
        Topic_name = getIntent().getExtras().getString("topic_name");
        topic_url = getIntent().getExtras().getString("topic_url");
        totalTime = getIntent().getExtras().getDouble("totalTime");

        new SendPostRequest().execute();

        float []timeArray = new float[7];
        for(int i=0;i<7;i++) {
            timeArray[i]=(float)TimeForEachQues[i];
        }
        float totalTimeF = (float)totalTime;

        //sending data from activity to FragmentScorePage
        Bundle bundle = new Bundle();
        bundle.putInt("finalScore", finalscore);
        bundle.putString("topicName", Topic_name);
        bundle.putString("topicURL", topic_url);
        bundle.putDouble("totalTime", totalTime);
        bundle.putIntArray("userAnswers",ans);

        //sending data from activity to FragmentGameAnalysis
        Bundle bundle1 = new Bundle();
        bundle1.putIntArray("scoreArray",scoreArray);
        bundle1.putFloatArray("timeArray", timeArray);
        bundle1.putFloat("totalTime", totalTimeF);
        bundle1.putIntArray("userAnswers",ans);

        //setting up vertical view pager
        vpPager = (CustomVerticalViewPager) findViewById(R.id.vpPager);
        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager(), bundle, bundle1);
        vpPager.setAdapter(adapterViewPager);
        vpPager.setPageMargin(0);

        System.out.println("finalscore in act = " + finalscore);
        System.out.println("topicName in act = " + Topic_name);
        System.out.println("totalTime in act = " + totalTime);
        System.out.println("ans in ant = "+ans[0]+" "+ans[1]+" "+ans[2]+" "+ans[3]+" "+ans[4]+" "+ans[5]+" "+ans[6]);
    }


    public static class MyPagerAdapter extends FragmentPagerAdapter {
        private static int NUM_ITEMS = 2;
        private final Bundle fragmentBundle, fragmentBundle1;
        public MyPagerAdapter(FragmentManager fragmentManager, Bundle bundle, Bundle bundle1) {
            super(fragmentManager);
            fragmentBundle=bundle;
            fragmentBundle1=bundle1;
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: // Fragment # 0 - This will show FirstFragment
                    FragmentScorePage fragmentScorePage = new FragmentScorePage();
                    fragmentScorePage.setArguments(fragmentBundle);
                    return fragmentScorePage;
                    //return fragmentScorePage.newInstance(0);
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    FragmentGameAnalysis fragmentGameAnalysis = new FragmentGameAnalysis();
                    fragmentGameAnalysis.setArguments(fragmentBundle1);
                    return fragmentGameAnalysis;
                default:
                    return null;
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            return "Page " + position;
        }

    }

    public void setCurrentitem(int item, boolean smoothScroll) {
        vpPager.setCurrentItem(item, smoothScroll);
    }

    @Override
    public void onBackPressed() {
        if (vpPager.getCurrentItem() == 0) {
            Intent intent1 = new Intent(getApplicationContext(), TopicPage.class);
            Topic topicdetails = new Topic(Topic_name, topic_url, "");
            intent1.putExtra("topic_details", new Gson().toJson(topicdetails));
            startActivity(intent1);
            finish();
        }
        else {
            setCurrentitem((vpPager.getCurrentItem()-1), true);
        }
    }
    public class SendPostRequest extends AsyncTask<String, Void, String> {

        protected void onPreExecute(){}

        protected String doInBackground(String... arg0) {
            obj =new Gson().fromJson(questionsAndAnswers , QuestionsApi.class);
            q_and_a= obj.getQuestionlist();

            String qid[]=new String[8];
            for (int l=0;l<=7;l++)
            {
                qid[l]=q_and_a.get(l).getQID();
            }

            if (qnoforperk[1]!=9)
            {
                qid[qnoforperk[1]]=qid[7];
            }
            try {
                String PID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                URL url = new URL("https://quizgame-backend.appspot.com/_ah/api/myapi/v1/dnldQuests?"); // here is your URL path

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("PID",PID);
                postDataParams.put("Qid1",qid[0]);
                postDataParams.put("Qid2",qid[1]);
                postDataParams.put("Qid3",qid[2]);
                postDataParams.put("Qid4",qid[3]);
                postDataParams.put("Qid5",qid[4]);
                postDataParams.put("Qid6",qid[5]);
                postDataParams.put("Qid7",qid[6]);

//                postDataParams.put("name", "abc");
//                postDataParams.put("email", "abc@gmail.com");
                System.out.println("params :"+postDataParams.toString());
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();

                int responseCode=conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in=new BufferedReader(new
                            InputStreamReader(
                            conn.getInputStream()));

                    StringBuffer sb = new StringBuffer("");
                    String line="";

                    while((line = in.readLine()) != null) {

                        sb.append(line);
                        break;
                    }

                    in.close();
                    return sb.toString();

                }
                else {
                    return new String("false : "+responseCode);
                }
            }
            catch(Exception e){
                return new String("Exception: " + e.getMessage());
            }

        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getApplicationContext(), result,
                    Toast.LENGTH_LONG).show();
        }
    }

    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while(itr.hasNext()){

            String key= itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }

}
