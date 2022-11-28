package com.example.sign_up;

import static android.app.PendingIntent.getActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

// import Log
import android.util.Log;
import android.widget.ListView;

// import Firebase Realtime Database
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ValueEventListener;
// import getResult

import java.util.ArrayList;

public class CoursesTaken_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_page);

        String studentID = "student_0"; // set id for test
        ArrayList<String> CourseTaken_List = new ArrayList<String>();
        ArrayList<String> Keys_List = new ArrayList<String>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        myRef.child("DATABASE").child("STUDENTS").child(studentID).child("course_taken")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Keys_List.clear();
                        for (DataSnapshot key : snapshot.getChildren()) {
                            Keys_List.add(key.getKey());
                            Log.d("KeyList", Keys_List.toString());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

        myRef.child("DATABASE").child("COURSES").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                CourseTaken_List.clear();
                for (DataSnapshot key : snapshot.getChildren()) {
                    if (Keys_List.contains(key.getKey())) {
                        String course_name = key.child("courseName").getValue().toString();
                        CourseTaken_List.add(key.getKey() + " " + course_name);
                    }
                }
                ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(CoursesTaken_Activity.this,
                        android.R.layout.simple_list_item_1, CourseTaken_List);
                ListView listView = (ListView) findViewById(R.id.course_taken_view);
                listView.setAdapter(itemsAdapter);
                Log.d("CourseList", CourseTaken_List.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

}