package com.example.sign_up;

import static android.app.PendingIntent.getActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CoursesTaken_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_page);

        String studentID = "student_0"; // set id for test
        ArrayList<String> Keys_List = new ArrayList<String>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        Button AddTaken_Button = findViewById(R.id.taken_add_button);
        Button DeleteTaken_Button = findViewById(R.id.taken_delete_button);
        EditText Taken_EditText = findViewById(R.id.taken_edit_text);

        myRef.child("DATABASE").child("STUDENTS").child(studentID).child("course_taken")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Keys_List.clear();
                        for (DataSnapshot key : snapshot.getChildren()) {
                            Keys_List.add(key.getKey());
                            Log.d("Key Added", Keys_List.toString());
                        }
                        showCourse(myRef, Keys_List, studentID);

                        AddTaken_Button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String Add_Course = String.valueOf(Taken_EditText.getText()).toUpperCase();
                                addCourse(myRef, Add_Course, studentID, Keys_List);
                            }
                        });

                        DeleteTaken_Button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String Delete_Course = String.valueOf(Taken_EditText.getText()).toUpperCase();
                                deleteCourse(myRef, Delete_Course, studentID, Keys_List);
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }

    private void showCourse(DatabaseReference database, ArrayList<String> keys, String id){
        database.child("DATABASE").child("COURSES").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> CourseTaken_List = new ArrayList<String>();
                for (DataSnapshot key: snapshot.getChildren()) {
                    if (keys.contains(key.getKey())) {
                        if (!(boolean)key.child("visible").getValue()){
                            AlertDialog.Builder dialog =
                                    new AlertDialog.Builder(CoursesTaken_Activity.this);
                            dialog.setTitle("Notice: Course Cancelled");
                            dialog.setMessage(key.getKey() + " has been cancelled.");
                            dialog.setCancelable(false);
                            dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //keys.remove(key.getKey());
                                    //Log.d("Dialog Clicked", keys.toString());
                                    database.child("DATABASE").child("STUDENTS").child(id)
                                            .child("course_taken").child(key.getKey()).removeValue();
                                }
                            });
                            dialog.show();
                            return;
                        }
                        String Course_Name = key.child("courseName").getValue().toString();
                        CourseTaken_List.add(key.getKey() + "-" + Course_Name);
                    }
                }
                ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(CoursesTaken_Activity.this,
                        android.R.layout.simple_list_item_1, CourseTaken_List);
                ListView CourseTaken_Listview = findViewById(R.id.course_taken_view);
                CourseTaken_Listview.setAdapter(itemsAdapter);
                Log.d("CourseList", CourseTaken_List.toString());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void addCourse(DatabaseReference database, String course, String id, ArrayList<String> keys){
        if (keys.contains(course)){
            Toast.makeText(CoursesTaken_Activity.this, "Course already added",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        database.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "----Error getting data----", task.getException());
                    Toast.makeText(CoursesTaken_Activity.this, "Error getting data",
                            Toast.LENGTH_SHORT).show();
                } else if (!task.getResult().child("DATABASE").child("COURSES").hasChild(course)) {
                        Toast.makeText(CoursesTaken_Activity.this,
                                "Course " + course + " does not exist", Toast.LENGTH_SHORT).show();
                } else {
                    Course toCheck = task.getResult().child("DATABASE").child("COURSES")
                            .child(course).getValue(Course.class);
                    if (!toCheck.visible){
                        Toast.makeText(CoursesTaken_Activity.this,
                                "Course " + course + " does not exist", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    //keys.add(course);
                    //Log.d("Course Added", keys.toString());
                    database.child("DATABASE").child("STUDENTS").child(id).child("course_taken")
                            .child(course).setValue(course);
                    Toast.makeText(CoursesTaken_Activity.this, "Add course successful",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void deleteCourse(DatabaseReference database, String course, String id, ArrayList<String> keys){
        if (!keys.contains(course)){
            Toast.makeText(CoursesTaken_Activity.this, "Course haven't been added",
                    Toast.LENGTH_SHORT).show();
        }
        else{
            //keys.remove(course);
            //Log.d("Course Removed", keys.toString());
            database.child("DATABASE").child("STUDENTS").child(id).child("course_taken")
                    .child(course).removeValue();
            Toast.makeText(CoursesTaken_Activity.this, "Remove course successful",
                    Toast.LENGTH_SHORT).show();
        }
    }

}