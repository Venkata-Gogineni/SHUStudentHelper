package com.example.android.shustudenthelper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class EventInDepthDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_in_depth_details);

        DatabaseOpenHelper myDbHelper = new DatabaseOpenHelper(this);

        List<EventsDisplay> eventsInDepthDisplay_data = new ArrayList<EventsDisplay>();

        eventsInDepthDisplay_data = myDbHelper.getEventInDepthDetailsToDisplay();

        ArrayAdapter myNewAdapter = new EventsInDepthDetailsAdapter(this,R.layout.custom_listview_eventdetails,eventsInDepthDisplay_data);

        ListView myListViewInDepthDetailsEvents = (ListView)findViewById(R.id.listview_indepth_event_details);

        myListViewInDepthDetailsEvents.setAdapter(myNewAdapter);
    }
}
