package com.example.android.shustudenthelper;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresPermission;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hanks.htextview.HTextView;
import com.hanks.htextview.HTextViewType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import me.grantland.widget.AutofitTextView;

import static android.support.v4.content.PermissionChecker.PERMISSION_GRANTED;

public class MyCustomHomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, LocationListener {

    private boolean locationBoolean = false;
    /*Database Instances*/
    DatabaseOpenHelper myDbHelper;
    DatabaseOpenHelper myDbHelper2;
    DatabaseOpenHelper myDbHelper3;

    /*Recycler View Instances*/
    private RecyclerView courseRecyclerView;
    private RecyclerView gamesView;
    private RecyclerView eventsRecyclerView;

    public static String clickedHomeActivityCourse;
    public static String clickedHomeActivityGame;
    public static String clickedHomeActivityGameDate;
    public static String clickedHomeActivityEvent;

    double latitude, longitude;
    LocationManager mLocMgr;
    Location myLocation;
    TextView gamesTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_custom_home);
        gamesTextView = (TextView)findViewById(R.id.games_text_display);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Snackbar.make(view, "Welcome to Sacred Heart", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setNotifications();//Run all Notifications when the app is run for the first time
        displaySelectedCourses();//Display all the courses selected at Settings
        displayGamesForNextOneWeek();//Display all the Games available for next one week from current system date
        displayEventsForNextOneWeek();//Display all the Events available for next one week from current system date
        requestPermissionFromUser();//Request permissions from user to allow GPS

    }//end On create

    @Override
    protected void onResume() {

        super.onResume();

        new FetchWeatherTask().execute("06606");//Run the weather task on resuming the app

        new FetchDurationAsyncTask().execute();//Run the duration task on resuming the app

        locationBoolean = false;

    }
    @Override
    protected void onRestart() {
        super.onRestart();  // Always call the superclass method first

        // Activity being restarted from stopped state
    }

    @Override
    protected void onStart() {
        super.onStart();  // Always call the superclass method first
    }

    public void setNotifications() {

        int counter = 1;

        for (AssignmentNotificationObject q : CourseSelectionActivity.listOfAllCourseDetailsForNotifications) {

            try {
                SimpleDateFormat myFormat = new SimpleDateFormat("MM/dd/yyyy");
                String dateFromDB = q.getAssignmentDueDate();

                Date date2 = myFormat.parse(dateFromDB);
                String[] myDate = dateFromDB.split("/");
                String month = myDate[0];
                String date = myDate[1];
                String year = myDate[2];

                Intent intent = new Intent(this, MyReceiver.class);
                intent.putExtra("Title", q.getCourseName());
                intent.putExtra("Name", q.getAssignmentName());
                intent.putExtra("Date", q.getAssignmentDueDate());
                intent.putExtra("Counter", counter);

                PendingIntent pendingIntent = PendingIntent.getBroadcast(this, counter, intent, 0);
                counter++;

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.DATE, Integer.parseInt(date));
                calendar.set(Calendar.MONTH, (Integer.parseInt(month) - 1));
                calendar.set(Calendar.YEAR, Integer.parseInt(year));
                calendar.set(Calendar.HOUR_OF_DAY, 11);
                calendar.set(Calendar.MINUTE, 29);
                calendar.set(Calendar.SECOND, 00);

                AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
                am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }//End Notification method

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        if(location!=null && !locationBoolean){
            locationBoolean = true;
            new FetchDurationAsyncTask().execute();
        }
//        System.out.println("Latitude :" + latitude);
//        System.out.println("Longitude :" + longitude);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    class FetchWeatherTask extends AsyncTask<String, Void, String[]> {

        private final String LOG_TAG = FetchWeatherTask.class.getSimpleName();

        private String getReadableDateString(long time) {
            // Because the API returns a unix timestamp (measured in seconds),
            // it must be converted to milliseconds in order to be converted to valid date.
            SimpleDateFormat shortenedDateFormat = new SimpleDateFormat("EEE MMM dd");
            return shortenedDateFormat.format(time);
        }

        /**
         * Prepare the weather high/lows for presentation.
         */
        private String formatHighLows(double high, double low) {
            // For presentation, assume the user doesn't care about tenths of a degree.
            long roundedHigh = Math.round(high);
            long roundedLow = Math.round(low);

            String highLowStr = roundedHigh + "/" + roundedLow;
            return highLowStr;
        }

        /**
         * Take the String representing the complete forecast in JSON Format and
         * pull out the data we need to construct the Strings needed for the wireframes.
         * constructor takes the JSON string and converts it
         * into an Object hierarchy for us.
         */
        private String[] getWeatherDataFromJson(String forecastJsonStr, int numDays)
                throws JSONException {

            // These are the names of the JSON objects that need to be extracted.
            final String OWM_LIST = "list";
            final String OWM_WEATHER = "weather";
            final String OWM_TEMPERATURE = "temp";
            final String OWM_MAX = "max";
            final String OWM_MIN = "min";
            final String OWM_DESCRIPTION = "main";

            JSONObject forecastJson = new JSONObject(forecastJsonStr);
            JSONArray weatherArray = forecastJson.getJSONArray(OWM_LIST);

            // OWM returns daily forecasts based upon the local time of the city that is being
            // asked for, which means that we need to know the GMT offset to translate this data
            // properly.

            // Since this data is also sent in-order and the first day is always the
            // current day, we're going to take advantage of that to get a nice
            // normalized UTC date for all of our weather.

            Time dayTime = new Time();
            dayTime.setToNow();

            // we start at the day returned by local time. Otherwise this is a mess.
            int julianStartDay = Time.getJulianDay(System.currentTimeMillis(), dayTime.gmtoff);

            // now we work exclusively in UTC
            dayTime = new Time();

            String[] resultStrs = new String[numDays];
            for (int i = 0; i < weatherArray.length(); i++) {
                // For now, using the format "Day, description, hi/low"
                String day;
                String description;
                String highAndLow;

                // Get the JSON object representing the day
                JSONObject dayForecast = weatherArray.getJSONObject(i);

                // The date/time is returned as a long.  We need to convert that
                // into something human-readable, since most people won't read "1400356800" as
                // "this saturday".
                long dateTime;
                // Cheating to convert this to UTC time, which is what we want anyhow
                dateTime = dayTime.setJulianDay(julianStartDay + i);
                day = getReadableDateString(dateTime);

                // description is in a child array called "weather", which is 1 element long.
                JSONObject weatherObject = dayForecast.getJSONArray(OWM_WEATHER).getJSONObject(0);
                description = weatherObject.getString(OWM_DESCRIPTION);

                // Temperatures are in a child object called "temp".  Try not to name variables
                // "temp" when working with temperature.  It confuses everybody.
                JSONObject temperatureObject = dayForecast.getJSONObject(OWM_TEMPERATURE);
                double high = temperatureObject.getDouble(OWM_MAX);
                double low = temperatureObject.getDouble(OWM_MIN);

                highAndLow = formatHighLows(high, low);
                resultStrs[i] = day + " - " + description + " - " + highAndLow;
            }

            /*for (String s : resultStrs) {
                Log.v(LOG_TAG, "Forecast entry: " + s);
            }*/
            return resultStrs;

        }

        @Override
        protected String[] doInBackground(String... params) {

            // If there's no zip code, there's nothing to look up.  Verify size of params.
            if (params.length == 0) {
                return null;
            }

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;

            String format = "json";
            String units = "metric";
            int numDays = 1;

            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                final String FORECAST_BASE_URL =
                        "http://api.openweathermap.org/data/2.5/forecast/daily?";
                final String QUERY_PARAM = "q";
                final String FORMAT_PARAM = "mode";
                final String UNITS_PARAM = "units";
                final String DAYS_PARAM = "cnt";
                final String APPID_PARAM = "APPID";

                Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                        .appendQueryParameter(QUERY_PARAM, params[0])
                        .appendQueryParameter(FORMAT_PARAM, format)
                        .appendQueryParameter(UNITS_PARAM, units)
                        .appendQueryParameter(DAYS_PARAM, Integer.toString(numDays))
                        .appendQueryParameter(APPID_PARAM, BuildConfig.OPEN_WEATHER_MAP_API_KEY)
                        .build();

                URL url = new URL(builtUri.toString());

                Log.v(LOG_TAG, "Built URI " + builtUri.toString());

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                forecastJsonStr = buffer.toString();

                Log.v(LOG_TAG, "Forecast string: " + forecastJsonStr);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                return getWeatherDataFromJson(forecastJsonStr, numDays);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            // This will only happen if there was an error getting or parsing the forecast.
            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {

            //TextView weatherTextView = (TextView) findViewById(R.id.text_view_weather);
            HTextView weatherTextView = (HTextView) findViewById(R.id.text_view_weather);

            if (result != null) {

                for (String dayForecastStr : result) {

                    weatherTextView.setAnimateType(HTextViewType.RAINBOW);
                    weatherTextView.setMarqueeRepeatLimit(15);
                    weatherTextView.animateText(dayForecastStr);//animate

                    //Toast.makeText(getApplicationContext(), dayForecastStr, Toast.LENGTH_LONG).show();
                }
                // New data is back from the server.
            }
        }//end onPostExecute method
    }//end FetchWeather class

    class FetchDurationAsyncTask extends AsyncTask<String, Void, String[]> {

        private final String LOG_TAG = FetchDurationAsyncTask.class.getSimpleName();

        private String[] getDurationDataFromJson(String forecastJsonStr)
                throws JSONException {

            // These are the names of the JSON objects that need to be extracted.
            final String OWM_ROUTES = "routes";
            final String OWM_LEGS = "legs";
            final String OWM_DURATION = "duration";
            final String OWM_TEXT = "text";


            String[] resultStrs = new String[1];
            String duration;

            JSONObject durationJson = new JSONObject(forecastJsonStr);

            JSONArray routeArray = durationJson.getJSONArray(OWM_ROUTES);

            JSONArray legArray = routeArray.getJSONObject(0).getJSONArray(OWM_LEGS);

            //Duration
            JSONObject durationObj = legArray.getJSONObject(0).getJSONObject(OWM_DURATION);

            duration = durationObj.getString(OWM_TEXT);

            resultStrs[0] = duration;

            System.out.println("Duration is: " + duration);
            for (String s : resultStrs) {
                System.out.println("Duration entry: " + s);
                Log.v(LOG_TAG, "Duration entry: " + s);
            }
            return resultStrs;

        }


        @Override
        protected String[] doInBackground(String... params) {



            String shuLat = "41.2207188";
            String shuLong = "-73.24168179999999";
            String forecastJsonStr = null;

            String myUrlSetup = "https://maps.googleapis.com/maps/api/directions/json?origin="+latitude + "," + longitude +"&destination="+shuLat +"," + shuLong + "&departure_time=now&traffic_model=best_guess&key=AIzaSyB6l8vrnspw2-1Q_cnzO03JlAsIOMl-7bs";
            System.out.println("myUrlSetup :" + myUrlSetup);
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            try {

                URL url;
                url = new URL(myUrlSetup);


                // Create the request to GoogleMapAPI, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                forecastJsonStr = buffer.toString();

                Log.v(LOG_TAG, "Forecast string: " + forecastJsonStr);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                  return getDurationDataFromJson(forecastJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return new String[0];
        }//end doInBackground

        @Override
        protected void onPostExecute(String[] result) {
            //TextView durationTextView = (TextView) findViewById(R.id.text_view_duration);
            HTextView durationTextView = (HTextView) findViewById(R.id.text_view_duration);

            if (result != null) {

                for (String durationStr : result) {

                    durationTextView.setAnimateType(HTextViewType.RAINBOW);
                    durationTextView.setMarqueeRepeatLimit(15);
                    durationTextView.animateText(durationStr);//animate
                    //Toast.makeText(getApplicationContext(), durationStr.toString(), Toast.LENGTH_LONG).show();
                }
                // New data is back from the server.
            }
        }//end onPostExecute method
    }//end class


    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my_custom_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_news) {
            Intent intent = new Intent("com.example.android.shustudenthelper.NewsWebActivity");
            startActivity(intent);

        } else if (id == R.id.nav_twitter) {
            Intent intent = new Intent("com.example.android.shustudenthelper.TwitterWebActivity");
            startActivity(intent);

        } else if (id == R.id.nav_facebook) {
            Intent intent = new Intent("com.example.android.shustudenthelper.FacebookWebActivity");
            startActivity(intent);
        } else if (id == R.id.nav_change_password) {
            Intent intent = new Intent("com.example.android.shustudenthelper.ChangePasswordActivity");
            startActivity(intent);
        } else if (id == R.id.nav_about) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 11: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    System.out.println("Permission granted");
                    try{
                        mLocMgr = (LocationManager) getSystemService(LOCATION_SERVICE);
                        mLocMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                                100, 0, this);
//                        wait(300);
//                        myLocation = mLocMgr.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//                        latitude = myLocation.getLatitude();
//                        longitude = myLocation.getLongitude();
//                        System.out.println("Lat is: " + latitude);
//                        System.out.println("Long is: " + longitude);
                        new FetchDurationAsyncTask().execute();
                    }catch (SecurityException e){
                        System.out.println("Permission error");
                    }
                } else {
                    System.out.println("Permission not granted");
                    // permission denied, Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

        }
    }
    private void displaySelectedCourses(){

        myDbHelper = new DatabaseOpenHelper(this);

        List<CourseDisplay> coursesDisplay_data = new ArrayList<CourseDisplay>();

        coursesDisplay_data = myDbHelper.getSelectedCoursesToDisplay();

        /*for (CourseDisplay C : coursesDisplay_data) {
            System.out.println("the selection status is " + C.getCourseName());
        }*/

        // 3. Initialize the Bakery adapter... unchanged.
        CourseDisplayRecyclerviewAdapter adapter = new CourseDisplayRecyclerviewAdapter(this, R.layout.custom_listview_layout_two, coursesDisplay_data);

        // 4. Initialize ItemAnimator, LayoutManager and ItemDecorators

        // 5. Setup our RecyclerView
        courseRecyclerView = (RecyclerView) findViewById(R.id.listview_selected_courses);

        // 6. For performance, tell OS RecyclerView won't change size

        //listingsView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

        int verticalSpacing = 20;
        VerticalSpaceItemDecorator itemDecorator =
                new VerticalSpaceItemDecorator(verticalSpacing);
        ShadowVerticalSpaceItemDecorator shadowItemDecorator =
                new ShadowVerticalSpaceItemDecorator(this, R.drawable.draw_shadow);
        // 7. Set the LayoutManager
        courseRecyclerView.setLayoutManager(layoutManager);

        // 8. Set the ItemDecorators
        courseRecyclerView.addItemDecoration(shadowItemDecorator);

        courseRecyclerView.addItemDecoration(itemDecorator);
        // 9. Attach the adapter to RecyclerView

        courseRecyclerView.setAdapter(adapter);
    }

    private void displayGamesForNextOneWeek(){

        myDbHelper2 = new DatabaseOpenHelper(this);

        List<GamesDisplay> gamesDisplay_data = new ArrayList<GamesDisplay>();

        gamesDisplay_data = myDbHelper2.getGamesForOneWeek();

        if (gamesDisplay_data == null || gamesDisplay_data.size()==0){

                gamesTextView.setText("Games : Not Available");
        }else {


        /*for (GamesDisplay C : gamesDisplay_data) {

            System.out.println("the selection status is " + C.getGamesDisplay());

        }*/
            // 3. Initialize the Bakery adapter... unchanged.
            GamesDisplayRecyclerviewAdapter adapter2 = new GamesDisplayRecyclerviewAdapter(this, R.layout.custom_listview_games, gamesDisplay_data);

            // 4. Initialize ItemAnimator, LayoutManager and ItemDecorators

            // 5. Setup our RecyclerView
            gamesView = (RecyclerView) findViewById(R.id.list_view_games);

            // 6. For performance, tell OS RecyclerView won't change size
            //listingsView.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(this);

            int verticalSpacing2 = 20;
            VerticalSpaceItemDecorator itemDecorator2 =
                    new VerticalSpaceItemDecorator(verticalSpacing2);
            ShadowVerticalSpaceItemDecorator shadowItemDecorator2 =
                    new ShadowVerticalSpaceItemDecorator(this, R.drawable.draw_shadow);
            // 7. Set the LayoutManager
            gamesView.setLayoutManager(layoutManager2);
            // 8. Set the ItemDecorators
            gamesView.addItemDecoration(shadowItemDecorator2);
            gamesView.addItemDecoration(itemDecorator2);
            // 9. Attach the adapter to RecyclerView
            gamesView.setAdapter(adapter2);
        }
    }

    private void displayEventsForNextOneWeek(){

        myDbHelper3 = new DatabaseOpenHelper(this);

        List<EventsDisplay> eventsDisplay_data = new ArrayList<EventsDisplay>();

        eventsDisplay_data = myDbHelper3.getEventsForOneWeek();

        if(eventsDisplay_data == null || eventsDisplay_data.size()==0){
            TextView eventsTextView = (TextView)findViewById(R.id.events_text_display);
            eventsTextView.setText("Events : Not Available");
        }else {


//        for (EventsDisplay C : eventsDisplay_data) {
//
//            System.out.println("event names are " + C.getEventName());
//
//        }

            //ArrayAdapter myNewAdapter3 = new EventDisplayAdapter(this, R.layout.custom_listview_events, eventsDisplay_data);
            EventsDisplayRecyclerViewAdapter adapter3 = new EventsDisplayRecyclerViewAdapter(this, R.layout.custom_listview_events, eventsDisplay_data);

            eventsRecyclerView = (RecyclerView) findViewById(R.id.list_view_events);

            RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(this);

            int verticalSpacing2 = 20;
            VerticalSpaceItemDecorator itemDecorator2 =
                    new VerticalSpaceItemDecorator(verticalSpacing2);
            ShadowVerticalSpaceItemDecorator shadowItemDecorator2 =
                    new ShadowVerticalSpaceItemDecorator(this, R.drawable.draw_shadow);

            eventsRecyclerView.setLayoutManager(layoutManager2);
            // 8. Set the ItemDecorators
            eventsRecyclerView.addItemDecoration(shadowItemDecorator2);
            eventsRecyclerView.addItemDecoration(itemDecorator2);
            // 9. Attach the adapter to RecyclerView
            eventsRecyclerView.setAdapter(adapter3);
        }
//
//
//        View header3 = getLayoutInflater().inflate(R.layout.activity_home_events_header, null);
//
//        header3.setClickable(false);
//
//        myListViewEvents.addHeaderView(header3, "EVENTS", false);
//
//        myListViewEvents.setAdapter(myNewAdapter3);
//
//        myListViewEvents.setClickable(true);
//
//        myListViewEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                /*Setting the color for row in the list view */
//                for (int i = 0; i < myListViewEvents.getChildCount(); i++) {
//                    if (position == i) {
//                        myListViewEvents.getChildAt(i).setBackgroundColor(Color.RED);
//                    } else {
//                        myListViewEvents.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
//                    }
//                }
//
//                LinearLayout ll = (LinearLayout) view; // get the parent layout view
//                AutofitTextView tv = (AutofitTextView) ll.findViewById(R.id.events_display); // get the child text view
//                clickedHomeActivityEvent = tv.getText().toString();
//                Toast.makeText(getApplicationContext(),
//                        clickedHomeActivityEvent, Toast.LENGTH_SHORT).show();
//
//                Intent intent = new Intent("com.example.android.shustudenthelper.EventInDepthDetailsActivity");
//                startActivity(intent);
//            }
//        });
    }

    private void requestPermissionFromUser(){

        int permissionCheck = ContextCompat.checkSelfPermission(MyCustomHomeActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck == PERMISSION_GRANTED){

        }else{
            ActivityCompat.requestPermissions(MyCustomHomeActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    11 );
        }

        mLocMgr = (LocationManager) getSystemService(LOCATION_SERVICE);

        try {

            myLocation = mLocMgr.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        } catch (SecurityException e) {
            if (Build.VERSION.SDK_INT >= 23 &&
                    ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            System.out.println("First Security Exception...");
        }
        try {

            mLocMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    500, 0, this);
            myLocation = mLocMgr.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            latitude = myLocation.getLatitude();
            longitude = myLocation.getLongitude();

        } catch (SecurityException e) {
            System.out.println("Security Exception...");
        }
    }
}
