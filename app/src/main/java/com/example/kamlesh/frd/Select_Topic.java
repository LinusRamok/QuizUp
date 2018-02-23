package com.example.kamlesh.frd;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;


import com.example.kamlesh.frd.Models.Topic;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Iterator;


public class Select_Topic extends AppCompatActivity implements ForceUpdateChecker.OnUpdateNeededListener {

    topic_adapter Adapter;
    FirebaseDatabase database;
    RecyclerView listView1;
    SQLiteDatabase db;
    DatabaseReference myRef;

    SharedPreferences pref=null;

    SearchView searchView;
    EditText searchEditText;
    ImageView searchIcon, closeIcon;

    ArrayList<Topic> topics = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ForceUpdateChecker.with(this).onUpdateNeeded(this).check();

        database = FirebaseDatabase.getInstance();

        /*View dv=getWindow().getDecorView();
        int ui= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        dv.setSystemUiVisibility(ui);*/

        setContentView(R.layout.activity_select_topic);
        listView1 = (RecyclerView) findViewById(R.id.listview);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),3);
        listView1.setLayoutManager(gridLayoutManager);

        Drawable icon1 = getResources().getDrawable(R.drawable.ic_search);
        Drawable icon2 = getResources().getDrawable(R.drawable.ic_cancel);
        Drawable mWrappedDrawable1 = icon1.mutate();
        Drawable mWrappedDrawable2 = icon2.mutate();
        mWrappedDrawable1 = DrawableCompat.wrap(mWrappedDrawable1);
        mWrappedDrawable2 = DrawableCompat.wrap(mWrappedDrawable2);
        DrawableCompat.setTint(mWrappedDrawable1, getResources().getColor(R.color.colorPrimary));
        DrawableCompat.setTint(mWrappedDrawable2, getResources().getColor(R.color.colorPrimary));

        searchView= findViewById(R.id.search);
        searchEditText = (EditText)searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(R.color.textColorPrimary));
        searchEditText.setHintTextColor(getResources().getColor(R.color.colorPrimary));
        searchIcon = (ImageView)searchView.findViewById(android.support.v7.appcompat.R.id.search_button);
        searchIcon.setImageDrawable(mWrappedDrawable1);
        closeIcon = (ImageView)searchView.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
        closeIcon.setImageDrawable(mWrappedDrawable2);


        Button settings = findViewById(R.id.settings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),logout_page.class);
                startActivity(intent);
            }
        });


        myRef = database.getReference();


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


    }
    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

    @Override
    public void onUpdateNeeded(final String updateUrl) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("New version available")
                .setMessage("Please, update app to new version to continue experiencing.")
                .setPositiveButton("Update",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                redirectStore(updateUrl);
                            }
                        }).setNegativeButton("No, thanks",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        }).create();
        dialog.show();
    }
    private void redirectStore(String updateUrl) {
        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(updateUrl));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

}
