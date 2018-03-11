package com.example.kamlesh.frd;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.kamlesh.frd.Models.Topic;
import com.example.kamlesh.frd.ScorePagePOJO.CustomVerticalViewPager;
import com.google.gson.Gson;

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


}
