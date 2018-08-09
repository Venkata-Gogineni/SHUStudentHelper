package com.example.android.shustudenthelper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.shustudenthelper.CourseInDepthDetailsAdapter.mInDepthDataSource;

/**
 * Created by Surya Gogineni on 10/28/2016.
 */

public class GamesInDepthDetailsAdapter extends ArrayAdapter<GamesDisplay> {

    private Context mContext;
    private LayoutInflater mInflater;
    int layoutResourceId;
    public static List<GamesDisplay> mInDepthGamesDataSource;
    public static int checkBoxCounter;

    public GamesInDepthDetailsAdapter(Context context, int layoutResourceId, List<GamesDisplay> items) {
        super(context, layoutResourceId, items);

        mContext = context;
        mInDepthGamesDataSource = items;
        this.layoutResourceId = layoutResourceId;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

    @Override
    public int getCount() {
        return mInDepthGamesDataSource.size();
    }

    @Override
    public GamesDisplay getItem(int position) {
        return mInDepthGamesDataSource.get(position);
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
        GamesInDepthDetailsAdapter.ViewHolder holder;
        holder = null;
        final GamesDisplay gameDisplay = getGame(position);
        // 1
        if (row == null) {

            // 2
            row = mInflater.inflate(R.layout.custom_listview_gamedetails, parent, false);

            // 3
            holder = new GamesInDepthDetailsAdapter.ViewHolder();

            //Casting the holder to text views
            holder.gameNameTextView = (TextView) row.findViewById(R.id.game_name_display);
            holder.gameStartTimeTextView = (TextView) row.findViewById(R.id.game_start_time_display);
            holder.gameVenueTextView = (TextView) row.findViewById(R.id.venue_display);
            holder.gameHostTextView = (TextView) row.findViewById(R.id.host_display);
            holder.gameVisitorTextView = (TextView) row.findViewById(R.id.visitor_display);
            holder.gameTicketPriceTextView = (TextView) row.findViewById(R.id.ticket_price_display);

            //Setting the text to display
            holder.gameNameTextView.setText(gameDisplay.getGameName());
            holder.gameStartTimeTextView.setText(gameDisplay.getGameTime());
            holder.gameVenueTextView.setText(gameDisplay.getGameVenue());
            holder.gameHostTextView.setText(gameDisplay.getGameHost());
            holder.gameVisitorTextView.setText(gameDisplay.getGameVisitor());
            holder.gameTicketPriceTextView.setText(gameDisplay.getGamePrice());

            // 4
            row.setTag(holder);
        } else {
            // 5
            holder = (GamesInDepthDetailsAdapter.ViewHolder) row.getTag();
        }

        // 6
        return row;
    }

    private GamesDisplay getGame(int position) {
        return ((GamesDisplay) getItem(position));
    }

    private static final class ViewHolder {

        public TextView gameNameTextView;
        public TextView gameStartTimeTextView;
        public TextView gameVenueTextView;
        public TextView gameHostTextView;
        public TextView gameVisitorTextView;
        public TextView gameTicketPriceTextView;
    }


}//end Adapter Class
