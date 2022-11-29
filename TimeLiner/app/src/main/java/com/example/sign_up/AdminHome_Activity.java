package com.example.sign_up;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminHome_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adm_page);

        ArrayList<Course> Course_List = new ArrayList<Course>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        // test: show CSCB07
        myRef.child("DATABASE").child("COURSES").child("CSCB07").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Course_List.clear();
                Course_List.add(snapshot.getValue(Course.class));

                ArrayAdapter<Course> itemsAdapter = new ArrayAdapter<Course>(AdminHome_Activity.this,
                        android.R.layout.simple_list_item_1, Course_List);
                ListView Course_Listview = (ListView) findViewById(R.id.all_course_list);
                Course_Listview.setAdapter(itemsAdapter);
                Log.d("CourseList", Course_List.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        // show all courses, haven't been tested
        // requires all data in COURSES fits the form of class Course
        /*
        myRef.child("DATABASE").child("COURSES").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Course_List.clear();
                for (DataSnapshot key : snapshot.getChildren()) {
                    Course_List.add(key.getValue(Course.class));
                }
                ArrayAdapter<Course> itemsAdapter = new ArrayAdapter<Course>(CoursesAdmin_Activity.this,
                        android.R.layout.simple_list_item_1, Course_List);
                ListView Course_Listview = (ListView) findViewById(R.id.course_admin_view);
                Course_Listview.setAdapter(itemsAdapter);
                Log.d("CourseList", Course_List.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });*/

        // go to edit page for course selected
        ListView Course_Listview = (ListView) findViewById(R.id.all_course_list);
        Course_Listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Course course = Course_List.get(i);
                Intent intent = new Intent(AdminHome_Activity.this, AdminEdit_Activity.class);
                // pass the selected course's code to edit page
                intent.putExtra("Course_Code", course.getCourseCode());
                startActivity(intent);
            }
        });
    }
}