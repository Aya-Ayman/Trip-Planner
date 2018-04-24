package com.example.omnia.easytripplanner.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by EslamWaheed on 3/5/2018.
 */

public class TripPlannerDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "trip_planner.db";

    public TripPlannerDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TripPlannerContract.SQL_CREATE_TABLE_TRIP);
        db.execSQL(TripPlannerContract.SQL_CREATE_TABLE_NOTE);
        db.execSQL(TripPlannerContract.SQL_CREATE_TABLE_PROFILE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(TripPlannerContract.SQL_DROP_TABLE_TRIP);
        db.execSQL(TripPlannerContract.SQL_DROP_TABLE_NOTE);
        db.execSQL(TripPlannerContract.SQL_DROP_TABLE_PROFILE);
        onCreate(db);
    }
}
