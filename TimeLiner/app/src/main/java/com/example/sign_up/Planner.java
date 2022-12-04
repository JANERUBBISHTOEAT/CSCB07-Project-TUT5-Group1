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
//import android.widget.Toast.makeText;
// TODO: I commented this because it shows error on committing, best, Yibai

public class Planner {
    ArrayList<String> courseTaken;
    ArrayList<String> courseWanted;
    AcademicYear planner;


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

    public ArrayList<String> getTimetableList(){
        ArrayList<String> j = new ArrayList<>();
        for(int i=0; i < planner.timeline.size(); i++){
            String str = "";
            if(!(planner.timeline.get(i).winter.size() == 0)){
                for(int k=0; k < planner.timeline.get(i).winter.size(); k++){
                    str += planner.timeline.get(i).winter;
                    str += " ";
                }
            }
            j.add(str);

            str = "";
            if(!(planner.timeline.get(i).summer.size() == 0)){
                for(int k=0; k < planner.timeline.get(i).summer.size(); k++){
                    str += planner.timeline.get(i).summer;
                    str += " ";
                }
            }
            j.add(str);

            str = "";
            if(!(planner.timeline.get(i).fall.size() == 0)){
                for(int k=0; k < planner.timeline.get(i).fall.size(); k++){
                    str += planner.timeline.get(i).fall;
                    str += " ";
                }
            }
            j.add(str);
        }
        return j;
    }

    // add all courses to the Planner. For the initial input, int i = 0. int sem = 0 if next
    // semester is winter; sem = 1 if next semester is summer; and sem = 2 if next semester is
    // fall. Last edited by Yibai.
    // TODO: use courseList to find all prerequisite courses for a given course.
    public ArrayList<Integer> addAllCoursePlanner(Course course, int i, int sem,
                                                  ArrayList<Course> courseList){
        //induction: course has prerequisite course(s).
        if(course.preRequisiteCourses.size() > 0){
            ArrayList<Integer> lis = new ArrayList<Integer>();
            for(int j = 0; j<course.preRequisiteCourses.size(); j++){
                //for each prerequisite course, check what time is available
                for (int k = 0; k<courseList.size(); k++){
                    if (courseList.get(k).courseCode.contains(course.preRequisiteCourses.get(j))){
                        //save the returned value of the recursion to a list.
                        lis.addAll(this.addAllCoursePlanner(courseList.get(k), i, sem, courseList));
                    }
                }


            }
            //get furthest session from all prerequisite recursions.
            int year = findFurthestSession(lis).get(0);
            int semester = findFurthestSession(lis).get(1);
            //return the available time for course.
            return this.addNextSession(course, year, semester);
        }
        else{
            //base case: course has no preRequisiteCourses. year i = 1, semester = sem
            return this.addNextSession(course, 1, sem);
        }
    }

    //given an arraylist of pairs of year and semesters; return the largest pair
    @NonNull
    public static ArrayList<Integer> findFurthestSession(@NonNull ArrayList<Integer> lis){
        int year = 0;
        int sem = 0;
        for(int i = 0; i<lis.size()-1; i=i+2){
            if(year == lis.get(i) && sem < lis.get(i+1)){
                sem = lis.get(i+1);
            }
            if(year < lis.get(i)) {
                year = lis.get(i);
                sem = lis.get(i + 1);
            }
        }
        ArrayList<Integer> j = new ArrayList<Integer>();
        j.add(year);
        j.add(sem);
        return j;
    }

    //given a Course, a given year i, and a semester sem, find the next session available for
    // the course, and add the course to the planner. Last edited by Yibai.
    // TOBETESTED
    public ArrayList<Integer> addNextSession(Course course, int i, int sem){
        //initialize arraylist j
        ArrayList<Integer> j = new ArrayList<Integer>();

        //check if course is taken, or course is invisible. Both will return first year, sem.
        if(this.courseTaken.contains(course.courseCode) || !course.visible ){
            j.add(1);
            j.add(sem);
            return j;
        }

        //check if it is initial input i = 0. Return initial input.
        if(i == 0){
            j.add(0);
            j.add(sem);
            return j;
        }

        // add to semester
        while (true){
            //if planner year not sufficient, add more years
            if(i > planner.timeline.size()){
                planner.addAcademicYear(i-planner.timeline.size());
            }

            // check if course is offered in session sem, and have free space in planner
            // TODO: instead of 6, try use a variable so that it can be edited
            if(course.sessionOffered.winter && sem == 0 && planner.getYear(i).winter.size() <=6){
                //check if courseCode already in planner
                if(!planner.getYear(i).winter.contains(course.courseCode)) {
                    planner.getYear(i).winter.add(course.courseCode);
                }
                j.add(i);
                j.add(1);
                return j;
            }
            if(course.sessionOffered.summer && sem <= 1 && planner.getYear(i).winter.size() <=6){
                if(!planner.getYear(i).summer.contains(course.courseCode)) {
                    planner.getYear(i).summer.add(course.courseCode);
                }
                j.add(i);
                j.add(2);
                return j;
            }
            if(course.sessionOffered.fall && sem <= 2 && planner.getYear(i).winter.size() <=6) {
                if(!planner.getYear(i).fall.contains(course.courseCode)) {
                    planner.getYear(i).fall.add(course.courseCode);
                }
                j.add(i+1);
                j.add(0);
                return j;
            }
            //course is not accepted this year. Switch to next year.
            i++;
        }

    }

    // Main function to generate a planner. Last edited by Yibai.
    public AcademicYear generatePlanner(ArrayList<Course> courseTaken,
            ArrayList<Course> courseWanted, ArrayList<Course> courseList, int sem) {
        // Create a planner
        AcademicYear planner = new AcademicYear();

        // Generate a planner, with recursive method
        for(int i = 0; i < courseWanted.size(); i++){
            this.addAllCoursePlanner(courseWanted.get(i), 0, sem, courseList);
        }
        return planner;
        // Generate a planner, with list method. (Commented)
        /*
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
         */
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
                    planner = generatePlanner(course_taken, course_wanted, courses, sem);

                    // TODO: The planner is never used (warning)
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
