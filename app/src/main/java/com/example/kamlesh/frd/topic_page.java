package com.example.kamlesh.frd;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.kamlesh.frd.Models.Topic;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class topic_page extends AppCompatActivity {
    Topic topic;
    FirebaseStorage storage =FirebaseStorage.getInstance();
    TextView playButton, txt, txt1, txt2, accuracy, quesCompleted, totalQues;
    ProgressBar p;
    String Topic_name;
    JSONObject respass=null;

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

        View dv=getWindow().getDecorView();
        int ui= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        dv.setSystemUiVisibility(ui);

        setContentView(R.layout.activity_topic_page);
        TextView topic_name =findViewById(R.id.topic_name);
        ImageView topic_image =findViewById(R.id.topic_image);

           try {
               if (getIntent() != null) {
                   topic = new Gson().fromJson(getIntent().getStringExtra("topic_details"), Topic.class);
                   System.out.println(topic.url);
                   System.out.println(topic.name);
                   System.out.println(topic.description);
                   topic_name.setText(topic.name);
                   Topic_name = topic.name;
                   StorageReference storageRef = storage.getReference(topic.url);
                   Glide.with(this)
                           .load(storageRef)
                           .into(topic_image);

               }
           }catch (Exception e){
               System.out.println(e.toString());
           }
        playButton = (TextView)findViewById(R.id.playButton);
        txt = (TextView)findViewById(R.id.txt);
        txt1 = (TextView)findViewById(R.id.txt1);
        txt2 = (TextView)findViewById(R.id.txt2);
        accuracy = (TextView)findViewById(R.id.accuracy);
        quesCompleted = (TextView)findViewById(R.id.quesCompleted);
        totalQues = (TextView)findViewById(R.id.totalQues);

        Typeface ourBoldFont = Typeface.createFromAsset(getAssets(), "fonts/primebold.otf");
        Typeface ourLightFont = Typeface.createFromAsset(getAssets(), "fonts/primelight.otf");
        topic_name.setTypeface(ourBoldFont);
        playButton.setTypeface(ourBoldFont);
        txt.setTypeface(ourBoldFont);
        txt1.setTypeface(ourBoldFont);
        txt2.setTypeface(ourLightFont);
        accuracy.setTypeface(ourLightFont);
        quesCompleted.setTypeface(ourLightFont);
        totalQues.setTypeface(ourLightFont);

        p = (ProgressBar)findViewById(R.id.progressBar2);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                topic_page.getcontentfornextactivity g= new topic_page.getcontentfornextactivity();
                g.execute();
            }
        });


    }
    public class getcontentfornextactivity extends AsyncTask<Integer ,Integer,String> {

        @Override
        protected void onPreExecute() {
            p.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String s) {
            p.setVisibility(View.GONE);
            Intent i=new Intent(topic_page.this,GamePlay.class);
            //intent topic name
            i.putExtra("topic_name", Topic_name);
            i.putExtra("topic_url", topic.url);
            i.putExtra("jsonobj",respass.toString());
            startActivity(i);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {

        }

        @Override
        protected String doInBackground(Integer... params) {
            String TopicKey = null;
            try {

               TopicKey = URLEncoder.encode(Topic_name, "UTF-8").replaceAll("\\+", "%20");
                System.out.println("here is encoded key :" + TopicKey);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return e.toString();
            }



            try {
                JSONObject response = getJSONObjectFromURL("https://quizgame-backend.appspot.com/_ah/api/myapi/v1/dnldQuests?PID=kamlesh&Topic="+TopicKey); // calls method to get JSON object
                respass=response;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return "Task Completed.";
        }
    }
    public static JSONObject getJSONObjectFromURL(String urlString) throws IOException, JSONException {

        HttpURLConnection urlConnection = null;

        URL url = new URL(urlString);

        urlConnection = (HttpURLConnection) url.openConnection();

        urlConnection.setRequestMethod("GET");
        urlConnection.setReadTimeout(10000 /* milliseconds */);
        urlConnection.setConnectTimeout(15000 /* milliseconds */);

        urlConnection.setDoOutput(true);

        urlConnection.connect();

        BufferedReader br=new BufferedReader(new InputStreamReader(url.openStream()));

        char[] buffer = new char[1024];

        String jsonString = new String();

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line+"\n");
        }
        br.close();

        jsonString = sb.toString();

        System.out.println("JSON: " + jsonString);
        urlConnection.disconnect();

        return new JSONObject(jsonString);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
