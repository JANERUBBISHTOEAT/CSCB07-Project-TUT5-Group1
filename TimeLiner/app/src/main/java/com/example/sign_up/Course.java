package com.example.sign_up;

// Import ArrayList, DatabaseReference, FirebaseDatabase, Log, Toast, getValue
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import android.util.Log;
import android.widget.Toast;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

// Import getApplicationContext
import android.content.Context;

// Import Session
import androidx.annotation.NonNull;

import com.example.sign_up.Session;

// Create a java course object

public class Course {
    String courseName;
    String courseCode;
    String courseDescription;
    boolean visible;

    // Prerequisite Courses List
    ArrayList<String> preRequisiteCourses = new ArrayList<String>();

    // Session Offered List
    Session sessionOffered = new Session();

    // Empty Constructor
    public Course() {
    }

    // Context
    Context context;

    public Course(Context context) {
        this.context = context;
    }

    // Constructor
    public Course(String courseName, String courseCode, String courseDescription, boolean visible) {
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.courseDescription = courseDescription;
        this.visible = visible;
    }

    // Getters and Setters
    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseDescription() {
        return courseDescription;
    }

    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    }

    public ArrayList<String> getPreRequisiteCourses() {
        return preRequisiteCourses;
    }

    public void setPreRequisiteCourses(ArrayList<String> preRequisiteCourses) {
        this.preRequisiteCourses = preRequisiteCourses;
    }

    // Add a prerequisite course
    public void addPreRequisiteCourse(String course) {
        preRequisiteCourses.add(course);
    }

    // Remove a prerequisite course
    public void removePreRequisiteCourse(String course) {
        preRequisiteCourses.remove(course);
    }

    // Check if a course has a prerequisite course
    public boolean hasPreRequisiteCourse(String course) {
        return preRequisiteCourses.contains(course);
    }

    // Get the list of sessions offered
    public Session getSessionOffered() {
        return sessionOffered;
    }

    // Set the list of sessions offered
    public void setSessionOffered(Session session) {
        this.sessionOffered = session;
    }

    // Add a session offered
    public void addSessionOffered(String session) {
        if (session.equals("winter")) {
            sessionOffered.setWinter(true);
        } else if (session.equals("summer")) {
            sessionOffered.setSummer(true);
        } else if (session.equals("fall")) {
            sessionOffered.setFall(true);
        }
    }

    // Remove a session offered
    public void removeSessionOffered(String session) {
        if (session.equals("winter")) {
            sessionOffered.setWinter(false);
        } else if (session.equals("summer")) {
            sessionOffered.setSummer(false);
        } else if (session.equals("fall")) {
            sessionOffered.setFall(false);
        }
    }

    // Check if a course is offered in a session
    public boolean isOfferedInSession(String session) {
        if (session.equals("winter")) {
            return sessionOffered.getWinter();
        } else if (session.equals("summer")) {
            return sessionOffered.getSummer();
        } else if (session.equals("fall")) {
            return sessionOffered.getFall();
        }
        return false;
    }

    // Set the visibility of the course
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    // Check if a course is visible
    public boolean isVisible() {
        return visible;
    }

    // Log a course object
    public void logCourse() {
        Log.i("Course", "Course Name: " + courseName);
        Log.i("Course", "Course Code: " + courseCode);
        Log.i("Course", "Course Description: " + courseDescription);
        Log.i("Course", "Prerequisite Courses: " + preRequisiteCourses);
        Toast.makeText(context.getApplicationContext(), "Course Name: " + courseName, Toast.LENGTH_SHORT).show();
    }

    // toString method
    @Override
    public String toString() {
        String ln1 = this.courseCode + " - " + this.courseName;
        String ln2 = "Session Offered: " + this.sessionOffered.toString();
        String ln3 = "Pre-requisites: " + preRequisiteCourses.toString();
        return ln1 + "\n" + ln2+ "\n" + ln3;
    }

    // equals method
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Course other = (Course) obj;
        if (courseCode == null) {
            if (other.courseCode != null)
                return false;
        } else if (!courseCode.equals(other.courseCode))
            return false;
        if (courseName == null) {
            if (other.courseName != null)
                return false;
        } else if (!courseName.equals(other.courseName))
            return false;
        if (preRequisiteCourses == null) {
            if (other.preRequisiteCourses != null)
                return false;
        } else if (!preRequisiteCourses.equals(other.preRequisiteCourses))
            return false;
        return true;
    }

    // Send a course object to the database
    public void sendToDatabase() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("DATABASE").child("COURSES").child(courseCode).setValue(this);
    }

    /* Approach 1 */
    /* Does not work */
    // // Get a course object from the database and return it
    // public Course getCourseFromDatabase(String courseCode) throws InterruptedException {
    //     Semaphore semaphore = new Semaphore(0);

    //     DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    //     databaseReference.child("DATABASE").child("COURSES").child(courseCode).addListenerForSingleValueEvent(new ValueEventListener() {
    //         @Override
    //         public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
    //             Course course = dataSnapshot.getValue(Course.class);
    //             semaphore.release();
    //         }

    //         @Override
    //         public void onCancelled(@NonNull DatabaseError databaseError) {
    //             Log.i("Course", "Failed to read value.");
    //         }
    //     });
    //     semaphore.acquire();
    //     return null;
    // }

    /* Approach 2 */
    /* Does not work */
    // // Define a custom callback
    // public interface MyCallback {
    //     void onCallback(Course course);
    // }

    // // Implement the callback function
    // public void onCallback(Course course) {
    //     // set the course object
    //     this.courseName = course.getCourseName();
    //     this.courseCode = course.getCourseCode();
    //     this.courseDescription = course.getCourseDescription();
    //     this.preRequisiteCourses = course.getPreRequisiteCourses();
    //     this.sessionOffered = course.getSessionOffered();
    //     this.visible = course.isVisible();

    //     // // log the course object
    //     // logCourse();
    // }


    // // Get a course object from the database and return it
    // public void getFromDatabase(String courseCode, final MyCallback myCallback) {

    //     DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    //     databaseReference.child("DATABASE").child("COURSES").child(courseCode)
    //             .addListenerForSingleValueEvent(new ValueEventListener() {
    //                 @Override
    //                 public void onDataChange(DataSnapshot dataSnapshot) {
    //                     Course course = dataSnapshot.getValue(Course.class);
    //                     Log.i("Course", "Course Name: " + course.getCourseName());
    //                     Log.i("Course", "Course Code: " + course.getCourseCode());
    //                     Log.i("Course", "Course Description: " + course.getCourseDescription());
    //                     Log.i("Course", "Prerequisite Courses: " + course.getPreRequisiteCourses());
    //                     Log.i("Course", "Sessions Offered: " + course.getSessionOffered().getFall() + " "
    //                             + course.getSessionOffered().getSummer() + " " + course.getSessionOffered().getWinter());
    //                     Log.i("Course", "Visible: " + course.isVisible());
                        
    //                     // course_return.setCourseCode(courseCode);
    //                     // course_return.setCourseName(course.getCourseName());
    //                     // course_return.setCourseDescription(course.getCourseDescription());
    //                     // course_return.setPreRequisiteCourses(course.getPreRequisiteCourses());
    //                     // course_return.setSessionOffered(course.getSessionOffered());
    //                     // course_return.setVisible(course.isVisible());

    //                     myCallback.onCallback(course);

    //                 }

    //                 @Override
    //                 public void onCancelled(DatabaseError databaseError) {
    //                     Log.i("Course", "Error: " + databaseError.getMessage());
    //                 }
    //             });
    //         return ;
    // }
}
