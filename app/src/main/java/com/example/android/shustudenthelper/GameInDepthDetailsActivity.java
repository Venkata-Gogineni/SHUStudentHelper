package com.example.android.shustudenthelper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class GameInDepthDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_in_depth_details);
        DatabaseOpenHelper myDbHelper = new DatabaseOpenHelper(this);

        List<GamesDisplay> gamesInDepthDisplay_data = new ArrayList<GamesDisplay>();

        gamesInDepthDisplay_data = myDbHelper.getGameInDepthDetailsToDisplay();

        ArrayAdapter myNewAdapter = new GamesInDepthDetailsAdapter(this,R.layout.custom_listview_gamedetails,gamesInDepthDisplay_data);

        ListView myListViewInDepthDetailsGames = (ListView)findViewById(R.id.listview_indepth_game_details);

        myListViewInDepthDetailsGames.setAdapter(myNewAdapter);
    }
}
