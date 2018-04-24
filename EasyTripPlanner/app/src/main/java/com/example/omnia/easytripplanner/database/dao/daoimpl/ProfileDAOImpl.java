package com.example.omnia.easytripplanner.database.dao.daoimpl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.nfc.Tag;
import android.util.Log;


import com.example.omnia.easytripplanner.database.TripPlannerContract;
import com.example.omnia.easytripplanner.database.TripPlannerDbHelper;
import com.example.omnia.easytripplanner.database.dao.daoint.ProfileDAOInt;
import com.example.omnia.easytripplanner.database.dto.ProfileDTO;

import java.io.ByteArrayOutputStream;

/**
 * Created by EslamWaheed on 3/6/2018.
 */

public class ProfileDAOImpl implements ProfileDAOInt {

    public static final String TAG = "ProfileDAOImpl";
    TripPlannerDbHelper dbHelper;
    SQLiteDatabase database;

    /**
     * Constructor take Context
     *
     * @param context
     */
    public ProfileDAOImpl(Context context) {
        dbHelper = new TripPlannerDbHelper(context);
        database = dbHelper.getReadableDatabase();
    }

    //for profile picture
    public static byte[] getPictureByteOfArray(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    /**
     * get user profile
     *
     * @param id
     * @return ProfileDTO
     */
    @Override
    public ProfileDTO getProfile(int id) {
        ProfileDTO profileDTO = new ProfileDTO();

        //where condition
        String whereCondition = TripPlannerContract.COLUMN_NAME_PROFILE_ID + "= ?";

        //where param
        String[] whereParam = new String[]{String.valueOf(id)};

        Cursor cursor = database.query(TripPlannerContract.TABLE_NAME_PROFILE,
                null, whereCondition, whereParam, null, null, null);

        while (cursor.moveToNext()) {
            profileDTO.setProfile_id(cursor.getInt(0));
            profileDTO.setProfile_name(cursor.getString(1));
            profileDTO.setProfile_email(cursor.getString(2));
            profileDTO.setProfile_picture(cursor.getBlob(3));
            profileDTO.setProfile_password(cursor.getString(4));
        }

        cursor.close();
        return profileDTO;
    }

    /**
     * insert user profile
     *
     * @param profileDTO
     * @return boolean
     */
    @Override
    public boolean insertProfile(ProfileDTO profileDTO) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(TripPlannerContract.COLUMN_NAME_PROFILE_NAME, profileDTO.getProfile_name());
        contentValues.put(TripPlannerContract.COLUMN_NAME_PROFILE_EMAIL, profileDTO.getProfile_email());
        contentValues.put(TripPlannerContract.COLUMN_NAME_PROFILE_PICTURE, profileDTO.getProfile_picture());
        contentValues.put(TripPlannerContract.COLUMN_NAME_PROFILE_PASSWORD, profileDTO.getProfile_password());

        long insert_profile = database.insert(TripPlannerContract.TABLE_NAME_PROFILE, null, contentValues);

        return insert_profile == 1;
    }

    /**
     * update user profile
     *
     * @param profileDTO
     * @return boolean
     */
    @Override
    public boolean updateProfile(ProfileDTO profileDTO) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(TripPlannerContract.COLUMN_NAME_PROFILE_NAME, profileDTO.getProfile_name());
        contentValues.put(TripPlannerContract.COLUMN_NAME_PROFILE_EMAIL, profileDTO.getProfile_email());
        contentValues.put(TripPlannerContract.COLUMN_NAME_PROFILE_PICTURE, profileDTO.getProfile_picture());
        contentValues.put(TripPlannerContract.COLUMN_NAME_PROFILE_PASSWORD, profileDTO.getProfile_password());

        //where condition
        String whereCondition = TripPlannerContract.COLUMN_NAME_PROFILE_ID + "= ?";

        //where param
        String[] whereParam = new String[]{String.valueOf(profileDTO.getProfile_id())};

        Log.i(TAG, "ID: " + String.valueOf(profileDTO.getProfile_id()));

