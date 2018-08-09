package com.example.android.shustudenthelper;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.checked;
import static android.R.id.list;
import static android.media.CamcorderProfile.get;

/**
 * Created by Surya Gogineni on 10/10/2016.
 */

public class CourseSelectionAdapter extends ArrayAdapter<CourseDisplay>{


    private Context mContext;
    private LayoutInflater mInflater;
    int layoutResourceId;
    public static List<CourseDisplay> mDataSource;
    public static int checkBoxCounter;


    public CourseSelectionAdapter(Context context, int layoutResourceId, List<CourseDisplay> items) {
        super(context, layoutResourceId, items);

        mContext = context;
        mDataSource = items;
        this.layoutResourceId = layoutResourceId;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        checkBoxCounter = 0;


    }

    @Override
    public int getCount() {
        return mDataSource.size();
    }

    @Override
    public CourseDisplay getItem(int position) {
        return mDataSource.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //4
    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {
        // Get view for row item
        View row = convertView;
        ViewHolder holder;
        holder = null;
        final CourseDisplay courseDisplay = getCourse(position);
// 1
        if (row == null) {

            // 2
            row = mInflater.inflate(R.layout.custom_listview_layout, parent, false);

            // 3
            holder = new ViewHolder();


            holder.courseCodeTextView = (TextView) row.findViewById(R.id.course_id_display);
            holder.courseNameTextView = (TextView) row.findViewById(R.id.course_name_display);
            holder.courseSelectedCheckBox = (CheckBox) row.findViewById(R.id.course_selection_checkbox);

            holder.courseCodeTextView.setText(courseDisplay.getCourseCode());
            holder.courseNameTextView.setText(courseDisplay.getCourseName());

            holder.courseSelectedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (isChecked){
                        checkBoxCounter++;
                        //getCourse((Integer) buttonView.getTag()).getCourseSelected();
                        courseDisplay.setCourseSelected("true");

                    }else {
                        checkBoxCounter--;
                        courseDisplay.setCourseSelected("false");
                    }
                    System.out.println("Count is: "+ checkBoxCounter);
                }
            });
            // 4
            row.setTag(holder);
        } else {
            // 5
            holder = (ViewHolder) row.getTag();
        }

            // 6
        holder.courseSelectedCheckBox.setTag(position);
        if (courseDisplay.getCourseSelected().equalsIgnoreCase("true")){
            holder.courseSelectedCheckBox.setChecked(true);
        }else{
            holder.courseSelectedCheckBox.setChecked(false);
        }


        return row;
    }

    private static final class ViewHolder {
        public TextView courseCodeTextView;
        public TextView courseNameTextView;
        public CheckBox courseSelectedCheckBox;
    }

    CourseDisplay getCourse(int position) {
        return ((CourseDisplay) getItem(position));
    }

    List<CourseDisplay> getBox() {

        List<CourseDisplay> box = new ArrayList<CourseDisplay>();

        for (CourseDisplay p : mDataSource) {

            if (p.getCourseSelected().equalsIgnoreCase("true"))
                box.add(p);
        }
        return box;
    }

}//end class
