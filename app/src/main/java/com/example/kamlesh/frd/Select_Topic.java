package com.example.kamlesh.frd;


import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;


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

    GridView listView1;
    SQLiteDatabase db;
    DatabaseReference myRef;

    SharedPreferences pref=null;

    android.widget.SearchView searchView;

    ArrayList<Topic> topics = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_topic);
  //      pref=getSharedPreferences("com.example.kamlesh.frd",MODE_PRIVATE);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.setPersistenceEnabled(true);
        listView1 = (GridView) findViewById(R.id.listview);
        searchView= findViewById(R.id.search);



        myRef = database.getReference();


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


    }

    @Override
    protected void onStart() {
        // Read from the database
        super.onStart();
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
                Adapter.filter(text);
                return false;
            }
        });

    }



}
