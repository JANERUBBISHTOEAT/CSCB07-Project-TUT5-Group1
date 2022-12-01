package com.example.sign_up;

import androidx.annotation.NonNull;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

// import Course.java
import com.example.sign_up.Course;

// import toast, makeText, and LENGTH_SHORT
import android.widget.Toast;
import android.widget.Toast.makeText;

public class Planner {
    ArrayList<String> courseTaken;
    ArrayList<String> courseWanted;

    public Planner() {
        courseTaken = new ArrayList<String>();
        courseWanted = new ArrayList<String>();
    }

    public void addCourseTaken(String courseCode) {
        courseTaken.add(courseCode);
    }

    public void addCourseWanted(String courseCode) {
        courseWanted.add(courseCode);
    }

    public ArrayList<String> getCourseTaken() {
        return courseTaken;
    }

    public ArrayList<String> getCourseWanted() {
        return courseWanted;
    }

    public void setCourseTaken(ArrayList<String> courseTaken) {
        this.courseTaken = courseTaken;
    }

    public void setCourseWanted(ArrayList<String> courseWanted) {
        this.courseWanted = courseWanted;
    }

    // Main function to generate a planner
    public ArrayList<AcademicYear> generatePlanner(ArrayList<Course> courseTaken,
            ArrayList<Course> courseWanted, ArrayList<Course> courseList) {
        // Create a planner
        ArrayList<AcademicYear> planner = new ArrayList<AcademicYear>();

        // Generate a planner
        while (courseWanted.size() > 0) {
            for (int i = 0; i < courseWanted.size(); i++) {
                // Check if the course is taken
                if (courseTaken.contains(courseWanted.get(i))) {
                    // Add the course to the planner
                    // TODO: Add the course to the planner

                    // Remove the course from the wanted list
                    courseWanted.remove(i);
                }

                // Check if the course has a prerequisite
                else if (courseList.get(i).getPrerequisite().size() > 0) {
                    for (int j = 0; j < courseList.get(i).getPrerequisite().size(); j++) {
                        // Check if the prerequisite is taken
                        if (courseTaken.contains(courseList.get(i).getPrerequisite().get(j))) {
                            // Add the course to the planner
                            // TODO: Add the course to the planner

                            // Remove the course from the wanted list
                            courseWanted.remove(i);
                        } else {
                            // Add the prerequisite to the wanted list
                            courseWanted.add(courseList.get(i).getPrerequisite().get(j));

                            // Remove the course from the wanted list
                            courseWanted.remove(i);

                            // Add the course to the taken list
                            courseTaken.add(courseList.get(i).getCourseCode());

                            // Add the course to the planner
                            // TODO: Add the course to the planner
                        }
                    }
                }
            }
        }
    }

    public Planner(String username, int sem) {
        courseTaken = new ArrayList<String>();
        courseWanted = new ArrayList<String>();

        // Timeline is a AcademicYear object

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
                    for (DataSnapshot ds : snapshot.child("DATABASE").child("COURSE").getChildren()) {
                        courses.add(ds.getValue(Course.class));
                    }

                    // Get all courses taken by user
                    if (snapshot.child("STUDENTS").child(username).child("course_taken").exists()) {
                        for (DataSnapshot ds : snapshot.child("STUDENTS").child(username).child("course_taken")
                                .getChildren()) {
                            course_taken.add(ds.getValue(Course.class));
                        }
                    }

                    // Get all courses wanted by user
                    if (snapshot.child("STUDENTS").child(username).child("course_wanted").exists()) {
                        for (DataSnapshot ds : snapshot.child("STUDENTS").child(username).child("course_wanted")
                                .getChildren()) {
                            course_wanted.add(ds.getValue(Course.class));
                        }
                    }

                    // Plan courses
                    ArrayList<AcademicYear> planner = new ArrayList<AcademicYear>();
                    planner = generatePlanner(course_taken, course_wanted, courses);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Database Error", error.getMessage());
                // Toast.makeText(Planner_Activity.this, "Database Error",
                // Toast.LENGTH_SHORT).show();
            }
        });
    }
}
