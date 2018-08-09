package com.example.android.shustudenthelper;

import static android.R.attr.checked;
import static android.R.attr.value;

/**
 * Created by Surya Gogineni on 10/10/2016.
 */

public class CourseDisplay{
    private String courseCode;
    private String courseName;
    private String courseProfessor;
    private String courseCLA;
    private String courseTimings;
    private String courseClassRoom;
    private String courseSelected;

    public CourseDisplay(String courseName){
        this.courseName = courseName;
    }

    public CourseDisplay(String courseCode, String courseName, String courseSelected) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.courseSelected = courseSelected;
    }

    public CourseDisplay(String courseName, String courseCode, String courseProfessor, String courseCLA, String courseTimings, String courseClassRoom) {

        this.courseCode = courseCode;
        this.courseName = courseName;
        this.courseProfessor = courseProfessor;
        this.courseCLA = courseCLA;
        this.courseTimings = courseTimings;
        this.courseClassRoom = courseClassRoom;
    }

    public String getCourseProfessor() {
        return courseProfessor;
    }

    public String getCourseCLA() {
        return courseCLA;
    }

    public String getCourseTimings() {
        return courseTimings;
    }

    public String getCourseClassRoom() {
        return courseClassRoom;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getCourseSelected() {
        return courseSelected;
    }

    public void setCourseSelected(String courseSelected) {
        this.courseSelected = courseSelected;
    }
}