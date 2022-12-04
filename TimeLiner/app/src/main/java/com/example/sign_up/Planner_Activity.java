package com.example.sign_up;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.util.ArrayList;

// import AcademicYear.java
import com.example.sign_up.AcademicYear;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

// import Log
import android.util.Log;

public class Planner_Activity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.planner_page);

        ArrayList<String> timetable_list = new ArrayList<>();
        // Get studentID from previous activity
        String studentID = getIntent().getStringExtra("studentID");
        Button Goto_Wish = findViewById(R.id.back_wishlist);
        // Output the timetable list on the ListView
        ListView listView = findViewById(R.id.planner);

        // Goto course_wanted activity when button goto_courses_wanted is clicked
        Goto_Wish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Planner_Activity.this, CoursesWanted_Activity.class);
                intent.putExtra("studentID", studentID);
                startActivity(intent);
            }
        });

        // Get all courses from database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Get all courses from database
                    ArrayList<Course> course_taken = new ArrayList<Course>();
                    ArrayList<Course> course_wanted = new ArrayList<Course>();
                    ArrayList<Course> courses = new ArrayList<Course>();

                    // Get all courses from database
                    for (DataSnapshot ds : snapshot.child("DATABASE").child("COURSES").getChildren()) {
                        courses.add(ds.getValue(Course.class));
                    }

                    // Get all courses taken by user
                    if (snapshot.child("DATABASE").child("STUDENTS").child(studentID).child("course_taken").exists()) {
                        for (DataSnapshot ds : snapshot.child("DATABASE").child("STUDENTS").child(studentID).child("course_taken")
                                .getChildren()) {
                            course_taken.add(ds.getValue(Course.class));
                        }
                    }

                    // Get all courses wanted by user
                    if (snapshot.child("DATABASE").child("STUDENTS").child(studentID).child("course_want").exists()) {
                        for (DataSnapshot ds : snapshot.child("DATABASE").child("STUDENTS").child(studentID).child("course_want")
                                .getChildren()) {
                            for(int i = 0; i < courses.size(); i++){
                                if(courses.get(i).courseCode.equals(ds.getValue().toString())){
                                    course_wanted.add(courses.get(i));
                                }
                            }

                        }
                    }

                    //  Log.d("PlannerActivity:", "5. " + course_wanted.get(0).courseCode);
                    // Plan courses from user
                    Planner p = new Planner(course_taken, course_wanted, courses, 0);
                    // Get a timetable list from planner
                    ArrayList<String> timetable_list = p.getTimetableList(2022);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(Planner_Activity.this,
                            android.R.layout.simple_list_item_1, timetable_list);
                    listView.setAdapter(adapter);
                }
            }


            @Override
            public void onCancelled(DatabaseError error) {
                Log.d("Database Error", error.getMessage());
                // Toast.makeText(Planner_Activity.this, "Database Error",
                // Toast.LENGTH_SHORT).show();
            }
        });


    }
}