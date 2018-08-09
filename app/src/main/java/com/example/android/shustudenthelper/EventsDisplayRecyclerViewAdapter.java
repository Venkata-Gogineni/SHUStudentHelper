package com.example.android.shustudenthelper;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class EventsDisplayRecyclerViewAdapter extends RecyclerView.Adapter<EventsRecyclerDisplay>{

    private final List<EventsDisplay> events;
    private Context context;
    private int itemResource;

    public EventsDisplayRecyclerViewAdapter(Context context, int itemResource, List<EventsDisplay> events) {

        // 1. Initialize our adapter
        this.events = events;
        this.context = context;
        this.itemResource = itemResource;
    }

    // 2. Override the onCreateViewHolder method
    @Override
    public EventsRecyclerDisplay onCreateViewHolder(ViewGroup parent, int viewType) {

        // 3. Inflate the view and return the new ViewHolder
        View view = LayoutInflater.from(parent.getContext())
                .inflate(this.itemResource, parent, false);
        return new EventsRecyclerDisplay(this.context, view);
    }

    // 4. Override the onBindViewHolder method
    @Override
    public void onBindViewHolder(EventsRecyclerDisplay holder, int position) {

        // 5. Use position to access the correct Bakery object
        EventsDisplay event = this.events.get(position);

        // 6. Bind the bakery object to the holder
        holder.bindEvents(event);
    }

    @Override
    public int getItemCount() {

        return this.events.size();
    }
}
