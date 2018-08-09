package com.example.android.shustudenthelper;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Surya Gogineni on 10/25/2016.
 */

public class GamesDisplayRecyclerviewAdapter extends RecyclerView.Adapter<GamesRecyclerDisplay>{

    private final List<GamesDisplay> bakeries;
    private Context context;
    private int itemResource;

    public GamesDisplayRecyclerviewAdapter(Context context, int itemResource, List<GamesDisplay> bakeries) {

        // 1. Initialize our adapter
        this.bakeries = bakeries;
        this.context = context;
        this.itemResource = itemResource;
    }

    // 2. Override the onCreateViewHolder method
    @Override
    public GamesRecyclerDisplay onCreateViewHolder(ViewGroup parent, int viewType) {

        // 3. Inflate the view and return the new ViewHolder
        View view = LayoutInflater.from(parent.getContext())
                .inflate(this.itemResource, parent, false);
        return new GamesRecyclerDisplay(this.context, view);
    }

    // 4. Override the onBindViewHolder method
    @Override
    public void onBindViewHolder(GamesRecyclerDisplay holder, int position) {

        // 5. Use position to access the correct Bakery object
        GamesDisplay bakery = this.bakeries.get(position);

        // 6. Bind the bakery object to the holder
        holder.bindGames(bakery);
    }

    @Override
    public int getItemCount() {

        return this.bakeries.size();
    }
}
