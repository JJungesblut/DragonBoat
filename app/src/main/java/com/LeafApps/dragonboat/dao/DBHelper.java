package com.LeafApps.dragonboat.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DBHelper extends SQLiteOpenHelper {

    public static final String TAG = "DBHelper";

    // columns of the boats table
    public static final String TABLE_BOATS = "boats";
    public static final String COLUMN_BOAT_ID = "_id";
    public static final String COLUMN_BOAT_NAME = "boat_name";
    public static final String COLUMN_BOAT_TEAMNAME = "team_name";
    public static final String COLUMN_BOAT_TIME = "time";
    public static final String COLUMN_BOAT_URI = "uri";

    // columns of the crew members table
    public static final String TABLE_CREWMEMBERS = "crew_members";
    public static final String COLUMN_CREWMEMBER_ID = "crew_id";
    public static final String COLUMN_CREW_BOAT_ID = COLUMN_BOAT_ID;
    public static final String COLUMN_CREWMEMBER_FIRST_NAME = "first_name";
    public static final String COLUMN_CREWMEMBER_LAST_NAME = "last_name";
    public static final String COLUMN_CREWMEMBER_WEIGHT = "weight";
    // public static final String COLUMN_CREWMEMBER_TASK = "task";

    private static final String DATABASE_NAME = "dragonboat.db";
    private static final int DATABASE_VERSION = 1;

    // SQL statement of the Crewmember table creation
    private static final String SQL_CREATE_TABLE_BOATS = "CREATE TABLE " + TABLE_BOATS + "("
            + COLUMN_BOAT_ID + " INTEGER PRIMARY KEY, "
            + COLUMN_BOAT_NAME + " TEXT NOT NULL, "
            + COLUMN_BOAT_TEAMNAME + " TEXT NOT NULL, "
            + COLUMN_BOAT_TIME + " STRING, "
            + COLUMN_BOAT_URI + " STRING"
            + ");";

    // SQL statement of the Boats table creation
    private static final String SQL_CREATE_TABLE_CREWMEMBERS = "CREATE TABLE " + TABLE_CREWMEMBERS + "("
            + COLUMN_CREWMEMBER_ID + " INTEGER NOT NULL, "
            + COLUMN_CREW_BOAT_ID + " INTEGER NOT NULL, "
            + COLUMN_CREWMEMBER_FIRST_NAME + " TEXT NOT NULL, "
            + COLUMN_CREWMEMBER_LAST_NAME + " TEXT, "              //hier ist das not null rausgenommen
            + COLUMN_CREWMEMBER_WEIGHT + " INTEGER NOT NULL, "
            //+ COLUMN_CREWMEMBER_TASK + " TEXT NOT NULL, "
            + "PRIMARY KEY (" + COLUMN_CREWMEMBER_ID +", "
            + COLUMN_CREW_BOAT_ID + ") "
            + ");";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(SQL_CREATE_TABLE_BOATS);
        database.execSQL(SQL_CREATE_TABLE_CREWMEMBERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG,
                "Upgrading the database from version " + oldVersion + " to " + newVersion);
        // clear all data
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOATS + ";");//das ist gefährlich, Datenverlust
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CREWMEMBERS + ";");

        // recreate the tables
        onCreate(db);
    }

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }
}
