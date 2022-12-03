package com.example.sign_up;

import java.util.ArrayList;

// Import getApplicationContext

// Import Session


public class AcademicYear {
    
    
    public class Year{
        ArrayList<String> winter;
        ArrayList<String> summer;
        ArrayList<String> fall;
        
        public Year(){
            winter = new ArrayList<String>();
            summer = new ArrayList<String>();
            fall = new ArrayList<String>();
        }
        
        public void addCourse(String courseCode, String semester){
            if(semester.equals("Winter")){
                winter.add(courseCode);
            }
            else if(semester.equals("Summer")){
                summer.add(courseCode);
            }
            else if(semester.equals("Fall")){
                fall.add(courseCode);
            }
        }

        public ArrayList<String> getSession(int i){
            if (i == 0){
                return winter;
            }
            else if (i == 1){
                return summer;
            }
            return fall;
        }

        public void setWinter(ArrayList<String> winter) {
            this.winter = winter;
        }

        public void setSummer(ArrayList<String> summer) {
            this.summer = summer;
        }

        public void setFall(ArrayList<String> fall) {
            this.fall = fall;
        }
    }

    ArrayList<Year> timeline;

    public AcademicYear(){
        timeline = new ArrayList<Year>();
        timeline.add(new Year());
    }

    public void addCourse(String courseCode, int year, String semester){
        timeline.get(year).addCourse(courseCode, semester);
    }

    public Year getYear(int year){
        return timeline.get(year-1);
    }

    public ArrayList<Year> getTimeline() {
        return timeline;
    }

    public void addAcademicYear(int i){
        for(int j = 0; j<i; j++) {
            this.timeline.add(new Year());
        }
    }
}
