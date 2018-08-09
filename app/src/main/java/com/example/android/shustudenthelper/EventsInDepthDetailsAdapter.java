package com.example.android.shustudenthelper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import static com.example.android.shustudenthelper.CourseInDepthDetailsAdapter.mInDepthDataSource;
import static com.example.android.shustudenthelper.GamesInDepthDetailsAdapter.mInDepthGamesDataSource;

/**
 * Created by Surya Gogineni on 11/1/2016.
 */

public class EventsInDepthDetailsAdapter extends ArrayAdapter<EventsDisplay> {

    private Context mContext;
    private LayoutInflater mInflater;
    int layoutResourceId;
    public static List<EventsDisplay> mInDepthEventsDataSource;


    public EventsInDepthDetailsAdapter(Context context, int layoutResourceId, List<EventsDisplay> items) {
        super(context, layoutResourceId, items);

        mContext = context;
        mInDepthEventsDataSource = items;
        this.layoutResourceId = layoutResourceId;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return mInDepthEventsDataSource.size();
    }

    @Override
    public EventsDisplay getItem(int position) {
        return mInDepthEventsDataSource.get(position);
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
        EventsInDepthDetailsAdapter.ViewHolder holder;
        holder = null;
        final EventsDisplay eventDisplay = getEvent(position);
        // 1
        if (row == null) {

            // 2
            row = mInflater.inflate(R.layout.custom_listview_eventdetails, parent, false);

            // 3
            holder = new EventsInDepthDetailsAdapter.ViewHolder();

            //Casting the holder to text views
            holder.eventNameTextView = (TextView) row.findViewById(R.id.event_name_display);
            holder.eventStartTimeTextView = (TextView) row.findViewById(R.id.event_start_time_display);
            holder.eventStartDateView = (TextView) row.findViewById(R.id.event_start_date_display);
            holder.eventVenueTextView = (TextView) row.findViewById(R.id.event_venue_display);
            holder.eventTicketPriceTextView = (TextView) row.findViewById(R.id.event_ticket_price_display);

            //Setting the text to display
            holder.eventNameTextView.setText(eventDisplay.getEventName());
            holder.eventStartTimeTextView.setText(eventDisplay.getEventTime());
            holder.eventStartDateView.setText(eventDisplay.getEventDate());
            holder.eventVenueTextView.setText(eventDisplay.getEventVenue());
            holder.eventTicketPriceTextView.setText(eventDisplay.getEventPrice());

            // 4
            row.setTag(holder);
        } else {
            // 5
            holder = (EventsInDepthDetailsAdapter.ViewHolder) row.getTag();
        }

        // 6
        return row;
    }



    private EventsDisplay getEvent(int position) {
        return ((EventsDisplay) getItem(position));
    }

    private static final class ViewHolder {

        public TextView eventNameTextView;
        public TextView eventStartTimeTextView;
        public TextView eventStartDateView;
        public TextView eventVenueTextView;
        public TextView eventTicketPriceTextView;
    }
}//end class
