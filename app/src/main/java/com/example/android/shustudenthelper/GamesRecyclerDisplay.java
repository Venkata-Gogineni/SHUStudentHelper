package com.example.android.shustudenthelper;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.android.shustudenthelper.MyCustomHomeActivity.clickedHomeActivityCourse;
import static com.example.android.shustudenthelper.MyCustomHomeActivity.clickedHomeActivityGame;
import static com.example.android.shustudenthelper.MyCustomHomeActivity.clickedHomeActivityGameDate;

public class GamesRecyclerDisplay extends RecyclerView.ViewHolder implements View.OnClickListener {
    private TextView gameName;
    private TextView gameDate;
    private GamesDisplay gamesDisplay;
    private Context context;

    public GamesRecyclerDisplay(Context context, View itemView){
        super(itemView);

        this.context = context;
        this.gameName = (TextView)itemView.findViewById(R.id.games_display);
        this.gameDate = (TextView) itemView.findViewById(R.id.game_date_display);
        itemView.setOnClickListener(this);
    }

    public void bindGames(GamesDisplay gamesDisplay){
        this.gamesDisplay = gamesDisplay;
        this.gameName.setText(gamesDisplay.getGamesDisplay());
        this.gameDate.setText(gamesDisplay.getGameDate());
    }
    public void onClick(View v) {

        // 5. Handle the onClick event for the ViewHolder
        if (this.gamesDisplay != null) {

            Toast.makeText(this.context, "Clicked on " + this.gamesDisplay.getGamesDisplay(), Toast.LENGTH_SHORT ).show();
            clickedHomeActivityGame = this.gamesDisplay.getGamesDisplay();
            clickedHomeActivityGameDate = this.gamesDisplay.getGameDate();
            Intent intent = new Intent("com.example.android.shustudenthelper.GameInDepthDetailsActivity");
            context.startActivity(intent);
        }
    }
}//end class
