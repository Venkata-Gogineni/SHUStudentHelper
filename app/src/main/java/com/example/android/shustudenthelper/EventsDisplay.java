package com.example.android.shustudenthelper;

/**
 * Created by Surya Gogineni on 10/24/2016.
 */

public class EventsDisplay {
    public String eventName;
    public String eventTime;
    public String eventDate;
    public String eventVenue;
    public String eventPrice;


    public EventsDisplay(String eventName) {
        this.eventName = eventName;
    }

    public EventsDisplay(String eventName, String eventTime, String eventDate, String eventVenue, String eventPrice) {
        this.eventName = eventName;
        this.eventTime = eventTime;
        this.eventDate = eventDate;
        this.eventVenue = eventVenue;
        this.eventPrice = eventPrice;
    }

    public String getEventName() {
        return eventName;
    }


    public String getEventTime() {
        return eventTime;
    }

    public String getEventDate() {
        return eventDate;
    }

    public String getEventVenue() {
        return eventVenue;
    }

    public String getEventPrice() {
        return eventPrice;
    }
}
