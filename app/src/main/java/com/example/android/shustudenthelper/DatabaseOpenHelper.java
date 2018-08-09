package com.example.android.shustudenthelper;
/**
 * Created by Surya Gogineni on 10/8/2016.
 */

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.Display;

public class DatabaseOpenHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "SHU_Courses_Database1.sqlite";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_COURSES = "tbl_courses";
    public static final String COLUMN_COURSE_ID = "_id";
    public static final String COLUMN_COURSE_NAME = "course_name";
    public static final String COLUMN_COURSE_CODE = "course_code";
    private static final String COLUMN_COURSE_TEXTBOOK = "course_textbook";
    private static final String COLUMN_COURSE_TIMINGS = "course_timings";
    private static final String COLUMN_COURSE_CLA = "course_cla";
    private static final String COLUMN_COURSE_CLASSROOM_NUMBER = "classroom_number";
    private static final String COLUMN_COURSE_PROFESSOR_NAME = "professor_name";
    public static final String COLUMN_COURSE_SELECTED = "checked_status";

    private static final String TABLE_ASSIGNMENTS = "tbl_assignments";
    private static final String COLUMN_ASSIGNMENT_DUEDATE = "assignment_duedate";
    private static final String COLUMN_ASSIGNMENT_LATE_DUEDATE = "assignment_last_duedate";
    private static final String COLUMN_ASSIGNMENT_NAME = "assignment_name";
    private static final String COLUMN_ASSIGNMENT_PENALTY = "assignment_penalty";
    private static final String COLUMN_ASSIGNMENT_LATE_SUBMISSION = "assignment_late_submission";
    private static final String COLUMN_ASSIGNMENT_SUBMISSION_STATUS = "assignment_submission_status";
    private static final String COLUMN_ASSIGNMENT_ID = "assignment_id";
    private static final String COLUMN_ASSIGNMENT_COURSE_ID = "_id";

    private static final String TABLE_EVENTS = "tbl_events";
    private static final String COLUMN_EVENT_DETAILS = "event_details";
    private static final String COLUMN_EVENT_START_DATE = "event_start_date";
    private static final String COLUMN_EVENT_START_TIME = "event_start_time";
    private static final String COLUMN_EVENT_VENUE = "event_venue";
    private static final String COLUMN_EVENT_PRICE = "event_price";
    private static final String COLUMN_EVENT_ID = "event_id";

    private static final String TABLE_GAMES = "tbl_games";
    private static final String COLUMN_GAME_DETAILS = "game_details";
    private static final String COLUMN_GAME_START_DATE = "game_start_date";
    private static final String COLUMN_GAME_START_TIME = "game_start_time";
    private static final String COLUMN_GAME_VENUE = "game_venue";
    private static final String COLUMN_GAME_TICKET_PRICE = "game_ticket_price";
    private static final String COLUMN_GAME_ID = "game_id";
    private static final String COLUMN_GAME_HOST = "game_host";
    private static final String COLUMN_GAME_VISITOR = "game_visitor";


    private SQLiteDatabase database;

    public final Context context;

    // database path
    private static String DATABASE_PATH = "/data/data/com.example.android.shustudenthelper/databases/";

    /** constructor */
    public DatabaseOpenHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = ctx;
    }
    @Override
    public void onCreate(SQLiteDatabase arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

    }

    /** open the database */
    public void openDataBase() throws SQLException {
        String myPath = context.getDatabasePath(DATABASE_NAME).getPath();
        if (database != null && database.isOpen()){
            return;
        }
        database = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    /** close the database */
    @Override
    public synchronized void close() {
        if (database != null)
            database.close();
        //super.close();
    }

    /**
     * Creates a empty database on the system and rewrites it with your own
     * database.
     * */
    public void create() throws IOException {
        //boolean check = checkDataBase();

        SQLiteDatabase db_Read = null;

        // Creates empty database default system path
        db_Read = this.getWritableDatabase();
        db_Read.close();
        try {
            //if (!check) {
                copyDataBase();
            //}
        } catch (IOException e) {
            throw new Error("Error copying database");
        }
    }

    /**
     * Check if the database already exist to avoid re-copying the file each
     * time you open the application.
     *
     * @return true if it exists, false if it doesn't
     */
    public boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try {
            String myPath = DATABASE_PATH;
            checkDB = SQLiteDatabase.openDatabase(myPath, null,
                    SQLiteDatabase.OPEN_READWRITE);
        } catch (SQLiteException e) {
            // database doesn't exist yet.
        }

        if (checkDB != null) {
            checkDB.close();
        }
        return checkDB != null ? true : false;
    }

    /**
     * Copies your database from your local assets-folder to the just created
     * empty database in the system folder, from where it can be accessed and
     * handled. This is done by transfering bytestream.
     * */
    private void copyDataBase() throws IOException {

        // Open your local db as the input stream
        InputStream myInput = context.getAssets().open(DatabaseOpenHelper.DATABASE_NAME);

        // Path to the just created empty db
        String outFileName = DatabaseOpenHelper.DATABASE_PATH + DatabaseOpenHelper.DATABASE_NAME;

        // Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        // transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length = 0;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        // Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }


    public List<CourseDisplay> getAllCoursesForRegistration() {
        List<CourseDisplay> courses = new ArrayList<>();
        openDataBase();
        getWritableDatabase();
        Cursor mCursor = database.query(TABLE_COURSES, new String[] {COLUMN_COURSE_CODE,COLUMN_COURSE_NAME,COLUMN_COURSE_SELECTED }, null, null, null, null, null);
        if(mCursor != null){
            mCursor.moveToFirst();
            do{
                String courseCode = mCursor.getString(0);
                String courseName = mCursor.getString(1);
                String courseSelected = mCursor.getString(2);
                courses.add(new CourseDisplay(courseCode, courseName,courseSelected));
            }while(mCursor.moveToNext());
        }
        mCursor.close();
        close();//close Database
        return courses;
    }

    // Updates Course Selected column
    public void updateSelectedCourseField(String courseName, String courseSelected) {

        openDataBase();//open database
        getWritableDatabase();
        ContentValues args = new ContentValues();

        args.put(COLUMN_COURSE_SELECTED, courseSelected);

        database.update(TABLE_COURSES, args, COLUMN_COURSE_NAME + "='" + courseName + "'",null);
        close();//Close database

    }

    public List<CourseDisplay> getSelectedCoursesToDisplay() {
        List<CourseDisplay> courses = new ArrayList<>();
        openDataBase();/**/
        getWritableDatabase();
        Cursor mCursor = database.query(TABLE_COURSES, new String[] {COLUMN_COURSE_NAME },COLUMN_COURSE_SELECTED + "='" + "true" + "'", null, null, null, null);
        if(mCursor != null){
            mCursor.moveToFirst();
            do{
                String courseName = mCursor.getString(0);
                courses.add(new CourseDisplay(courseName));
            }while(mCursor.moveToNext());
        }
        mCursor.close();
        close();//close Database
        return courses;
    }

    public List<CourseDisplay> getCourseInDepthDetailsToDisplay() {
        List<CourseDisplay> courses = new ArrayList<>();
        openDataBase();/**/
        getWritableDatabase();
        String courseSelectedInHomeScreen = MyCustomHomeActivity.clickedHomeActivityCourse;
        Cursor mCursor = database.query(TABLE_COURSES, new String[] {COLUMN_COURSE_NAME,COLUMN_COURSE_CODE,
                              COLUMN_COURSE_PROFESSOR_NAME,COLUMN_COURSE_CLA,COLUMN_COURSE_TIMINGS,
                              COLUMN_COURSE_CLASSROOM_NUMBER},COLUMN_COURSE_NAME + "='" + courseSelectedInHomeScreen + "'", null, null, null, null);
        if(mCursor != null){
            mCursor.moveToFirst();
            do{
                String courseName = mCursor.getString(0);
                String courseCode = mCursor.getString(1);
                String courseProfessor = mCursor.getString(2);
                String courseCLA = mCursor.getString(3);
                String courseTimings = mCursor.getString(4);
                String courseClassRoom = mCursor.getString(5);
                courses.add(new CourseDisplay(courseName, courseCode,courseProfessor,courseCLA,courseTimings,courseClassRoom));
            }while(mCursor.moveToNext());
        }
        mCursor.close();
        close();//close Database
        return courses;
    }

    public List<GamesDisplay> getGameInDepthDetailsToDisplay() {
        List<GamesDisplay> games = new ArrayList<>();
        openDataBase();/**/
        getWritableDatabase();
        String gameSelectedInHomeScreen = MyCustomHomeActivity.clickedHomeActivityGame;
        String gameSelectedDate = MyCustomHomeActivity.clickedHomeActivityGameDate;
        Cursor mCursor = database.query(TABLE_GAMES, new String[] {COLUMN_GAME_DETAILS,COLUMN_GAME_START_TIME,
                COLUMN_GAME_VENUE,COLUMN_GAME_HOST,COLUMN_GAME_VISITOR,
                COLUMN_GAME_TICKET_PRICE},COLUMN_GAME_DETAILS + "='" + gameSelectedInHomeScreen + "'"
                + " AND " + COLUMN_GAME_START_DATE + "='" + gameSelectedDate + "'", null, null, null, null);
        if(mCursor != null){
            mCursor.moveToFirst();
            do{
                String gameName = mCursor.getString(0);
                String gameTime = mCursor.getString(1);
                String gameVenue = mCursor.getString(2);
                String gameHost = mCursor.getString(3);
                String gameVisitor = mCursor.getString(4);
                String gamePrice = mCursor.getString(5);
                games.add(new GamesDisplay(gameName, gameTime,gameVenue,gameHost,gameVisitor,gamePrice));
            }while(mCursor.moveToNext());
        }
        mCursor.close();
        close();//close Database
        return games;
    }

    public List<EventsDisplay> getEventInDepthDetailsToDisplay() {

        List<EventsDisplay> events = new ArrayList<>();
        openDataBase();/**/
        getWritableDatabase();
        String eventSelectedInHomeScreen = MyCustomHomeActivity.clickedHomeActivityEvent;

        Cursor mCursor = database.query(TABLE_EVENTS, new String[] {COLUMN_EVENT_DETAILS,COLUMN_EVENT_START_TIME,
                COLUMN_EVENT_START_DATE,COLUMN_EVENT_VENUE,
                COLUMN_EVENT_PRICE},COLUMN_EVENT_DETAILS + "='" + eventSelectedInHomeScreen + "'",null,null,null,null);

        if(mCursor != null){
            mCursor.moveToFirst();
            do{
                String eventName = mCursor.getString(0);
                String eventTime = mCursor.getString(1);
                String eventDate = mCursor.getString(2);
                String eventVenue = mCursor.getString(3);
                String eventPrice = mCursor.getString(4);

                events.add(new EventsDisplay(eventName, eventTime,eventDate,eventVenue,eventPrice));
            }while(mCursor.moveToNext());
        }
        mCursor.close();
        close();//close Database
        return events;
    }

    public List<AssignmentDisplay> getAssignmentNamesToDisplay() {

        List<AssignmentDisplay> courses = new ArrayList<AssignmentDisplay>();
        openDataBase();/**/
        getWritableDatabase();
        String courseSelectedInHomeScreen = MyCustomHomeActivity.clickedHomeActivityCourse;

        Cursor mCursor = database.rawQuery("SELECT " + COLUMN_ASSIGNMENT_NAME +" FROM " + TABLE_ASSIGNMENTS + "," + TABLE_COURSES +
                " WHERE " +  TABLE_COURSES+"._id IN " + "(SELECT _id FROM "+TABLE_COURSES +
                " WHERE " + COLUMN_COURSE_NAME + "=" + "'"+courseSelectedInHomeScreen+"'" + " AND " + TABLE_COURSES+"."+COLUMN_COURSE_ID +
                "=" + TABLE_ASSIGNMENTS+"."+COLUMN_ASSIGNMENT_COURSE_ID + ")",
                null, null);
        if(mCursor != null){
            mCursor.moveToFirst();
            do{
                String assignmentName = mCursor.getString(0);
                courses.add(new AssignmentDisplay(assignmentName));
            }while(mCursor.moveToNext());
        }
        mCursor.close();
        close();//close Database
        return courses;
    }

    public List<String> getAssignmentDetailsOfEachToDisplay(String selectedAssignment) {

        List<String> courses = new ArrayList<String>();
        openDataBase();/**/
        getWritableDatabase();
        String courseSelectedInHomeScreen = MyCustomHomeActivity.clickedHomeActivityCourse;
        Cursor mCursor = database.rawQuery("SELECT " + COLUMN_ASSIGNMENT_DUEDATE + "," + COLUMN_ASSIGNMENT_LATE_SUBMISSION + "," + COLUMN_ASSIGNMENT_PENALTY
                        +" FROM " + TABLE_ASSIGNMENTS +","+TABLE_COURSES + " "+
                        " WHERE " + COLUMN_ASSIGNMENT_NAME + "=" + "'" + selectedAssignment + "'" +
                        " AND " +  TABLE_COURSES+"._id IN " + "(SELECT _id FROM "+TABLE_COURSES +
                        " WHERE " + COLUMN_COURSE_NAME + "=" + "'"+courseSelectedInHomeScreen+"'" + " AND " + TABLE_COURSES+"."+COLUMN_COURSE_ID +
                        "=" + TABLE_ASSIGNMENTS+"."+COLUMN_ASSIGNMENT_COURSE_ID + ")",null, null);
        if(mCursor != null){
            mCursor.moveToFirst();
            do{
                String assignmentName = mCursor.getString(0);
                String assignmentLateSub = mCursor.getString(1);
                String assignmentPenalty = mCursor.getString(2);

                courses.add(assignmentName);
                courses.add(assignmentLateSub);
                courses.add(assignmentPenalty);

            }while(mCursor.moveToNext());
        }
        mCursor.close();
        close();//close Database
        return courses;
    }

    public List<AssignmentNotificationObject> getAssignmentDetailsOfEachToPushNotifications(String selectedCourse) {

        List<AssignmentNotificationObject> courses = new ArrayList<AssignmentNotificationObject>();
        openDataBase();/**/
        getWritableDatabase();

        Cursor mCursor = database.rawQuery("SELECT " + COLUMN_COURSE_NAME + "," + COLUMN_ASSIGNMENT_NAME + "," +COLUMN_ASSIGNMENT_DUEDATE
                +" FROM " + TABLE_ASSIGNMENTS +","+TABLE_COURSES + " "+
                " WHERE " + COLUMN_COURSE_NAME + "=" + "'" + selectedCourse + "'" +
                " AND " +  TABLE_COURSES+"._id IN " + "(SELECT _id FROM "+TABLE_COURSES +
                " WHERE " + COLUMN_COURSE_NAME + "=" + "'"+selectedCourse+"'" + " AND " + TABLE_COURSES+"."+COLUMN_COURSE_ID +
                "=" + TABLE_ASSIGNMENTS+"."+COLUMN_ASSIGNMENT_COURSE_ID + ")",null, null);
        if(mCursor != null){
            mCursor.moveToFirst();
            do{
                String courseName = mCursor.getString(0);
                String assignmentName = mCursor.getString(1);
                String assignmentDueDate = mCursor.getString(2);

                courses.add(new AssignmentNotificationObject(courseName,assignmentName,assignmentDueDate));


            }while(mCursor.moveToNext());
        }
        mCursor.close();
        close();//close Database
        return courses;
    }

    public List<GamesDisplay> getGamesForOneWeek() {
        List<GamesDisplay> games = new ArrayList<GamesDisplay>();
        openDataBase();/**/
        getWritableDatabase();

        String currentDate = new SimpleDateFormat("MM/dd/yyyy").format(new Date());

        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date date = new Date();
        String todate = dateFormat.format(date);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, +6);
        Date todate1 = cal.getTime();
        String fromdate = dateFormat.format(todate1);

        Cursor mCursor = database.rawQuery("SELECT " + COLUMN_GAME_DETAILS + "," +COLUMN_GAME_START_DATE
                + " FROM " + TABLE_GAMES
                + " WHERE " + COLUMN_GAME_START_DATE + " BETWEEN " + "'" + currentDate + "'"
                + " AND " + "'" + fromdate + "'",null, null);

        if(mCursor != null){
            try {
                mCursor.moveToFirst();
                do {
                    String gameName = mCursor.getString(0);
                    String gameDate = mCursor.getString(1);
                    games.add(new GamesDisplay(gameName, gameDate));

                } while (mCursor.moveToNext());
            }catch (CursorIndexOutOfBoundsException e){
                return games;

            }
        }
        mCursor.close();
        close();//close Database
        return games;
    }


    public List<EventsDisplay> getEventsForOneWeek() {
        List<EventsDisplay> events = new ArrayList<EventsDisplay>();
        openDataBase();/**/
        getWritableDatabase();

        String currentDate = new SimpleDateFormat("MM/dd/yyyy").format(new Date());

        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date date = new Date();
        String todate = dateFormat.format(date);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, +6);
        Date todate1 = cal.getTime();
        String fromdate = dateFormat.format(todate1);

        Cursor mCursor = database.rawQuery("SELECT " + COLUMN_EVENT_DETAILS
                + " FROM " + TABLE_EVENTS
                + " WHERE " + COLUMN_EVENT_START_DATE + " BETWEEN " + "'" + currentDate + "'"
                + " AND " + "'" + fromdate + "'",null, null);

        if(mCursor != null){
            try{
            mCursor.moveToFirst();
            do{
                String eventName = mCursor.getString(0);
                events.add(new EventsDisplay(eventName));

            }while(mCursor.moveToNext());
        }catch (CursorIndexOutOfBoundsException e){
            return events;

        }
        }
        mCursor.close();
        close();//close Database
        return events;
    }

}//end class


