package com.example.android.shustudenthelper;

/**
 * Created by Surya Gogineni on 10/22/2016.
 */

public class AssignmentDisplay {
    String assignmentName;
    String assignmentLateSub;
    String assignmentPenalty;

    public AssignmentDisplay(String assignmentName) {
        this.assignmentName = assignmentName;
    }

    public String getAssignmentName() {
        return assignmentName;
    }
}
