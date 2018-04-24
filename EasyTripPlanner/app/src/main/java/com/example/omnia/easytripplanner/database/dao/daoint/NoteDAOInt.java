package com.example.omnia.easytripplanner.database.dao.daoint;



import com.example.omnia.easytripplanner.database.dto.NoteDTO;

import java.util.List;

/**
 * Created by EslamWaheed on 3/6/2018.
 */

public interface NoteDAOInt {
    NoteDTO getNote(int id);

    boolean insertNote(NoteDTO noteDTO);

    boolean updateNote(NoteDTO noteDTO);

    boolean deleteNote(int id, int trip_id);

    boolean deleteAllNote(int tripId);

    int NumberOfNote();

    List<NoteDTO> getAllTripNotes(int note_trip_id);
}