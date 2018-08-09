package com.example.android.shustudenthelper;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.android.shustudenthelper.MyCustomHomeActivity.clickedHomeActivityCourse;

/**
 * Created by Surya Gogineni on 10/25/2016.
 */

public class CourseRecyclerDisplay extends RecyclerView.ViewHolder implements View.OnClickListener {
    private TextView courseName;
     private CourseDisplay courseDisplay;
    private Context context;

    public CourseRecyclerDisplay(Context context, View itemView){
        super(itemView);

        this.context = context;
        this.courseName = (TextView)itemView.findViewById(R.id.selected_course_names_display);
        itemView.setOnClickListener(this);
    }

    public void bindCourses(CourseDisplay courseDisplay){
        this.courseDisplay = courseDisplay;
        this.courseName.setText(courseDisplay.getCourseName());
    }
    public void onClick(View v) {

        // 5. Handle the onClick event for the ViewHolder
        if (this.courseDisplay != null) {

            Toast.makeText(this.context, "Clicked on " + this.courseDisplay.getCourseName(), Toast.LENGTH_SHORT ).show();
            clickedHomeActivityCourse = this.courseDisplay.getCourseName();
            Intent intent = new Intent("com.example.android.shustudenthelper.CourseInDepthDetailsActivity");
            context.startActivity(intent);
        }

    }
}//end class
