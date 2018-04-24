package com.example.omnia.easytripplanner.database.dao.daoimpl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.example.omnia.easytripplanner.database.TripPlannerContract;
import com.example.omnia.easytripplanner.database.TripPlannerDbHelper;
import com.example.omnia.easytripplanner.database.dao.daoint.NoteDAOInt;
import com.example.omnia.easytripplanner.database.dto.NoteDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by EslamWaheed on 3/6/2018.
 */


public class NoteDAOImpl implements NoteDAOInt {

    TripPlannerDbHelper dbHelper;
    SQLiteDatabase database;

    /**
     * Constructor take Context
     *
     * @param context
     */
    public NoteDAOImpl(Context context) {
        dbHelper = new TripPlannerDbHelper(context);
        database = dbHelper.getReadableDatabase();
    }

    @Override
    public NoteDTO getNote(int id) {
        NoteDTO noteDTO = new NoteDTO();

        //where condition
        String whereCondition = TripPlannerContract.COLUMN_NAME_NOTE_ID + "= ?";

        //where param
        String[] whereParam = new String[]{String.valueOf(id)};

        Cursor cursor = database.query(TripPlannerContract.TABLE_NAME_NOTE,
                null, whereCondition, whereParam, null, null, null);

        while (cursor.moveToNext()) {
            noteDTO.setNote_id(cursor.getInt(0));
            noteDTO.setNote_description(cursor.getString(1));
            noteDTO.setNote_trip_id(cursor.getInt(2));
        }

        cursor.close();
        return noteDTO;
    }

    /**
     * insert trip note
     *
     * @param noteDTO
     * @return boolean
     */
    @Override
    public boolean insertNote(NoteDTO noteDTO) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TripPlannerContract.COLUMN_NAME_NOTE_ID, noteDTO.getNote_id());
        contentValues.put(TripPlannerContract.COLUMN_NAME_NOTE_DESCRIPTION, noteDTO.getNote_description());
        contentValues.put(TripPlannerContract.COLUMN_NAME_NOTE_TRIP_ID, noteDTO.getNote_trip_id());

        long insert_note = database.insert(TripPlannerContract.TABLE_NAME_NOTE, null, contentValues);

        return insert_note == 1;
    }

    /**
     * update trip note
     *
     * @param noteDTO
     * @return boolean
     */
    @Override
    public boolean updateNote(NoteDTO noteDTO) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TripPlannerContract.COLUMN_NAME_NOTE_ID, noteDTO.getNote_id());
        contentValues.put(TripPlannerContract.COLUMN_NAME_NOTE_DESCRIPTION, noteDTO.getNote_description());
        contentValues.put(TripPlannerContract.COLUMN_NAME_NOTE_TRIP_ID, noteDTO.getNote_trip_id());

        //where condition
        String whereCondition = TripPlannerContract.COLUMN_NAME_NOTE_ID + "= ? AND " +
                TripPlannerContract.COLUMN_NAME_NOTE_TRIP_ID + "= ?";

        //where param
        String[] whereParam = new String[]{String.valueOf(noteDTO.getNote_id()), String.valueOf(noteDTO.getNote_trip_id())};

        int update_note = database.update(TripPlannerContract.TABLE_NAME_NOTE, contentValues, whereCondition, whereParam);
        return update_note == 1;
    }

    /**
     * delete trip note
     *
     * @param note_id
     * @param trip_id
     * @return boolean
     */

    @Override
    public boolean deleteNote(int note_id, int trip_id) {
        //where condition
        String whereCondition = TripPlannerContract.COLUMN_NAME_NOTE_ID + "= ? AND " +
                TripPlannerContract.COLUMN_NAME_NOTE_TRIP_ID + "= ?";

        //where param
        String[] whereParam = new String[]{String.valueOf(note_id), String.valueOf(trip_id)};

        int delete_note = database.delete(TripPlannerContract.TABLE_NAME_NOTE, whereCondition, whereParam);

        return delete_note == 1;
    }

    @Override
    public boolean deleteAllNote(int tripId) {
        //where condition
        String whereCondition = TripPlannerContract.COLUMN_NAME_NOTE_TRIP_ID + "= ?";

        //where param
        String[] whereParam = new String[]{String.valueOf(tripId)};

        int delete_note = database.delete(TripPlannerContract.TABLE_NAME_NOTE, whereCondition, whereParam);

        return delete_note == 1;
    }

    /**
     * get number of notes
     *
     * @return int
     */
    @Override
    public int NumberOfNote() {
        int count = 0;

        Cursor cursor = database.query(TripPlannerContract.TABLE_NAME_NOTE,
                null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            count++;
        }

        cursor.close();
        return count;
    }

    /**
     * get all trip notes
     *
     * @param note_trip_id
     * @return List<NoteDTO>
     */
    @Override
    public List<NoteDTO> getAllTripNotes(int note_trip_id) {
        List<NoteDTO> noteDTOS = new ArrayList<>();

        //where condition
        String whereCondition = TripPlannerContract.COLUMN_NAME_NOTE_TRIP_ID + "= ?";

        //where param
        String[] whereParam = new String[]{String.valueOf(note_trip_id)};

        Cursor cursor = database.query(TripPlannerContract.TABLE_NAME_NOTE,
                null, whereCondition, whereParam, null, null, null);

        while (cursor.moveToNext()) {
            NoteDTO noteDTO = new NoteDTO();
            noteDTO.setNote_id(cursor.getInt(0));
            noteDTO.setNote_description(cursor.getString(1));
            noteDTO.setNote_trip_id(cursor.getInt(2));
            noteDTOS.add(noteDTO);
        }

        cursor.close();
        return noteDTOS;
    }
}
