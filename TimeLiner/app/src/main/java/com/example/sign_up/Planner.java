package com.example.sign_up;

import androidx.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.Task;
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
    AcademicYear planner;


    public Planner() {
        courseTaken = new ArrayList<String>();
        planner = new AcademicYear();
    }

    public void addCourseTaken(String courseCode) {
        courseTaken.add(courseCode);
    }

    public ArrayList<String> getCourseTaken() {
        return courseTaken;
    }

    public void setCourseTaken(ArrayList<String> courseTaken) {
        this.courseTaken = courseTaken;
    }

    private static String buildMsg(String msg) {
        StringBuilder buffer = new StringBuilder();

        final StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[4];
        buffer.append("[ ");
        buffer.append(Thread.currentThread().getName());
        buffer.append(": ");
        buffer.append(stackTraceElement.getFileName());
        buffer.append(": ");
        buffer.append(stackTraceElement.getLineNumber());
        buffer.append(": ");
        buffer.append(stackTraceElement.getMethodName());


        buffer.append("() ] _____ ");

        buffer.append(msg);

        return buffer.toString();
    }

    public ArrayList<String> getTimetableList(int year) {
        ArrayList<String> j = new ArrayList<>();
        for (int i = 0; i < planner.timeline.size(); i++) {
            String str = year + " Winter       ";
            if (!(planner.timeline.get(i).winter.size() == 0)) {
                for (int k = 0; k < planner.timeline.get(i).winter.size(); k++) {
                    str += planner.timeline.get(i).winter;
                    str += " ";
                }
                Log.d("InsidePlanner:", "add a winter semester" + str);
            }
            j.add(str);

            str = year + " Summer  ";
            if (!(planner.timeline.get(i).summer.size() == 0)) {
                for (int k = 0; k < planner.timeline.get(i).summer.size(); k++) {
                    str = str + planner.timeline.get(i).summer;
                    str = str + " ";
                }
                Log.d("InsidePlanner:", "add a summer semester" + str);
            }
            j.add(str);

            str = year + " Fall          ";
            if (!(planner.timeline.get(i).fall.size() == 0)) {
                for (int k = 0; k < planner.timeline.get(i).fall.size(); k++) {
                    str += planner.timeline.get(i).fall;
                    str += " ";
                }
                Log.d("InsidePlanner:", "add a fall semester" + str);
            }
            j.add(str);
            year++;
        }
        return j;
    }

    // add all courses to the Planner. For the initial input, int i = 0. int sem = 0 if next
    // semester is winter; sem = 1 if next semester is summer; and sem = 2 if next semester is
    // fall. Last edited by Yibai.
    // TODO: use courseList to find all prerequisite courses for a given course.
    public ArrayList<Integer> addAllCoursePlanner(Course course, int i, int sem,
                                                  ArrayList<Course> courseList) {
        //induction: course has prerequisite course(s).
        if (course.preRequisiteCourses.size() > 0) {
            ArrayList<Integer> lis = new ArrayList<Integer>();
            for (int j = 0; j < course.preRequisiteCourses.size(); j++) {
                //for each prerequisite course, check what time is available
                for (int k = 0; k < courseList.size(); k++) {
                    if (courseList.get(k).courseCode.contains(course.preRequisiteCourses.get(j))) {
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
        } else {
            //base case: course has no preRequisiteCourses. year i = 1, semester = sem
            return this.addNextSession(course, 1, sem);
        }
    }

    //given an arraylist of pairs of year and semesters; return the largest pair
    @NonNull
    public static ArrayList<Integer> findFurthestSession(@NonNull ArrayList<Integer> lis) {
        int year = 0;
        int sem = 0;
        for (int i = 0; i < lis.size() - 1; i = i + 2) {
            if (year == lis.get(i) && sem < lis.get(i + 1)) {
                sem = lis.get(i + 1);
            }
            if (year < lis.get(i)) {
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
    // the course, and add the course to the planner.
    // TOBETESTED
    public ArrayList<Integer> addNextSession(Course course, int i, int sem) {
        //initialize arraylist j
        ArrayList<Integer> j = new ArrayList<Integer>();

        //check if course is taken, or course is invisible. Both will return first year, sem.
        if (this.courseTaken.contains(course.courseCode) || !course.visible) {
            j.add(1);
            j.add(sem);
            return j;
        }

        //check if it is initial input i = 0. Return initial input.
        if (i == 0) {
            j.add(0);
            j.add(sem);
            return j;
        }

        // add to semester
        while (true) {
            //if planner year not sufficient, add more years
            int a  = planner.timeline.size();
            if (i > a) {
                planner.addAcademicYear(i - planner.timeline.size());
            }

            // check if course is offered in session sem, and have free space in planner
            // TODO: instead of 6, try use a variable so that it can be edited
            if (course.sessionOffered.winter && sem == 0 && planner.getYear(i).winter.size() <= 6) {
                //check if courseCode already in planner
                if (!planner.getYear(i).winter.contains(course.courseCode)) {
                    planner.getYear(i).winter.add(course.courseCode);
                }
                j.add(i);
                j.add(1);
                return j;
            }
            if (course.sessionOffered.summer && sem <= 1 && planner.getYear(i).winter.size() <= 6) {
                if (!planner.getYear(i).summer.contains(course.courseCode)) {
                    planner.getYear(i).summer.add(course.courseCode);
                }
                j.add(i);
                j.add(2);
                return j;
            }
            if (course.sessionOffered.fall && sem <= 2 && planner.getYear(i).winter.size() <= 6) {
                if (!planner.getYear(i).fall.contains(course.courseCode)) {
                    planner.getYear(i).fall.add(course.courseCode);
                }
                j.add(i + 1);
                j.add(0);
                return j;
            }
            //course is not accepted this year. Switch to next year.
            i++;
        }

    }

    // Main function to generate a planner. Last edited by Yibai.
    public void generatePlanner(ArrayList<Course> courseWanted, ArrayList<Course> courseList, int sem) {
        // Create a planner
        AcademicYear planner = new AcademicYear();

        // Generate a planner, with recursive method
        for (int i = 0; i < courseWanted.size(); i++) {
            this.addAllCoursePlanner(courseWanted.get(i), 0, sem, courseList);
        }
    }

    public Planner(ArrayList<Course> course_taken, ArrayList<Course> course_wanted, ArrayList<Course> course_list, int sem) {
        courseTaken = new ArrayList<>();
        if (!(course_taken.size() == 0)) {
            for (int i = 0; i < courseTaken.size(); i++) {
                courseTaken.add(course_taken.get(i).courseCode);
            }
        }
        planner = new AcademicYear();
        int a  = planner.timeline.size();
        this.generatePlanner(course_wanted, course_list, sem);
    }
}
