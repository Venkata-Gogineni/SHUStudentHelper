package com.example.android.shustudenthelper;

/**
 * Created by Surya Gogineni on 10/24/2016.
 */

public class GamesDisplay {
    public String gamesDisplay;
    public String gameDate;
    public String gameName;
    public String gameTime;
    public String gameVenue;
    public String gameHost;
    public String gameVisitor;
    public String gamePrice;

    public GamesDisplay(String gamesDisplay, String gameDate)
    {
        this.gamesDisplay = gamesDisplay;
        this.gameDate = gameDate;
    }

    public GamesDisplay(String gameName, String gameTime, String gameVenue, String gameHost, String gameVisitor, String gamePrice) {
        this.gameName = gameName;
        this.gameTime = gameTime;
        this.gameVenue = gameVenue;
        this.gameHost = gameHost;
        this.gameVisitor = gameVisitor;
        this.gamePrice = gamePrice;
    }

    public String getGamesDisplay() {
        return gamesDisplay;
    }

    public String getGameName() {
        return gameName;
    }

    public String getGameTime() {
        return gameTime;
    }

    public String getGameVenue() {
        return gameVenue;
    }

    public String getGameHost() {
        return gameHost;
    }

    public String getGameVisitor() {
        return gameVisitor;
    }

    public String getGamePrice() {
        return gamePrice;
    }

    public String getGameDate() {
        return gameDate;
    }
}
