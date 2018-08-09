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

public class GamesDisplayAdapter extends ArrayAdapter<GamesDisplay> {
    private Context mContext;
    private LayoutInflater mInflater;
    int layoutResourceId;
    private static List<GamesDisplay> mDataSource;



    public GamesDisplayAdapter(Context context, int layoutResourceId, List<GamesDisplay> items) {
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
    public GamesDisplay getItem(int position) {
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
        GamesDisplayAdapter.ViewHolder holder;
        holder = null;
        final GamesDisplay gamesDisplay = getGame(position);
// 1
        if (row == null) {

            // 2
            row = mInflater.inflate(R.layout.custom_listview_games, parent, false);

            // 3
            holder = new GamesDisplayAdapter.ViewHolder();


            holder.gameNameTextView = (TextView) row.findViewById(R.id.games_display);

            holder.gameNameTextView.setText(gamesDisplay.getGamesDisplay());
            // 4
            row.setTag(holder);
        } else {
            // 5

            holder = (GamesDisplayAdapter.ViewHolder) row.getTag();
        }
        return row;
    }

    private static final class ViewHolder {

        private TextView gameNameTextView;

    }

    GamesDisplay getGame(int position)
    {
        return ((GamesDisplay) getItem(position));
    }

    List<GamesDisplay> getBox() {
        List<GamesDisplay> box = new ArrayList<GamesDisplay>();
        for (GamesDisplay p : mDataSource) {
            if (p.toString().equalsIgnoreCase("true"))
                box.add(p);
        }
        return box;
    }
}//end class
