package com.example.kamlesh.frd;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.daimajia.numberprogressbar.NumberProgressBar;
import com.example.kamlesh.frd.Models.Topic;
import com.google.firebase.auth.FirebaseAuth;
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
import java.util.ArrayList;
import java.util.Random;

import devlight.io.library.ArcProgressStackView;

public class TopicPage extends AppCompatActivity {
    Topic topic;
    FirebaseStorage storage =FirebaseStorage.getInstance();
    LinearLayout playButton;
    TextView playTxt;
    ProgressBar p;
    String Topic_name;
    JSONObject respass=null;
    Integer count = 0;

    @Override
    protected void onResume() {
        super.onResume();

        View dv=getWindow().getDecorView();
        int ui= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        dv.setSystemUiVisibility(ui);
    }
    public boolean isConnected() throws InterruptedException, IOException
    {
        String command = "ping -c 1 google.com";
        return (Runtime.getRuntime().exec (command).waitFor() == 0);
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

        Typeface ourBoldFont = Typeface.createFromAsset(getAssets(), "fonts/primebold.otf");
        Typeface ourLightFont = Typeface.createFromAsset(getAssets(), "fonts/primelight.otf");



        try {
               if (getIntent() != null) {
                   topic = new Gson().fromJson(getIntent().getStringExtra("topic_details"), Topic.class);
                   System.out.println(topic.url);
                   System.out.println(topic.name);
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
        playButton = (LinearLayout) findViewById(R.id.playButton);
        playTxt = (TextView) findViewById(R.id.playText);

        topic_name.setTypeface(ourBoldFont);
        playTxt.setTypeface(ourBoldFont);

        p = (ProgressBar)findViewById(R.id.progressBar2);
           playButton.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   try {
                       if (isConnected()) {
                           TopicPage.getcontentfornextactivity g = new TopicPage.getcontentfornextactivity();
                           g.execute(100);
                       } else
                           Toast.makeText(TopicPage.this, "Not connected to internet", Toast.LENGTH_SHORT).show();
                   }
                   catch (Exception e)
                   {}


               }
           });


        int[] progressBgColor = getResources().getIntArray(R.array.progressBgColor);
        int[] progressColor = getResources().getIntArray(R.array.progressColor);

        final ArrayList<ArcProgressStackView.Model> models = new ArrayList<>();
        models.add(new ArcProgressStackView.Model("Accuracy", 25, progressBgColor[0], progressColor[0] ));
        models.add(new ArcProgressStackView.Model("Qustions Solved", 90, progressBgColor[1], progressColor[1] ));
        models.add(new ArcProgressStackView.Model("Correctly Answered", 75, progressBgColor[2], progressColor[2] ));
        models.add(new ArcProgressStackView.Model("Incorrectly Answered", 50, progressBgColor[3], progressColor[3] ));

        final ArcProgressStackView arcProgressStackView  = (ArcProgressStackView)findViewById(R.id.apsv);
        arcProgressStackView.setModels(models);
        arcProgressStackView.animateProgress();
    }
    public class getcontentfornextactivity extends AsyncTask<Integer ,Integer,String> {

        Dialog loadingDialog = new Dialog(TopicPage.this);
        TextView boxTitle;
        NumberProgressBar progressBar;
        Boolean isDialogOpened = false;

        @Override
        protected void onPreExecute() {

            loadingDialog.setContentView(R.layout.alertdialog_loading);
            loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));  //to make background of dialog transparent, hence allowing curved borders to be visible
            loadingDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE); //to make dialog not focussible, so that immersive mode can persist
            loadingDialog.setCanceledOnTouchOutside(false);

            boxTitle = loadingDialog.findViewById(R.id.boxTitle);
            progressBar = loadingDialog.findViewById(R.id.progressBar);

            boxTitle.setText("Loading");

            Typeface ourBoldFont = Typeface.createFromAsset(getAssets(), "fonts/primebold.otf");
            Typeface ourLightFont = Typeface.createFromAsset(getAssets(), "fonts/primelight.otf");
            boxTitle.setTypeface(ourBoldFont);

            isDialogOpened = true;
            loadingDialog.show();
            loadingDialog.getWindow().getDecorView().setSystemUiVisibility(TopicPage.this.getWindow().getDecorView().getSystemUiVisibility());
            loadingDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE); //clear not focussible flags

            loadingDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    loadingDialog.dismiss();
                    isDialogOpened = false;
                }
            });

            progressBar.setProgress(0);
        }

        @Override
        protected void onPostExecute(String s) {
            if(isDialogOpened == true)
            {
                try {
                    loadingDialog.dismiss();
                    isDialogOpened = false;
                    Intent i = new Intent(TopicPage.this, GamePlay.class);
                    //intent topic name
                    i.putExtra("topic_name", Topic_name);
                    i.putExtra("topic_url", topic.url);
                    i.putExtra("jsonobj", respass.toString());
                    startActivity(i);
                }
                catch (Exception e)
                {
                    Toast.makeText(TopicPage.this, "Someting went wrong... please try again", Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressBar.setProgress(values[0]);
        }

        @Override
        protected String doInBackground(Integer... params) {
            for (count = 0; count <= params[0]; count++) {
                    publishProgress(count);
                    System.out.println("count = "+count);
            }

            String TopicKey = null;
            try {
               TopicKey = URLEncoder.encode(Topic_name, "UTF-8").replaceAll("\\+", "%20");
                System.out.println("here is encoded key :" + TopicKey);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return e.toString();
            }
            try {
                String PID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                JSONObject response = getJSONObjectFromURL("https://quizgame-backend.appspot.com/_ah/api/myapi/v1/dnldQuests?PID="+PID+"&Topic="+TopicKey); // calls method to get JSON object
                respass=response;
            } catch (IOException e) {
                System.out.println("error 1");
                e.printStackTrace();
            } catch (JSONException e) {
                System.out.println("error 2");
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
