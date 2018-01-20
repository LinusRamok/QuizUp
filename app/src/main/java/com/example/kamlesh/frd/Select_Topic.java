package com.example.kamlesh.frd;


import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;


import com.example.kamlesh.frd.Models.Topic;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Iterator;


public class Select_Topic extends AppCompatActivity {

    topic_adapter Adapter;
    FirebaseDatabase database;
    GridView listView1;
    SQLiteDatabase db;
    DatabaseReference myRef;

    SharedPreferences pref=null;

    android.widget.SearchView searchView;

    ArrayList<Topic> topics = new ArrayList<>();

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database = FirebaseDatabase.getInstance();

        View dv=getWindow().getDecorView();
        int ui= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        dv.setSystemUiVisibility(ui);

        setContentView(R.layout.activity_select_topic);
        Button settings = findViewById(R.id.settings);
        listView1 = (GridView) findViewById(R.id.listview);
        searchView= findViewById(R.id.search);
        myRef = database.getReference();


        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Select_Topic.this,logout_page.class));
                //  finish();
            }
        });

        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                System.out.println("item clicked...");
                System.out.println(topics.get(i).name);
                System.out.println(topics.get(i).url);
                System.out.println(topics.get(i).description);
                Intent intent = new Intent(Select_Topic.this,topic_page.class);
                intent.putExtra("topic_details", new Gson().toJson(topics.get(i)));
                startActivity(intent);


            }
        });


        myRef.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();
                db=openOrCreateDatabase("trainlist",MODE_PRIVATE,null);
                topics.clear();

                while (iterator.hasNext()){
                    Topic t = iterator.next().getValue(Topic.class);

                    System.out.println("here are values :");
                    System.out.println(t.name);
                    System.out.println(t.description);
                    System.out.println(t.url);
                    //   if (topics.contains(t)) {
                    topics.add(t);
                    //   }
                }


                Adapter = new topic_adapter(Select_Topic.this,topics);
                listView1.setAdapter(Adapter);
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });



    }



    @Override
    protected void onStart() {
        // Read from the database
        super.onStart();



 //               searchView.setIconified(false);
        searchView.setQueryHint("Search Topic....");
        searchView.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            boolean list1visible=false;
            @Override
            public boolean onQueryTextChange(String newText) {
                String text = newText;
           //  System.out.println(text);
                try {
                    Adapter.filter(text);
                }catch (Exception e){
                    e.fillInStackTrace();
                }
                return false;
            }
        });

    }
    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }



}
