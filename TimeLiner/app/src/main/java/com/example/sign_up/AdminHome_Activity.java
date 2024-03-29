package com.example.sign_up;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
        Button sign_out_btn = findViewById(R.id.sign_out_adm);
        Button add_course_btn = findViewById(R.id.admin_add_button);

        // Goto Home Page
        sign_out_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // show all courses
        myRef.child("DATABASE").child("COURSES").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Course_List.clear();
                for (DataSnapshot key : snapshot.getChildren()) {
                    // add course to Course_List if visible
                    if (key.child("visible").getValue().toString().equals("true")) {
                        Course_List.add(key.getValue(Course.class));
                    }
                }
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

        // go to add new course page
        add_course_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminHome_Activity.this, AdminAdd_Activity.class));
            }
        });
    }
}