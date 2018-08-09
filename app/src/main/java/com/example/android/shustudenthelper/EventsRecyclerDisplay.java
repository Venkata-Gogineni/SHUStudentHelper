package com.example.android.shustudenthelper;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.android.shustudenthelper.MyCustomHomeActivity.clickedHomeActivityCourse;
import static com.example.android.shustudenthelper.MyCustomHomeActivity.clickedHomeActivityEvent;
import static com.example.android.shustudenthelper.MyCustomHomeActivity.clickedHomeActivityGame;
import static com.example.android.shustudenthelper.MyCustomHomeActivity.clickedHomeActivityGameDate;

public class EventsRecyclerDisplay extends RecyclerView.ViewHolder implements View.OnClickListener {
    private TextView eventName;
    private EventsDisplay eventsDisplay;
    private Context context;

    public EventsRecyclerDisplay(Context context, View itemView){
        super(itemView);

        this.context = context;
        this.eventName = (TextView)itemView.findViewById(R.id.events_display);
        //this.gameDate = (TextView) itemView.findViewById(R.id.event_date_display);
        itemView.setOnClickListener(this);
    }

    public void bindEvents(EventsDisplay eventsDisplay){
        this.eventsDisplay = eventsDisplay;
        this.eventName.setText(eventsDisplay.getEventName());
        //this.gameDate.setText(gamesDisplay.getGameDate());
    }
    public void onClick(View v) {

        // 5. Handle the onClick event for the ViewHolder
        if (this.eventsDisplay != null) {

            Toast.makeText(this.context, "Clicked on " + this.eventsDisplay.getEventName(), Toast.LENGTH_SHORT ).show();
            clickedHomeActivityEvent = this.eventsDisplay.getEventName();
            //clickedHomeActivityGameDate = this.gamesDisplay.getGameDate();
            Intent intent = new Intent("com.example.android.shustudenthelper.EventInDepthDetailsActivity");
            context.startActivity(intent);
        }
    }
}//end class
