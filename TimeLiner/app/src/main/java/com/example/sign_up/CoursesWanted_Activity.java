package com.example.sign_up;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

import java.util.ArrayList;

public class CoursesWanted_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wish_course_page);

        String studentID = "test"; // set id for test
        ArrayList<String> Keys_List = new ArrayList<String>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        Button AddWish_Button = findViewById(R.id.wishlist_add);
        Button DeleteWish_Button = findViewById(R.id.wishlist_delete);
        Button Back_Button = findViewById(R.id.backto_courses_taken);
        EditText Wish_EditText = findViewById(R.id.wishlist_edit_text);

        myRef.child("DATABASE").child("STUDENTS").child(studentID).child("course_want")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Keys_List.clear();
                        for (DataSnapshot key : snapshot.getChildren()) {
                            Keys_List.add(key.getKey());
                            Log.d("Key Added", Keys_List.toString());
                        }
                        showCourse(myRef, Keys_List, studentID);

                        AddWish_Button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String Add_Course = String.valueOf(Wish_EditText.getText()).toUpperCase();
                                addCourse(myRef, Add_Course, studentID, Keys_List);
                            }
                        });

                        DeleteWish_Button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String Delete_Course = String.valueOf(Wish_EditText.getText()).toUpperCase();
                                deleteCourse(myRef, Delete_Course, studentID, Keys_List);
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

    private void showCourse(DatabaseReference database, ArrayList<String> keys, String id){
        database.child("DATABASE").child("COURSES").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> CourseWanted_List = new ArrayList<String>();
                for (DataSnapshot key: snapshot.getChildren()) {
                    if (keys.contains(key.getKey())) {
                        if (!(boolean)key.child("visible").getValue()){
                            AlertDialog.Builder dialog =
                                    new AlertDialog.Builder(CoursesWanted_Activity.this);
                            dialog.setTitle("Notice: Course Cancelled");
                            dialog.setMessage(key.getKey() + " has been cancelled.");
                            dialog.setCancelable(false);
                            dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //keys.remove(key.getKey());
                                    //Log.d("Dialog Clicked", keys.toString());
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

    private void addCourse(DatabaseReference database, String course, String id, ArrayList<String> keys){
        if (keys.contains(course)){
            Toast.makeText(CoursesWanted_Activity.this, "Course already added",
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
                    if (!toCheck.visible){
                        Toast.makeText(CoursesWanted_Activity.this,
                                "Course " + course + " does not exist", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    //keys.add(course);
                    //Log.d("Course Added", keys.toString());
                    database.child("DATABASE").child("STUDENTS").child(id).child("course_want")
                            .child(course).setValue(course);
                    Toast.makeText(CoursesWanted_Activity.this, "Add course successful",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void deleteCourse(DatabaseReference database, String course, String id, ArrayList<String> keys){
        if (!keys.contains(course)){
            Toast.makeText(CoursesWanted_Activity.this, "Course haven't been added",
                    Toast.LENGTH_SHORT).show();
        }
        else{
            //keys.remove(course);
            //Log.d("Course Removed", keys.toString());
            database.child("DATABASE").child("STUDENTS").child(id).child("course_want")
                    .child(course).removeValue();
            Toast.makeText(CoursesWanted_Activity.this, "Remove course successful",
                    Toast.LENGTH_SHORT).show();
        }
    }
}