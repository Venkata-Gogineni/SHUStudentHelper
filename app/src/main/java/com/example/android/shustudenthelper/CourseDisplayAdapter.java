package com.example.android.shustudenthelper;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android.shustudenthelper.CourseDisplay;
import com.example.android.shustudenthelper.R;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.shustudenthelper.CourseSelectionAdapter.checkBoxCounter;

/**
 * Created by Surya Gogineni on 10/10/2016.
 */

public class CourseDisplayAdapter extends ArrayAdapter<CourseDisplay>{


    private Context mContext;
    private LayoutInflater mInflater;
    int layoutResourceId;
    private static List<CourseDisplay> mDataSource;



    public CourseDisplayAdapter(Context context, int layoutResourceId, List<CourseDisplay> items) {
        super(context, layoutResourceId, items);

        mContext = context;
        mDataSource = items;
        this.layoutResourceId = layoutResourceId;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

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
            row = mInflater.inflate(R.layout.custom_listview_layout_two, parent, false);

            // 3
            holder = new ViewHolder();


            holder.courseNameTextView = (TextView) row.findViewById(R.id.selected_course_names_display);

            holder.courseNameTextView.setText(courseDisplay.getCourseName());
            // 4
            row.setTag(holder);
        } else {
            // 5
            holder = (ViewHolder) row.getTag();
        }
        return row;
    }

    private static final class ViewHolder {

        private TextView courseNameTextView;

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
