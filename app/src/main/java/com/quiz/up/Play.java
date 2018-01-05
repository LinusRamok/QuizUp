package com.quiz.up;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Play extends AppCompatActivity {

    Button b;
    ProgressBar p;
    JSONObject respass=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View dv=getWindow().getDecorView();
        int ui=View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        dv.setSystemUiVisibility(ui);
        setContentView(R.layout.activity_play);
        b = (Button)findViewById(R.id.button);
        p = (ProgressBar)findViewById(R.id.progressBar2);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getcontentfornextactivity g= new getcontentfornextactivity();
                g.execute();
            }
        });
    }

    public class getcontentfornextactivity extends AsyncTask<Integer ,Integer,String>{

        @Override
        protected void onPreExecute() {
            p.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String s) {
            p.setVisibility(View.GONE);
            Intent i=new Intent(Play.this,GamePlay.class);
            i.putExtra("jsonobj",respass.toString());
            startActivity(i);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {

        }

        @Override
        protected String doInBackground(Integer... params) {

            try {
                JSONObject response = getJSONObjectFromURL("https://quizgame-backend.appspot.com/_ah/api/myapi/v1/dnldQuests?PID=kamlesh&Topic=astrology"); // calls method to get JSON object
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
}
