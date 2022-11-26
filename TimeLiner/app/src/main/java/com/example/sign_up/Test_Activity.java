package com.example.sign_up;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

// import Log
import android.util.Log;

// import Firebase Realtime Database
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ValueEventListener;
import com.google.android.gms.tasks.OnCompleteListener;
// import getResult
import com.google.android.gms.tasks.Task;

// import Course
import com.example.sign_up.Course;

// import Toast
import android.widget.Toast;

import java.util.ArrayList;

public class Test_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_page);

        Log.i("Test_Activity", "----onCreate: In Test Page Now----");

        Button btn = (Button) findViewById(R.id.test_database_button);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Connect to Firebase Realtime Database
                String database_url = "https://cscb07-project-tut5-grou-f0436-default-rtdb.firebaseio.com/";
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference();

                // Write a message to the database
                // myRef.child("Test").setValue("Hello, World!");
                // Log.i("Test_Activity", "----onClick: Write to Database----");

                // Log all the data in the database
                myRef.child("DATABASE").child("STUDENTS").get()
                        .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                if (!task.isSuccessful()) {
                                    Log.e("firebase", "----Error getting data----", task.getException());
                                } else {
                                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                                }
                            }
                        });
            }
        });

        // goto student's view all courses page
        findViewById(R.id.test_view_courses).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Test_Activity", "----onClick: Goto View Courses Page----");
                startActivity(new Intent(Test_Activity.this, CoursesTaken_Activity.class));
            }
        });

        // Test Course class by creating a course object and send it to the database
        findViewById(R.id.Test_JavaCourse_Class_Btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Test_Activity", "----onClick: Test Course Class----");
                Session session = new Session(true, true, true);
                ArrayList<String> preRequisiteCourses = new ArrayList<String>();
                preRequisiteCourses.add("CSCA08");
                preRequisiteCourses.add("CSCA48");
                Course course = new Course("Java", "CSCB07", "Java is a programming language", true);
                course.setSessionOffered(session);
                course.setPreRequisiteCourses(preRequisiteCourses);
                course.sendToDatabase();
            }
        });

        // Test Course class by getting a course object from the database
        findViewById(R.id.Test_JavaCourse_Class_Btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Test_Activity", "----onClick: Test Course Class----");
                Course course = new Course();
                
                // Does not work
                // try {
                //     course.getCourseFromDatabase("CSCB07");
                //     Toast.makeText(Test_Activity.this, course.getCourseCode(), Toast.LENGTH_SHORT).show();
                // } catch (InterruptedException e) {
                //     e.printStackTrace();
                // }

                // Doesn't work
                // Use the custom callback function to get the course object
                course.getFromDatabase("CSCB07", new Course.MyCallback() {
                    @Override
                    public void onCallback(Course course_callback) {
                        // set the course object
                        course.setCourseName(course_callback.getCourseName());
                        course.setCourseCode(course_callback.getCourseCode());
                        course.setCourseDescription(course_callback.getCourseDescription());
                        course.setSessionOffered(course_callback.getSessionOffered());
                        course.setPreRequisiteCourses(course_callback.getPreRequisiteCourses());
                        course.setVisible(course_callback.isVisible());
                        Toast.makeText(Test_Activity.this, course.getCourseCode(), Toast.LENGTH_SHORT).show();
                    }
                });
                // // Wait until the data is retrieved from the database
                // while (course.getCourseName() == null) {
                // try {
                // Thread.sleep(1000);
                // } catch (InterruptedException e) {
                // e.printStackTrace();
                // }
                // }
                // Toast.makeText(Test_Activity.this, course.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
