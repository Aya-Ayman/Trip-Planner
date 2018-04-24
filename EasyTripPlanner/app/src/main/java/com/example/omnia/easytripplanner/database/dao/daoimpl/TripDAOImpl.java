package com.example.omnia.easytripplanner.database.dao.daoimpl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.omnia.easytripplanner.database.TripPlannerContract;
import com.example.omnia.easytripplanner.database.TripPlannerDbHelper;
import com.example.omnia.easytripplanner.database.dao.daoint.TripDAOInt;
import com.example.omnia.easytripplanner.database.dto.Status;
import com.example.omnia.easytripplanner.database.dto.TripDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by EslamWaheed on 3/6/2018.
 */

public class TripDAOImpl implements TripDAOInt {

    TripPlannerDbHelper dbHelper;
    SQLiteDatabase database;

    /**
     * Constructor take Context
     *
     * @param context
     */
    public TripDAOImpl(Context context) {
        dbHelper = new TripPlannerDbHelper(context);
        database = dbHelper.getReadableDatabase();
    }

    /**
     * get user trip
     *
     * @param trip_id
     * @param profile_id
     * @return
     */
    @Override
    public TripDTO getTrip(int trip_id, int profile_id) {
        TripDTO tripDTO = new TripDTO();

        //where condition
        String whereCondition = TripPlannerContract.COLUMN_NAME_TRIP_ID + "= ? AND " +
                TripPlannerContract.COLUMN_NAME_PROFILE_ID + "= ?";

        //where param
        String[] whereParam = new String[]{String.valueOf(trip_id), String.valueOf(profile_id)};

        Cursor cursor = database.query(TripPlannerContract.TABLE_NAME_TRIP,
                null, whereCondition, whereParam, null, null, null);

        while (cursor.moveToNext()) {
            tripDTO.setTrip_id(cursor.getInt(0));
            tripDTO.setTrip_name(cursor.getString(1));
            tripDTO.setTrip_start_point(cursor.getString(2));
            tripDTO.setTrip_end_point(cursor.getString(3));
            tripDTO.setTrip_start_point_longitude(cursor.getDouble(4));
            tripDTO.setTrip_end_point_longitude(cursor.getDouble(5));
            tripDTO.setTrip_start_point_latitude(cursor.getDouble(6));
            tripDTO.setTrip_end_point_latitude(cursor.getDouble(7));
            tripDTO.setTrip_date(cursor.getString(8));
            tripDTO.setTrip_time(cursor.getString(9));
            tripDTO.setTrip_rounded(cursor.getInt(10));
            tripDTO.setTrip_picture(cursor.getBlob(11));
            tripDTO.setTrip_millisecond(cursor.getDouble(12));
            tripDTO.setProfile_id(cursor.getInt(13));
            tripDTO.setTrip_status(cursor.getString(14));
        }

        cursor.close();
        return tripDTO;
    }

    /**
     * get user trip by name
     *
     * @param name
     * @return
     */
    @Override
    public TripDTO getTripByName(String name, int profile_id) {
        TripDTO tripDTO = new TripDTO();

        //where condition
        String whereCondition = TripPlannerContract.COLUMN_NAME_TRIP_NAME + "= ? AND " +
                TripPlannerContract.COLUMN_NAME_PROFILE_ID + "= ?";

        //where param
        String[] whereParam = new String[]{String.valueOf(name), String.valueOf(profile_id)};

        Cursor cursor = database.query(TripPlannerContract.TABLE_NAME_TRIP,
                null, whereCondition, whereParam, null, null, null);

        while (cursor.moveToNext()) {
            tripDTO.setTrip_id(cursor.getInt(0));
            tripDTO.setTrip_name(cursor.getString(1));
            tripDTO.setTrip_start_point(cursor.getString(2));
            tripDTO.setTrip_end_point(cursor.getString(3));
            tripDTO.setTrip_start_point_longitude(cursor.getDouble(4));
            tripDTO.setTrip_end_point_longitude(cursor.getDouble(5));
            tripDTO.setTrip_start_point_latitude(cursor.getDouble(6));
            tripDTO.setTrip_end_point_latitude(cursor.getDouble(7));
            tripDTO.setTrip_date(cursor.getString(8));
            tripDTO.setTrip_time(cursor.getString(9));
            tripDTO.setTrip_rounded(cursor.getInt(10));
            tripDTO.setTrip_picture(cursor.getBlob(11));
            tripDTO.setTrip_millisecond(cursor.getDouble(12));
            tripDTO.setProfile_id(cursor.getInt(13));
            tripDTO.setTrip_status(cursor.getString(14));
        }
        cursor.close();
        return tripDTO;
    }