        int update_profile = database.update(TripPlannerContract.TABLE_NAME_PROFILE, contentValues, whereCondition, whereParam);
        return update_profile == 1;
    }

    /**
     * delete user profile
     *
     * @param id
     * @return boolean
     */
    @Override
    public boolean deleteProfile(int id) {

        //where condition
        String whereCondition = TripPlannerContract.COLUMN_NAME_PROFILE_ID + "= ?";

        //where param
        String[] whereParam = new String[]{String.valueOf(id)};

        int delete_profile = database.delete(TripPlannerContract.TABLE_NAME_PROFILE, whereCondition, whereParam);

        return delete_profile == 1;
    }

    /**
     * check if email exist or not
     *
     * @param profile_email
     * @return boolean
     */
    @Override
    public boolean isEmailExist(String profile_email) {
        //where condition
        String whereCondition = TripPlannerContract.COLUMN_NAME_PROFILE_EMAIL + "= ?";

        //columns
        String[] columns = new String[]{TripPlannerContract.COLUMN_NAME_PROFILE_EMAIL};

        //where param
        String[] whereParam = new String[]{profile_email};

        Cursor cursor = database.query(TripPlannerContract.TABLE_NAME_PROFILE,
                columns, whereCondition, whereParam, null, null, null);

        /*return true if email is null so the the email doesn't exist in the database
          so we can add this mail to our database*/

        String returned_email = "";
        while (cursor.moveToNext()) {
            returned_email = cursor.getString(0);
        }

        Log.i(TAG, "returned_email: " + returned_email);

        return !returned_email.equals("");
    }

    /**
     * get number of users
     *
     * @return int
     */
    @Override
    public int NumberOfUser() {

        int count = 0;

        Cursor cursor = database.query(TripPlannerContract.TABLE_NAME_PROFILE,
                null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            count++;
        }

        cursor.close();
        return count;
    }

    /**
     * get profile using email and password
     *
     * @param profile_email
     * @param profile_password
     * @return profileDTO
     */
    @Override
    public ProfileDTO getProfileByEmailAndPassword(String profile_email, String profile_password) {
        ProfileDTO profileDTO = new ProfileDTO();

        //where condition
        String whereCondition = TripPlannerContract.COLUMN_NAME_PROFILE_EMAIL + "= ? AND " +
                TripPlannerContract.COLUMN_NAME_PROFILE_PASSWORD + "= ?";

        //where param
        String[] whereParam = new String[]{profile_email, profile_password};

        Cursor cursor = database.query(TripPlannerContract.TABLE_NAME_PROFILE,
                null, whereCondition, whereParam, null, null, null);

        while (cursor.moveToNext()) {
            profileDTO.setProfile_id(cursor.getInt(0));
            profileDTO.setProfile_name(cursor.getString(1));
            profileDTO.setProfile_email(cursor.getString(2));
            profileDTO.setProfile_picture(cursor.getBlob(3));
            profileDTO.setProfile_password(cursor.getString(4));
        }

        cursor.close();
        return profileDTO;
    }
    @Override
    public ProfileDTO getProfileByEmail(String profile_email) {
        ProfileDTO profileDTO = new ProfileDTO();

        //where condition
        String whereCondition = TripPlannerContract.COLUMN_NAME_PROFILE_EMAIL + "= ?";

        //where param
        String[] whereParam = new String[]{profile_email};

        Cursor cursor = database.query(TripPlannerContract.TABLE_NAME_PROFILE,
                null, whereCondition, whereParam, null, null, null);

        while (cursor.moveToNext()) {
            profileDTO.setProfile_id(cursor.getInt(0));
            profileDTO.setProfile_name(cursor.getString(1));
            profileDTO.setProfile_email(cursor.getString(2));
            profileDTO.setProfile_picture(cursor.getBlob(3));
            profileDTO.setProfile_password(cursor.getString(4));
        }

        cursor.close();
        return profileDTO;
    }
}
