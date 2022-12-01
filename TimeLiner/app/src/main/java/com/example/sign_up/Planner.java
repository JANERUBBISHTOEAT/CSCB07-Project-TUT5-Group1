package com.example.sign_up;

import androidx.annotation.NonNull;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Planner {
    ArrayList<AcademicYear> timeline;
    ArrayList<String> courseTaken;
    ArrayList<String> courseWanted;

    // getTimetableList() returns a list of all the courses in the timetable
    public ArrayList<String> getTimetableList() {
        ArrayList<String> timetableList = new ArrayList<String>();
        for (int i = 0; i < timeline.size(); i++) {
            AcademicYear year = timeline.get(i);
            timetableList.addAll(year.winter);
            timetableList.addAll(year.summer);
            timetableList.addAll(year.fall);
        }
        return timetableList;
    }

    //create an empty planner.
    public Planner(){
        timeline = new ArrayList<AcademicYear>();
        timeline.add(new AcademicYear());
        courseTaken = new ArrayList<String>();
        courseWanted = new ArrayList<String>();
    }

    //how many years estimate to be taken.
    public void addAcademicYear(int i){
        for(int j = 0; j<i; j++) {
            timeline.add(new AcademicYear());
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

    //add all taken courses to the courseTaken arraylist of the planner.
    public void addCourseTaken(String user){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("DATABASE").child("STUDENTS").child(user);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists() && snapshot.hasChild("course_taken")){
                    courseTaken.clear();
                    for(DataSnapshot dss: snapshot.child("course_taken").getChildren()){
                        String t = dss.getValue(String.class);
                        courseTaken.add(t);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //add all wanted courses to the courseWanted arraylist of the planner.
    public void addCourseWanted(String user){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("DATABASE").child("STUDENTS").child(user);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists() && snapshot.hasChild("course_want")){
                    courseWanted.clear();
                    for(DataSnapshot dss: snapshot.child("course_want").getChildren()){
                        String t = dss.getValue(String.class);
                        courseWanted.add(t);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    //given a Course, a given year i, and a semester sem, find the next session available for
    // the course, and add the course to the planner.
    public ArrayList<Integer> addNextSession(Course course, int i, int sem){
        //initialize arraylist j
        ArrayList<Integer> j = new ArrayList<Integer>();

        //check if course is taken, or course is invisible. Both will return first year, sem.
        if(this.courseTaken.contains(course.courseCode) || !course.visible){
            j.add(1);
            j.add(sem);
        }
        //i = 0 if it is initial input. Return initial input.
        if(i == 0){
            j.add(i);
            j.add(sem);
            return j;
        }
        //if planner year not sufficient, add more years
        if(i > this.timeline.size()){
            this.addAcademicYear(i-this.timeline.size());
        }
        //add to semester
        if(course.sessionOffered.winter && sem == 0){
            //check if courseCode already in planner
            if(!this.timeline.get(i).winter.contains(course.courseCode)) {
                this.timeline.get(i).winter.add(course.courseCode);
            }
            j.add(i);
            j.add(1);
        } else if(course.sessionOffered.summer && sem <= 1){
            if(!this.timeline.get(i).summer.contains(course.courseCode)) {
                this.timeline.get(i).summer.add(course.courseCode);
            }
            j.add(i);
            j.add(2);
        } else if(course.sessionOffered.fall && sem <= 2) {
            if(!this.timeline.get(i).fall.contains(course.courseCode)) {
                this.timeline.get(i).fall.add(course.courseCode);
            }
            j.add(i+1);
            j.add(0);
        }
        return j;
    }


    //add all courses to the Planner. For the initial input, int i = 0. int sem = 0 if next
    // semester is winter; sem = 1 if next semester is summer; and sem = 2 if next semester is
    // fall.
    public ArrayList<Integer> addAllCoursePlanner(Course course, int i, int sem){
        //induction: course has prerequisite course(s).
        if(course.preRequisiteCourses.size() > 0){
            ArrayList<Integer> lis = new ArrayList<Integer>();
            for(int j = 0; j<course.preRequisiteCourses.size(); j++){
                //for each prerequisite course c, check what time is available
                Course c = new Course();
                c.getFromDatabase(course.preRequisiteCourses.get(i), new Course.MyCallback() {
                    @Override
                    public void onCallback(Course course_callback) {
                        c.setCourseName(course_callback.getCourseName());
                        c.setCourseCode(course_callback.getCourseCode());
                        c.setCourseDescription(course_callback.getCourseDescription());
                        c.setSessionOffered(course_callback.getSessionOffered());
                        c.setPreRequisiteCourses(course_callback.getPreRequisiteCourses());
                        c.setVisible(course_callback.isVisible());
                    }
                });
                lis.addAll(this.addAllCoursePlanner(c, i, sem));
            }
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

    //give username of a student, the next semester sem which 0,1,2 represent winter, summer,fall
    // respectively.
    public Planner(String username, int sem){
        timeline = new ArrayList<AcademicYear>();
        timeline.add(new AcademicYear());
        courseTaken = new ArrayList<String>();
        courseWanted = new ArrayList<String>();
        this.addCourseTaken(username);
        this.addCourseWanted(username);
        for(String s: this.courseWanted){
            Course c = new Course();
            c.getFromDatabase(s, new Course.MyCallback() {
                @Override
                public void onCallback(Course course_callback) {
                    c.setCourseName(course_callback.getCourseName());
                    c.setCourseCode(course_callback.getCourseCode());
                    c.setCourseDescription(course_callback.getCourseDescription());
                    c.setSessionOffered(course_callback.getSessionOffered());
                    c.setPreRequisiteCourses(course_callback.getPreRequisiteCourses());
                    c.setVisible(course_callback.isVisible());
                }
            });

            this.addAllCoursePlanner(c, 1, sem);
        }
    }
}
