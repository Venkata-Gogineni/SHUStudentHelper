package com.example.android.shustudenthelper;

/**
 * Created by Surya Gogineni on 11/10/2016.
 */

public class AssignmentNotificationObject {

    private String courseName;
    private String assignmentName;
    private String assignmentDueDate;

    public AssignmentNotificationObject(String courseName, String assignmentName, String assignmentDueDate) {
        this.courseName = courseName;
        this.assignmentName = assignmentName;
        this.assignmentDueDate = assignmentDueDate;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getAssignmentName() {
        return assignmentName;
    }

    public void setAssignmentName(String assignmentName) {
        this.assignmentName = assignmentName;
    }

    public String getAssignmentDueDate() {
        return assignmentDueDate;
    }

    public void setAssignmentDueDate(String assignmentDueDate) {
        this.assignmentDueDate = assignmentDueDate;
    }


}
