package com.example.sign_up;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AdminEdit_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_edit_page);

        Intent intent = getIntent();
        String Course_Code = intent.getStringExtra("Course_Code");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        Course course = new Course();
        course.getFromDatabase(Course_Code, new Course.MyCallback() {
            @Override
            public void onCallback(Course course_callback) {
                // set the course object
                course.setCourseName(course_callback.getCourseName());
                course.setCourseCode(course_callback.getCourseCode());
                course.setCourseDescription(course_callback.getCourseDescription());
                course.setSessionOffered(course_callback.getSessionOffered());
                course.setPreRequisiteCourses(course_callback.getPreRequisiteCourses());
                course.setVisible(course_callback.isVisible());

                Log.d("Before Edit",course.toString());

                EditText Code_EditText = findViewById(R.id.edit_code_text);
                EditText Name_EditText = findViewById(R.id.edit_name_text);
                EditText Description_EditText = findViewById(R.id.edit_descript_text);
                EditText Pre_EditText = findViewById(R.id.edit_pre_text);
                ListView Pre_ListView = findViewById(R.id.pre_course_list);
                CheckBox Fall_CheckBox = findViewById(R.id.edit_fall_check);
                CheckBox Winter_CheckBox = findViewById(R.id.edit_winter_check);
                CheckBox Summer_CheckBox = findViewById(R.id.edit_summer_check);
                CheckBox Visible_CheckBox = findViewById(R.id.edit_visible_check);
                Button Add_Button = findViewById(R.id.edit_add_button);
                Button Delete_Button = findViewById(R.id.edit_delete_button);
                Button Save_Button = findViewById(R.id.edit_save_button);
                
                // To meet the requirement:
                // As an admin, I want to view the list of all courses and edit or
                // *delete* any course in the list so that I can keep the course information up to date.
                Button Delete_This = findViewById(R.id.edit_delete_this_button);

                ArrayList<String> Pre_List = new ArrayList<String>(course.preRequisiteCourses);

                Code_EditText.setText(course.courseCode);
                Name_EditText.setText(course.courseName);
                Description_EditText.setText(course.courseDescription);
                ArrayAdapter<String> PreList_Adapter = new ArrayAdapter<String>(AdminEdit_Activity.this,
                        android.R.layout.simple_list_item_1, Pre_List);
                Pre_ListView.setAdapter(PreList_Adapter);
                Fall_CheckBox.setChecked(course.sessionOffered.fall);
                Winter_CheckBox.setChecked(course.sessionOffered.winter);
                Summer_CheckBox.setChecked(course.sessionOffered.summer);
                Visible_CheckBox.setChecked(course.visible);

                Delete_This.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Record the course code
                        String courseCode = course.courseCode;
                        // Delete the course from the database
                        myRef.child("Courses").child(courseCode).removeValue();
                        // Toast to show the course is deleted
                        Toast.makeText(AdminEdit_Activity.this, "Course Deleted: " + courseCode, Toast.LENGTH_SHORT).show();
                        // Go back to the admin page
                        Intent intent = new Intent(AdminEdit_Activity.this, AdminHome_Activity.class);
                        startActivity(intent);
                    }
                });

                Add_Button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String Add_Course = String.valueOf(Pre_EditText.getText()).toUpperCase();
                        if (Pre_List.contains(Add_Course)){
                            Toast.makeText(AdminEdit_Activity.this, "Course already added",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else if(Add_Course.equals(course.courseCode)){
                            Toast.makeText(AdminEdit_Activity.this,
                                    "A course cannot be its pre-requisite",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Pre_List.add(Add_Course);
                            Pre_ListView.setAdapter(PreList_Adapter);
                            Toast.makeText(AdminEdit_Activity.this, "Add course successful",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                Delete_Button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String Delete_Course = String.valueOf(Pre_EditText.getText()).toUpperCase();
                        if (!Pre_List.contains(Delete_Course)){
                            Toast.makeText(AdminEdit_Activity.this, "Course haven't been added",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Pre_List.remove(Delete_Course);
                            Pre_ListView.setAdapter(PreList_Adapter);
                            Toast.makeText(AdminEdit_Activity.this, "Remove course successful",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                Save_Button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String new_code = String.valueOf(Code_EditText.getText());
                        String new_name = String.valueOf(Name_EditText.getText());
                        String new_description = String.valueOf(Description_EditText.getText());
                        if (new_code.isEmpty() || new_name.isEmpty() || new_description.isEmpty()){
                            Toast.makeText(AdminEdit_Activity.this,
                                    "Text cannot be empty", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                if (!task.isSuccessful()) {
                                    Log.e("firebase", "----Error getting data----", task.getException());
                                    Toast.makeText(AdminEdit_Activity.this, "Error getting data",
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    for(String code: Pre_List){
                                        if (!task.getResult().child("DATABASE").child("COURSES").hasChild(code)) {
                                            Toast.makeText(AdminEdit_Activity.this,
                                                    "Course " + code + " does not exist",
                                                    Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                    }
                                    if (!course.courseCode.equals(new_code)){
                                        course.setVisible(false);
                                        Log.d("Course Disabled", course.toString());
                                        course.sendToDatabase();
                                        course.setCourseCode(new_code);
                                    }
                                    course.setCourseName(new_name);
                                    course.setCourseDescription(new_description);
                                    course.setPreRequisiteCourses(Pre_List);
                                    course.sessionOffered.setFall(Fall_CheckBox.isChecked());
                                    course.sessionOffered.setWinter(Winter_CheckBox.isChecked());
                                    course.sessionOffered.setSummer(Summer_CheckBox.isChecked());
                                    course.setVisible(Visible_CheckBox.isChecked());
                                    Log.d("After Edit",course.toString());
                                    Log.d("After Edit", course.courseDescription);
                                    Log.d("After Edit", String.valueOf(course.visible));
                                    course.sendToDatabase();
                                    Toast.makeText(AdminEdit_Activity.this, "Changes Applied",
                                            Toast.LENGTH_SHORT).show();
                                    AdminEdit_Activity.this.finish();
                                }
                            }
                        });
                    }
                });
            }
        });
    }

}