    /**
     * insert user trip
     *
     * @param tripDTO
     * @return boolean
     */
    @Override
    public boolean insertTrip(TripDTO tripDTO) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(TripPlannerContract.COLUMN_NAME_TRIP_NAME, tripDTO.getTrip_name());
        contentValues.put(TripPlannerContract.COLUMN_NAME_TRIP_START_POINT, tripDTO.getTrip_start_point());
        contentValues.put(TripPlannerContract.COLUMN_NAME_TRIP_END_POINT, tripDTO.getTrip_end_point());
        contentValues.put(TripPlannerContract.COLUMN_NAME_TRIP_START_POINT_Longitude, tripDTO.getTrip_start_point_longitude());
        contentValues.put(TripPlannerContract.COLUMN_NAME_TRIP_END_POINT_Longitude, tripDTO.getTrip_end_point_longitude());
        contentValues.put(TripPlannerContract.COLUMN_NAME_TRIP_START_POINT_Latitude, tripDTO.getTrip_start_point_latitude());
        contentValues.put(TripPlannerContract.COLUMN_NAME_TRIP_END_POINT_Latitude, tripDTO.getTrip_end_point_latitude());
        contentValues.put(TripPlannerContract.COLUMN_NAME_TRIP_DATE, tripDTO.getTrip_date());
        contentValues.put(TripPlannerContract.COLUMN_NAME_TRIP_TIME, tripDTO.getTrip_time());
        contentValues.put(TripPlannerContract.COLUMN_NAME_TRIP_Rounded, tripDTO.getTrip_rounded());
        contentValues.put(TripPlannerContract.COLUMN_NAME_TRIP_PICTURE, tripDTO.getTrip_picture());
        contentValues.put(TripPlannerContract.COLUMN_NAME_TRIP_MILIISECOND, tripDTO.getTrip_millisecond());
        contentValues.put(TripPlannerContract.COLUMN_NAME_PROFILE_ID, tripDTO.getProfile_id());
        contentValues.put(TripPlannerContract.COLUMN_NAME_TRIP_STATUS, tripDTO.getTrip_status());

        long insert_trip = database.insert(TripPlannerContract.TABLE_NAME_TRIP, null, contentValues);

