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

import com.example.android.shustudenthelper.CourseDisplay;
import com.example.android.shustudenthelper.R;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.checked;
import static android.R.id.list;
import static android.media.CamcorderProfile.get;
import static com.example.android.shustudenthelper.CourseSelectionAdapter.mDataSource;

/**
 * Created by Surya Gogineni on 10/10/2016.
 */

public class CourseInDepthDetailsAdapter extends ArrayAdapter<CourseDisplay> {


    private Context mContext;
    private LayoutInflater mInflater;
    int layoutResourceId;
    public static List<CourseDisplay> mInDepthDataSource;
    public static int checkBoxCounter;


    public CourseInDepthDetailsAdapter(Context context, int layoutResourceId, List<CourseDisplay> items) {
        super(context, layoutResourceId, items);

        mContext = context;
        mInDepthDataSource = items;
        this.layoutResourceId = layoutResourceId;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        checkBoxCounter = 0;


    }

    @Override
    public int getCount() {
        return mInDepthDataSource.size();
    }

    @Override
    public CourseDisplay getItem(int position) {
        return mInDepthDataSource.get(position);
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
            row = mInflater.inflate(R.layout.custom_listview_coursedetails_three, parent, false);

            // 3
            holder = new ViewHolder();

            //Casting the holder to text views
            holder.courseNameTextView = (TextView) row.findViewById(R.id.coursedetails_name_display);
            holder.courseCodeTextView = (TextView) row.findViewById(R.id.coursedetails_code_display);
            holder.courseProfessorTextView = (TextView) row.findViewById(R.id.coursedetails_Professor_display);
            holder.courseCLATextView = (TextView) row.findViewById(R.id.coursedetails_cla_display);
            holder.courseTimingsTextView = (TextView) row.findViewById(R.id.coursedetails_classtimings_display);
            holder.courseClassRoomTextView = (TextView) row.findViewById(R.id.coursedetails_classroom_display);

            //Setting the text to display
            holder.courseCodeTextView.setText(courseDisplay.getCourseCode());
            holder.courseNameTextView.setText(courseDisplay.getCourseName());
            holder.courseProfessorTextView.setText(courseDisplay.getCourseProfessor());
            holder.courseCLATextView.setText(courseDisplay.getCourseCLA());
            holder.courseTimingsTextView.setText(courseDisplay.getCourseTimings());
            holder.courseClassRoomTextView.setText(courseDisplay.getCourseClassRoom());

            // 4
            row.setTag(holder);
        } else {
            // 5
            holder = (ViewHolder) row.getTag();
        }

        // 6
        return row;
    }

    private static final class ViewHolder {

        public TextView courseNameTextView;
        public TextView courseCodeTextView;
        public TextView courseProfessorTextView;
        public TextView courseCLATextView;
        public TextView courseTimingsTextView;
        public TextView courseClassRoomTextView;


    }

    CourseDisplay getCourse(int position) {
        return ((CourseDisplay) getItem(position));
    }

    List<CourseDisplay> getDetailsBox() {

        List<CourseDisplay> box = new ArrayList<CourseDisplay>();

        for (CourseDisplay p : mInDepthDataSource) {

            if (p.getCourseSelected().equalsIgnoreCase("true"))
                box.add(p);
        }
        return box;
    }

}//end class
