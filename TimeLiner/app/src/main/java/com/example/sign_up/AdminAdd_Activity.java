package com.example.sign_up;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class AdminAdd_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_add_page);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        Course course = new Course();
        ArrayList<String> Pre_List = new ArrayList<String>();

        EditText Code_EditText = findViewById(R.id.new_code_text);
        EditText Name_EditText = findViewById(R.id.new_name_text);
        EditText Description_EditText = findViewById(R.id.new_descript_text);
        EditText Pre_EditText = findViewById(R.id.new_pre_text);
        ListView Pre_ListView = findViewById(R.id.new_pre_course_list);
        CheckBox Fall_CheckBox = findViewById(R.id.new_fall_check);
        CheckBox Winter_CheckBox = findViewById(R.id.new_winter_check);
        CheckBox Summer_CheckBox = findViewById(R.id.new_summer_check);
        Button Add_Button = findViewById(R.id.new_add_button);
        Button Delete_Button = findViewById(R.id.new_delete_button);
        Button Save_Button = findViewById(R.id.new_save_button);
        ArrayAdapter<String> PreList_Adapter = new ArrayAdapter<String>(AdminAdd_Activity.this,
                android.R.layout.simple_list_item_1, Pre_List);
        Pre_ListView.setAdapter(PreList_Adapter);

        Add_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Add_Course = String.valueOf(Pre_EditText.getText()).toUpperCase();
                if (Add_Course.isEmpty()){
                    Toast.makeText(AdminAdd_Activity.this, "Empty text",
                            Toast.LENGTH_SHORT).show();
                } else if (Pre_List.contains(Add_Course)) {
                    Toast.makeText(AdminAdd_Activity.this, "Course already added",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Pre_List.add(Add_Course);
                    Pre_ListView.setAdapter(PreList_Adapter);
                    Toast.makeText(AdminAdd_Activity.this, "Add course successful",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        Delete_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Delete_Course = String.valueOf(Pre_EditText.getText()).toUpperCase();
                if (Delete_Course.isEmpty()){
                    Toast.makeText(AdminAdd_Activity.this, "Empty text",
                            Toast.LENGTH_SHORT).show();
                } else if (!Pre_List.contains(Delete_Course)) {
                    Toast.makeText(AdminAdd_Activity.this, "Course haven't been added",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Pre_List.remove(Delete_Course);
                    Pre_ListView.setAdapter(PreList_Adapter);
                    Toast.makeText(AdminAdd_Activity.this, "Remove course successful",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        Save_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String new_code = String.valueOf(Code_EditText.getText()).toUpperCase();
                String new_name = String.valueOf(Name_EditText.getText());
                String new_description = String.valueOf(Description_EditText.getText());
                if (new_code.isEmpty() || new_name.isEmpty() || new_description.isEmpty()){
                    Toast.makeText(AdminAdd_Activity.this,
                            "Text cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                // check if course has itself as pre-requisite
                if (Pre_List.contains(new_code)){
                    Toast.makeText(AdminAdd_Activity.this,
                            new_code + "cannot be a pre-requisite", Toast.LENGTH_SHORT).show();
                    return;
                }

                myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            Log.e("firebase", "----Error getting data----", task.getException());
                            Toast.makeText(AdminAdd_Activity.this, "Error getting data",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // check if course is not in database or not visible
                            if (task.getResult().child("DATABASE").child("COURSES").hasChild(new_code)){
                                Course Identical_Course = task.getResult().child("DATABASE")
                                        .child("COURSES").child(new_code).getValue(Course.class);
                                if (Identical_Course.visible){
                                    Toast.makeText(AdminAdd_Activity.this,
                                            "Course " + new_code + " already exists",
                                            Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                            // check if all pre-requisites are in database & visible
                            for(String code: Pre_List){
                                if (task.getResult().child("DATABASE").child("COURSES").hasChild(code)){
                                    Course Pre_Course = task.getResult().child("DATABASE")
                                            .child("COURSES").child(code).getValue(Course.class);
                                    if (!Pre_Course.visible){
                                        Toast.makeText(AdminAdd_Activity.this,
                                                "Course " + code + " does not exist",
                                                Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                } else {
                                    Toast.makeText(AdminAdd_Activity.this,
                                            "Course " + code + " does not exist",
                                            Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }

                            course.setCourseCode(new_code);
                            course.setCourseName(new_name);
                            course.setCourseDescription(new_description);
                            course.setPreRequisiteCourses(Pre_List);
                            course.sessionOffered.setFall(Fall_CheckBox.isChecked());
                            course.sessionOffered.setWinter(Winter_CheckBox.isChecked());
                            course.sessionOffered.setSummer(Summer_CheckBox.isChecked());
                            course.setVisible(true);
                            Log.d("New Course",course.toString());
                            Log.d("New Course", course.courseDescription);
                            Log.d("New Course", course.sessionOffered.toString());
                            Log.d("New Course", String.valueOf(course.visible));
                            course.sendToDatabase();
                            Toast.makeText(AdminAdd_Activity.this, "New Course Added",
                                    Toast.LENGTH_SHORT).show();
                            AdminAdd_Activity.this.finish();
                        }
                    }
                });
            }
        });
    }
}