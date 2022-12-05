package com.example.sign_up;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

// import Spinner
import android.widget.AdapterView;
import android.widget.Spinner;
// import android.widget.AdapterView.OnItemSelectedListener;

// import ArrayAL

import java.util.ArrayList;

public class CoursesWanted_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wish_course_page);

        // Get studentID from previous activity
        String studentID = getIntent().getStringExtra("studentID");
        ArrayList<String> Wanted_Keys = new ArrayList<String>();
        ArrayList<String> Taken_Keys = new ArrayList<String>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        Button AddWish_Button = findViewById(R.id.wishlist_add);
        Button DeleteWish_Button = findViewById(R.id.wishlist_delete);
        Button Back_Button = findViewById(R.id.backto_courses_taken);
        EditText Wish_EditText = findViewById(R.id.wishlist_edit_text);
        Button Goto_Planner = findViewById(R.id.generate_planner);

        Spinner available_year = findViewById(R.id.available_year);
        Spinner available_session = findViewById(R.id.available_session);

        // Set the spinner for available year
        ArrayList<String> year_list = new ArrayList<String>();
        year_list.add("2022-2023");
        year_list.add("2023-2024");
        year_list.add("2024-2025");
        year_list.add("2025-2026");
        year_list.add("2026-2027");
        
        ArrayAdapter<String> year_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, year_list);
        year_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        available_year.setAdapter(year_adapter);

        // Set the spinner for available session
        ArrayList<String> session_list = new ArrayList<String>();
        session_list.add("Winter"); // 0
        session_list.add("Summer"); // 1
        session_list.add("Fall");   // 2

        ArrayAdapter<String> session_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, session_list);
        session_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        available_session.setAdapter(session_adapter);



        // Goto planner activity when button goto_planner is clicked
        Goto_Planner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the selected year and session
                String selected_year = available_year.getSelectedItem().toString();
                String selected_session = available_session.getSelectedItem().toString();

                int session_index = session_list.indexOf(selected_session);
                int year_index = year_list.indexOf(selected_year);
                
                // Convert the selected year and session to integer
                if (selected_year.equals("2022-2023")) {
                    year_index = 2022;
                } else if (selected_year.equals("2023-2024")) {
                    year_index = 2023;
                } else if (selected_year.equals("2024-2025")) {
                    year_index = 2024;
                } else if (selected_year.equals("2025-2026")) {
                    year_index = 2025;
                } else if (selected_year.equals("2026-2027")) {
                    year_index = 2026;
                }

                if (selected_session.equals("Winter")) {
                    session_index = 0;
                } else if (selected_session.equals("Summer")) {
                    session_index = 1;
                } else if (selected_session.equals("Fall")) {
                    session_index = 2;
                }

                Intent intent = new Intent(CoursesWanted_Activity.this, Planner_Activity.class);
                intent.putExtra("studentID", studentID);
                intent.putExtra("selected_year", year_index);
                intent.putExtra("selected_session", session_index);
                startActivity(intent);
            }
        });

        myRef.child("DATABASE").child("STUDENTS").child(studentID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Taken_Keys.clear();
                        Wanted_Keys.clear();

                        for (DataSnapshot key : snapshot.child("course_want").getChildren()) {
                            Wanted_Keys.add(key.getKey());
                            Log.d("Wanted Key Added", Wanted_Keys.toString());
                        }

                        // Get the list of taken courses
                        for (DataSnapshot key : snapshot.child("course_taken").getChildren()) {
                            Taken_Keys.add(key.getKey());
                            Log.d("Taken Key Added", Taken_Keys.toString());
                        }

                        showCourse(myRef, Wanted_Keys, studentID);

                        AddWish_Button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String Add_Course = String.valueOf(Wish_EditText.getText()).toUpperCase();
                                addCourse(myRef, Add_Course, studentID, Wanted_Keys, Taken_Keys);
                            }
                        });

                        DeleteWish_Button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String Delete_Course = String.valueOf(Wish_EditText.getText()).toUpperCase();
                                deleteCourse(myRef, Delete_Course, studentID, Wanted_Keys);
                            }
                        });

                        Back_Button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                finish();
                            }
                        });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }

    private void showCourse(DatabaseReference database, ArrayList<String> keys, String id) {
        database.child("DATABASE").child("COURSES").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> CourseWanted_List = new ArrayList<String>();
                for (DataSnapshot key : snapshot.getChildren()) {
                    if (keys.contains(key.getKey())) {
                        if (!(boolean) key.child("visible").getValue()) {
                            AlertDialog.Builder dialog = new AlertDialog.Builder(CoursesWanted_Activity.this);
                            dialog.setTitle("Notice: Course Cancelled");
                            dialog.setMessage(key.getKey() + " has been cancelled.");
                            dialog.setCancelable(false);
                            dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    database.child("DATABASE").child("STUDENTS").child(id)
                                            .child("course_want").child(key.getKey()).removeValue();
                                }
                            });
                            dialog.show();
                            return;
                        }
                        String Course_Name = key.child("courseName").getValue().toString();
                        CourseWanted_List.add(key.getKey() + "-" + Course_Name);
                    }
                }
                ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(CoursesWanted_Activity.this,
                        android.R.layout.simple_list_item_1, CourseWanted_List);
                ListView CourseWanted_Listview = findViewById(R.id.wish_course_view);
                CourseWanted_Listview.setAdapter(itemsAdapter);
                Log.d("CourseList", CourseWanted_Listview.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void addCourse(DatabaseReference database, String course, String id,
                           ArrayList<String> wanted_keys, ArrayList<String> taken_keys) {
        if (wanted_keys.contains(course)) {
            Toast.makeText(CoursesWanted_Activity.this, "Course already added",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if course exists in taken courses
        if (taken_keys.contains(course)) {
            Toast.makeText(CoursesWanted_Activity.this, "Course already taken",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        
        database.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "----Error getting data----", task.getException());
                    Toast.makeText(CoursesWanted_Activity.this, "Error getting data",
                            Toast.LENGTH_SHORT).show();
                } else if (!task.getResult().child("DATABASE").child("COURSES").hasChild(course)) {
                    Toast.makeText(CoursesWanted_Activity.this,
                            "Course " + course + " does not exist", Toast.LENGTH_SHORT).show();
                } else {
                    Course toCheck = task.getResult().child("DATABASE").child("COURSES")
                            .child(course).getValue(Course.class);
                    if (!toCheck.visible) {
                        Toast.makeText(CoursesWanted_Activity.this,
                                "Course " + course + " does not exist", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    database.child("DATABASE").child("STUDENTS").child(id).child("course_want")
                            .child(course).setValue(course);
                    Toast.makeText(CoursesWanted_Activity.this, "Add course successful",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void deleteCourse(DatabaseReference database, String course, String id, ArrayList<String> keys) {
        if (!keys.contains(course)) {
            Toast.makeText(CoursesWanted_Activity.this, "Course haven't been added",
                    Toast.LENGTH_SHORT).show();
        } else {
            database.child("DATABASE").child("STUDENTS").child(id).child("course_want")
                    .child(course).removeValue();
            Toast.makeText(CoursesWanted_Activity.this, "Remove course successful",
                    Toast.LENGTH_SHORT).show();
        }
    }
}