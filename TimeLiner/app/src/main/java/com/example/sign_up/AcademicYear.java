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

        public ArrayList<String> getWinter() {
            return winter;
        }

        public ArrayList<String> getSummer() {
            return summer;
        }

        public ArrayList<String> getFall() {
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

    public ArrayList<String> getYear(int year){
        return timeline.get(year);
    }

    public ArrayList<Year> getTimeline() {
        return timeline;
    }
}
