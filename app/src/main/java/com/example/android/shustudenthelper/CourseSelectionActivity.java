package com.example.android.shustudenthelper;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.SQLException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.media.CamcorderProfile.get;


public class CourseSelectionActivity extends AppCompatActivity {

    //New instances to register with real ID
    private ListView myListView;
    public static Button confirm_Button;

    //Adapter Instance
    public static CourseSelectionAdapter newAdapter;

    //New variables of List type
    public static List<AssignmentNotificationObject> listOfSingleCourseDetailsForNotifications = new ArrayList<AssignmentNotificationObject>();
    public static List<AssignmentNotificationObject> listOfAllCourseDetailsForNotifications = new ArrayList<AssignmentNotificationObject>();

    //Database Instances
    DatabaseOpenHelper myDbHelper;
    DatabaseOpenHelper newDatabaseConForNotifications;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_selection);//register the layout

        myDbHelper = new DatabaseOpenHelper(this);
        newDatabaseConForNotifications = new DatabaseOpenHelper(this);

        try {
            // check if database exists in app path, if not copy it from assets
            if(myDbHelper.checkDataBase() == false) {
                myDbHelper.create();
            }
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }
        try {
            // open the database
            myDbHelper.openDataBase();
            myDbHelper.getWritableDatabase();
        } catch (SQLException sqle) {
            throw sqle;
        }
        // close the database
        myDbHelper.close();

        //Display in ListView
        populateListView();

        //button click listener
        onClickConfirmButtonListener();

    }//end OnCreate method

    /*Method to populate list view from database*/
    private void populateListView(){

        DatabaseOpenHelper myDbHelper2 = new DatabaseOpenHelper(this);//New Object of Database

        List<CourseDisplay> courseDisplay_data = new ArrayList<CourseDisplay>();

        courseDisplay_data = myDbHelper2.getAllCoursesForRegistration();

        //set the layout, data into adapter to handle the list view
        newAdapter = new CourseSelectionAdapter(this,R.layout.custom_listview_layout,courseDisplay_data);
        //register the list view
        myListView = (ListView)findViewById(R.id.listview_courses);
        myListView.setChoiceMode(2);
        //set adapter to list view to populate
        myListView.setAdapter(newAdapter);

        }//end populate list view method

    /* On click listener method implementation*/
    public void onClickConfirmButtonListener() {

        /* Register the instance with real ID*/
        confirm_Button = (Button) findViewById(R.id.course_register_button);

        /* set listener on button click* and handle the user selection*/
        confirm_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!(CourseSelectionAdapter.checkBoxCounter  < 1) && CourseSelectionAdapter.checkBoxCounter <= 4) {
                    showResult();//Show the courses selected.

                    Toast.makeText(CourseSelectionActivity.this, "You are directed to Notifications", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent("com.example.android.shustudenthelper.NotificationSetupActivity");
                    startActivity(intent);
                }
                else if (CourseSelectionAdapter.checkBoxCounter > 4){
                    Toast.makeText(CourseSelectionActivity.this, "You cannot select more than 4 courses", Toast.LENGTH_SHORT).show();
                }else if (CourseSelectionAdapter.checkBoxCounter < 1){
                    Toast.makeText(CourseSelectionActivity.this, "You need to select at least one course", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    /*Method to display the selected courses through toast*/
    private void showResult() {

        int size = newAdapter.getBox().size();
        System.out.println("Size is: " + size);
        String result = "Selected Courses are :";

        for (CourseDisplay p : newAdapter.getBox()) {
            if (p.getCourseSelected().equalsIgnoreCase("true")) {
                result += "\n" + p.getCourseName();
                listOfSingleCourseDetailsForNotifications = newDatabaseConForNotifications.getAssignmentDetailsOfEachToPushNotifications(p.getCourseName());
                theList(listOfSingleCourseDetailsForNotifications);
            }
        }

    }//end show method

    //List method to append on to new list
    public void theList (List<AssignmentNotificationObject> wholeList) {

        listOfAllCourseDetailsForNotifications.addAll(wholeList);
    }//end theList method


}//end class