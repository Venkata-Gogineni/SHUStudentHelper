package com.example.android.shustudenthelper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    ListView myListViewCourses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

     DatabaseOpenHelper myDbHelper = new DatabaseOpenHelper(this);

        List<CourseDisplay> coursesDisplay_data = new ArrayList<CourseDisplay>();

        coursesDisplay_data = myDbHelper.getSelectedCoursesToDisplay();

        for (CourseDisplay C:
                coursesDisplay_data ) {
            System.out.println("the selection status is " +C.getCourseName());
        }

        ArrayAdapter myNewAdapter = new CourseDisplayAdapter(this,R.layout.custom_listview_layout_two,coursesDisplay_data);

        myListViewCourses = (ListView)findViewById(R.id.listview_selected_courses);

        View header = getLayoutInflater().inflate(R.layout.activity_home_courses_header, null);

        myListViewCourses.addHeaderView(header);

        myListViewCourses.setAdapter(myNewAdapter);



    }//end OnCreate


}//end class