        return insert_trip == 1;
    }

    /**
     * update user trip
     *
     * @param tripDTO
     * @return boolean
     */
    @Override
    public boolean updateTrip(TripDTO tripDTO) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(TripPlannerContract.COLUMN_NAME_TRIP_NAME, tripDTO.getTrip_name());
        contentValues.put(TripPlannerContract.COLUMN_NAME_TRIP_START_POINT, tripDTO.getTrip_start_point());
        contentValues.put(TripPlannerContract.COLUMN_NAME_TRIP_END_POINT, tripDTO.getTrip_end_point());
        contentValues.put(TripPlannerContract.COLUMN_NAME_TRIP_START_POINT_Longitude, tripDTO.getTrip_start_point_longitude());
        contentValues.put(TripPlannerContract.COLUMN_NAME_TRIP_END_POINT_Longitude, tripDTO.getTrip_end_point_longitude());
        contentValues.put(TripPlannerContract.COLUMN_NAME_TRIP_START_POINT_Latitude, tripDTO.getTrip_start_point_latitude());
        contentValues.put(TripPlannerContract.COLUMN_NAME_TRIP_END_POINT_Latitude, tripDTO.getTrip_end_point_latitude());
        contentValues.put(TripPlannerContract.COLUMN_NAME_TRIP_DATE, tripDTO.getTrip_date());
        contentValues.put(TripPlannerContract.COLUMN_NAME_TRIP_TIME, tripDTO.getTrip_time());
        contentValues.put(TripPlannerContract.COLUMN_NAME_TRIP_Rounded, tripDTO.getTrip_rounded());
        contentValues.put(TripPlannerContract.COLUMN_NAME_TRIP_PICTURE, tripDTO.getTrip_picture());
        contentValues.put(TripPlannerContract.COLUMN_NAME_TRIP_MILIISECOND, tripDTO.getTrip_millisecond());
        contentValues.put(TripPlannerContract.COLUMN_NAME_PROFILE_ID, tripDTO.getProfile_id());
        contentValues.put(TripPlannerContract.COLUMN_NAME_TRIP_STATUS, tripDTO.getTrip_status());

        //where condition
        String whereCondition = TripPlannerContract.COLUMN_NAME_TRIP_ID + "= ?";

        //where param
        String[] whereParam = new String[]{String.valueOf(tripDTO.getTrip_id())};

        int update_trip = database.update(TripPlannerContract.TABLE_NAME_TRIP, contentValues, whereCondition, whereParam);
        return update_trip == 1;
    }

    /**
     * delete user trip
     *
     * @param id
     * @return boolean
     */
    @Override
    public boolean deleteTrip(int id) {

        //where condition
        String whereCondition = TripPlannerContract.COLUMN_NAME_TRIP_ID + "= ?";

        //where param
        String[] whereParam = new String[]{String.valueOf(id)};

        int delete_trip = database.delete(TripPlannerContract.TABLE_NAME_TRIP, whereCondition, whereParam);

        return delete_trip == 1;
    }

    /**
     * get number of trips
     *
     * @return int
     */
    @Override
    public int NumberOfTrip() {
        int count = 0;

        Cursor cursor = database.query(TripPlannerContract.TABLE_NAME_TRIP,
                null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            count++;
        }

        cursor.close();
        return count;
    }

    /**
     * get all upcoming trips
     *
     * @param profile_id
     * @return
     */
    @Override
    public List<TripDTO> getAllUpcomingTrips(int profile_id) {

        List<TripDTO> tripDTOS = new ArrayList<TripDTO>();

        //where condition
        String whereCondition = TripPlannerContract.COLUMN_NAME_TRIP_STATUS + "= ? AND " +
                TripPlannerContract.COLUMN_NAME_PROFILE_ID + "= ?";

        //where param
        String[] whereParam = new String[]{Status.UPCOMING, String.valueOf(profile_id)};

        Cursor cursor = database.query(TripPlannerContract.TABLE_NAME_TRIP,
                null, whereCondition, whereParam, null, null, null);

        while (cursor.moveToNext()) {
            TripDTO tripDTO = new TripDTO();
            tripDTO.setTrip_id(cursor.getInt(0));
            tripDTO.setTrip_name(cursor.getString(1));
            tripDTO.setTrip_start_point(cursor.getString(2));
            tripDTO.setTrip_end_point(cursor.getString(3));
            tripDTO.setTrip_start_point_longitude(cursor.getDouble(4));
            tripDTO.setTrip_end_point_longitude(cursor.getDouble(5));
            tripDTO.setTrip_start_point_latitude(cursor.getDouble(6));
            tripDTO.setTrip_end_point_latitude(cursor.getDouble(7));
            tripDTO.setTrip_date(cursor.getString(8));
            tripDTO.setTrip_time(cursor.getString(9));
            tripDTO.setTrip_rounded(cursor.getInt(10));
            tripDTO.setTrip_picture(cursor.getBlob(11));
            tripDTO.setTrip_millisecond(cursor.getDouble(12));
            tripDTO.setProfile_id(cursor.getInt(13));
            tripDTO.setTrip_status(cursor.getString(14));
            tripDTOS.add(tripDTO);
        }

        cursor.close();
        return tripDTOS;
    }

    /**
     * get all done and cancelled trips
     *
     * @param profile_id
     * @return
     */
    @Override
    public List<TripDTO> getAllDoneAndCancelledTrips(int profile_id) {
        List<TripDTO> tripDTOS = new ArrayList<>();
//
        //where condition
        String whereCondition =
                TripPlannerContract.COLUMN_NAME_PROFILE_ID + " = ? AND "
         +"( " +TripPlannerContract.COLUMN_NAME_TRIP_STATUS + " = ? OR " +
                        TripPlannerContract.COLUMN_NAME_TRIP_STATUS + " = ? ) " ;

        //where param
        String[] whereParam = new String[]{String.valueOf(profile_id),Status.DONE, Status.CANCELLED};

        Cursor cursor = database.query(TripPlannerContract.TABLE_NAME_TRIP,
                null, whereCondition, whereParam, null, null, null);

        while (cursor.moveToNext()) {
            TripDTO tripDTO = new TripDTO();
            tripDTO.setTrip_id(cursor.getInt(0));
            tripDTO.setTrip_name(cursor.getString(1));
            tripDTO.setTrip_start_point(cursor.getString(2));
            tripDTO.setTrip_end_point(cursor.getString(3));
            tripDTO.setTrip_start_point_longitude(cursor.getDouble(4));
            tripDTO.setTrip_end_point_longitude(cursor.getDouble(5));
            tripDTO.setTrip_start_point_latitude(cursor.getDouble(6));
            tripDTO.setTrip_end_point_latitude(cursor.getDouble(7));
            tripDTO.setTrip_date(cursor.getString(8));
            tripDTO.setTrip_time(cursor.getString(9));
            tripDTO.setTrip_rounded(cursor.getInt(10));
            tripDTO.setTrip_picture(cursor.getBlob(11));
            tripDTO.setTrip_millisecond(cursor.getDouble(12));
            tripDTO.setProfile_id(cursor.getInt(13));
            tripDTO.setTrip_status(cursor.getString(14));
            tripDTOS.add(tripDTO);
        }

        cursor.close();
        return tripDTOS;
    }

    /**
     * Get all trip's names
     *
     * @return
     */
    @Override
    public List<String> getAllTripsNames() {

        List<String> tripNames = new ArrayList<String>();


        //columns
        String[] columns = new String[]{TripPlannerContract.COLUMN_NAME_TRIP_NAME};
        Cursor cursor = database.query(TripPlannerContract.TABLE_NAME_TRIP,
                columns, null, null, null, null, null);

        while (cursor.moveToNext()) {
            tripNames.add(cursor.getString(0));
        }

        cursor.close();
        return tripNames;
    }
}