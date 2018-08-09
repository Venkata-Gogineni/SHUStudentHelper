package com.example.android.shustudenthelper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Surya Gogineni on 10/24/2016.
 */

public class EventDisplayAdapter extends ArrayAdapter<EventsDisplay> {
    private Context mContext;
    private LayoutInflater mInflater;
    int layoutResourceId;
    private static List<EventsDisplay> mDataSource;



    public EventDisplayAdapter(Context context, int layoutResourceId, List<EventsDisplay> items) {
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
    public EventsDisplay getItem(int position) {
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
        final EventsDisplay eventsDisplay = getEvent(position);
// 1
        if (row == null) {

            // 2
            row = mInflater.inflate(R.layout.custom_listview_events, parent, false);

            // 3
            holder = new EventDisplayAdapter.ViewHolder();


            holder.eventNameTextView = (TextView) row.findViewById(R.id.events_display);

            holder.eventNameTextView.setText(eventsDisplay.getEventName());
            // 4
            row.setTag(holder);
        } else {
            // 5
            holder = (EventDisplayAdapter.ViewHolder) row.getTag();
        }
        return row;
    }

    private static final class ViewHolder {

        private TextView eventNameTextView;

    }

    EventsDisplay getEvent(int position) {
        return ((EventsDisplay) getItem(position));
    }

    List<EventsDisplay> getBox() {
        List<EventsDisplay> box = new ArrayList<EventsDisplay>();
        for (EventsDisplay p : mDataSource) {
            if (p.toString().equalsIgnoreCase("true"))
                box.add(p);
        }
        return box;
    }
}
