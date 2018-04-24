package com.example.omnia.easytripplanner.database;

/**
 * Created by EslamWaheed on 3/5/2018.
 */

public class TripPlannerContract {

    //TABLE NAME
    public static final String TABLE_NAME_TRIP = "trip";
    public static final String TABLE_NAME_NOTE = "note";
    public static final String TABLE_NAME_PROFILE = "profile";

    //TRIP COLUMN NAME
    public static final String COLUMN_NAME_TRIP_ID = "trip_id";
    public static final String COLUMN_NAME_TRIP_NAME = "trip_name";
    public static final String COLUMN_NAME_TRIP_START_POINT = "trip_start_point";
    public static final String COLUMN_NAME_TRIP_END_POINT = "trip_end_point";
    public static final String COLUMN_NAME_TRIP_START_POINT_Longitude = "trip_start_point_longitude";
    public static final String COLUMN_NAME_TRIP_END_POINT_Longitude = "trip_end_point_longitude";
    public static final String COLUMN_NAME_TRIP_START_POINT_Latitude = "trip_start_point_latitude";
    public static final String COLUMN_NAME_TRIP_END_POINT_Latitude = "trip_end_point_latitude";
    public static final String COLUMN_NAME_TRIP_DATE = "trip_date";
    public static final String COLUMN_NAME_TRIP_TIME = "trip_time";
    public static final String COLUMN_NAME_TRIP_Rounded = "trip_rounded";
    public static final String COLUMN_NAME_TRIP_PICTURE = "trip_picture";
    public static final String COLUMN_NAME_TRIP_MILIISECOND = "trip_millisecond";
    public static final String COLUMN_NAME_TRIP_STATUS = "trip_status";


    //NOTE COLUMN NAME
    public static final String COLUMN_NAME_NOTE_ID = "note_id";
    public static final String COLUMN_NAME_NOTE_DESCRIPTION = "note_description";
    public static final String COLUMN_NAME_NOTE_TRIP_ID = "note_trip_id";

    //PROFILE COLUMN NAME
    public static final String COLUMN_NAME_PROFILE_ID = "profile_id";
    public static final String COLUMN_NAME_PROFILE_NAME = "profile_name";
    public static final String COLUMN_NAME_PROFILE_EMAIL = "profile_email";
    public static final String COLUMN_NAME_PROFILE_PICTURE = "profile_picture";
    public static final String COLUMN_NAME_PROFILE_PASSWORD = "profile_password";


    //CREATE TRIP TABLE
    public static final String SQL_CREATE_TABLE_TRIP =
            "CREATE TABLE " + TABLE_NAME_TRIP + "(" +
                    COLUMN_NAME_TRIP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME_TRIP_NAME + " TEXT UNIQUE NOT NULL, " +
                    COLUMN_NAME_TRIP_START_POINT + " TEXT NOT NULL, " +
                    COLUMN_NAME_TRIP_END_POINT + " TEXT NOT NULL, " +
                    COLUMN_NAME_TRIP_START_POINT_Longitude + " DOUBLE NOT NULL, " +
                    COLUMN_NAME_TRIP_END_POINT_Longitude + " DOUBLE NOT NULL, " +
                    COLUMN_NAME_TRIP_START_POINT_Latitude + " DOUBLE NOT NULL, " +
                    COLUMN_NAME_TRIP_END_POINT_Latitude + " DOUBLE NOT NULL, " +
                    COLUMN_NAME_TRIP_DATE + " TEXT NOT NULL, " +
                    COLUMN_NAME_TRIP_TIME + " TEXT NOT NULL, " +
                    COLUMN_NAME_TRIP_Rounded + " DOUBLE NOT NULL, " +
                    COLUMN_NAME_TRIP_PICTURE + " BLOB, " +
                    COLUMN_NAME_TRIP_MILIISECOND + " INTEGER, " +
                    COLUMN_NAME_PROFILE_ID + " INTEGER NOT NULL, " +
                    COLUMN_NAME_TRIP_STATUS + " TEXT NOT NULL" + ");";

    //CREATE NOTE TABLE
    public static final String SQL_CREATE_TABLE_NOTE =
            "CREATE TABLE " + TABLE_NAME_NOTE + "(" +
                    COLUMN_NAME_NOTE_ID + " INTEGER NOT NULL, " +
                    COLUMN_NAME_NOTE_DESCRIPTION + " TEXT NOT NULL, " +
                    COLUMN_NAME_NOTE_TRIP_ID + " INTEGER NOT NULL, " +
                    "FOREIGN KEY (" + COLUMN_NAME_NOTE_TRIP_ID + ") REFERENCES " + TABLE_NAME_TRIP + " (" + COLUMN_NAME_TRIP_ID + ") );";


    //CREATE PROFILE TABLE
    public static final String SQL_CREATE_TABLE_PROFILE =
            "CREATE TABLE " + TABLE_NAME_PROFILE + "(" +
                    COLUMN_NAME_PROFILE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME_PROFILE_NAME + " TEXT NOT NULL, " +
                    COLUMN_NAME_PROFILE_EMAIL + " TEXT UNIQUE NOT NULL, " +
                    COLUMN_NAME_PROFILE_PICTURE + " BLOB, " +
                    COLUMN_NAME_PROFILE_PASSWORD + " TEXT NOT NULL " + ");";

    //DROP TRIP TABLE
    public static final String SQL_DROP_TABLE_TRIP =
            "DROP TABLE IF EXISTS " + TABLE_NAME_TRIP;

    //DROP NOTE TABLE
    public static final String SQL_DROP_TABLE_NOTE =
            "DROP TABLE IF EXISTS " + TABLE_NAME_NOTE;

    //DROP PROFILE TABLE
    public static final String SQL_DROP_TABLE_PROFILE =
            "DROP TABLE IF EXISTS " + TABLE_NAME_PROFILE;
}
