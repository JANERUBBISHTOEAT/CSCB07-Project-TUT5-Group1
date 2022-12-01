package com.example.sign_up;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.util.ArrayList;

// import AcademicYear.java
import com.example.sign_up.AcademicYear;

// import Log
import android.util.Log;

public class Planner_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.planner_page);

        // Get studentID from previous activity
        String studentID = getIntent().getStringExtra("studentID");
        Button Goto_Wish = findViewById(R.id.back_wishlist);

        // Goto course_wanted activity when button goto_courses_wanted is clicked
        Goto_Wish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Planner_Activity.this, CoursesWanted_Activity.class);
                intent.putExtra("studentID", studentID);
                startActivity(intent);
            }
        });

        // Get a planned timetable from planner(username, sem)
        Planner planner = new Planner(studentID, 0);

        // Get a timetable list from planner
        ArrayList<String> timetable_list = planner.getTimetableList();

        // Output the timetable list on the ListView
        ListView listView = findViewById(R.id.planner);
        Log.d("Planner_Activity", "timetable_list: " + timetable_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, timetable_list);
        listView.setAdapter(adapter);

    }
}