package com.example.sign_up;

import static android.app.PendingIntent.getActivity;

import static java.security.AccessController.getContext;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

// import Log
import android.util.Log;
import android.widget.ListView;

// import Firebase Realtime Database
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ValueEventListener;
import com.google.android.gms.tasks.OnCompleteListener;
// import getResult
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class CoursesTaken_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.courses_taken_page);

        String studentID = "student_0"; // set id for test
        ArrayList<String> courseTakenList = new ArrayList<String>();
        ArrayList<String> keys = new ArrayList<String>();

        String database_url = "https://cscb07-project-tut5-grou-f0436-default-rtdb.firebaseio.com/";
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        myRef.child("DATABASE").child("STUDENTS").child(studentID).child("course_taken")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        keys.clear();
                        for (DataSnapshot key : snapshot.getChildren()) {
                            keys.add(key.getKey());
                            Log.d("view", keys.toString());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

        myRef.child("DATABASE").child("COURSES").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                courseTakenList.clear();
                for (DataSnapshot key : snapshot.getChildren()) {
                    if (keys.contains(key.getKey())) {
                        String course_name = key.child("course_name").getValue().toString();
                        courseTakenList.add(key.getKey() + " " + course_name);
                        // Log.d("view", courseTakenList.toString());
                        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(CoursesTaken_Activity.this,
                                android.R.layout.simple_list_item_1, courseTakenList);
                        ListView listView = (ListView) findViewById(R.id.course_taken_view);
                        listView.setAdapter(itemsAdapter);
                    }
                }

                Log.d("view", courseTakenList.toString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        // check course list
        Log.d("___________", courseTakenList.toString());

        // ArrayAdapter<String> itemsAdapter =
        // new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,
        // courseTakenList);
        // ListView listView = (ListView) findViewById(R.id.course_taken_view);
        // listView.setAdapter(itemsAdapter);
    }

}