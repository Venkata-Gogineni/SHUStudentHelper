package com.example.android.shustudenthelper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

public class CourseInDepthDetailsActivity extends AppCompatActivity {

    ListView myListViewInDepthDetailsCourses;

    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;

    public static String selectedExpandableItem;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_indepth_details);
        DatabaseOpenHelper myDbHelper = new DatabaseOpenHelper(this);

        List<CourseDisplay> coursesInDepthDisplay_data = new ArrayList<CourseDisplay>();

        coursesInDepthDisplay_data = myDbHelper.getCourseInDepthDetailsToDisplay();

        ArrayAdapter myNewAdapter = new CourseInDepthDetailsAdapter(this,R.layout.custom_listview_coursedetails_three,coursesInDepthDisplay_data);

        myListViewInDepthDetailsCourses = (ListView)findViewById(R.id.listview_indepth_course_details);

        myListViewInDepthDetailsCourses.setAdapter(myNewAdapter);

        //==========================================================================================================================

        expandableListView = (ExpandableListView) findViewById(R.id.expandable_list_view);

        HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();

        List<AssignmentDisplay> coursesAssignmentDisplay_data = new ArrayList<AssignmentDisplay>();

        coursesAssignmentDisplay_data = myDbHelper.getAssignmentNamesToDisplay();

        List<String> project_data = new ArrayList<String>();

        for (AssignmentDisplay C: coursesAssignmentDisplay_data) {

            project_data = myDbHelper.getAssignmentDetailsOfEachToDisplay(C.getAssignmentName());

            expandableListDetail.put(C.getAssignmentName(), project_data);

            //System.out.println("the assignments are " +C.getAssignmentName());
//            for (String D: project_data) {
//                System.out.println("the assignments details are " + D.toString());
//            }

        }//end for

        expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());

        expandableListAdapter = new AssignmentDisplayAdapter(this, expandableListTitle, expandableListDetail);

        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
//                Toast.makeText(getApplicationContext(),
//                        expandableListTitle.get(groupPosition) + " List Expanded.",
//                        Toast.LENGTH_SHORT).show();
                selectedExpandableItem = expandableListTitle.get(groupPosition);
            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
//                Toast.makeText(getApplicationContext(),
//                        expandableListTitle.get(groupPosition) + " List Collapsed.",
//                        Toast.LENGTH_SHORT).show();

            }
        });

    }//end On create
}//end class
