package com.example.omnia.easytripplanner.database.dto;

import java.io.Serializable;

/**
 * Created by EslamWaheed on 3/5/2018.
 */

public class NoteDTO implements Serializable{
    private Integer note_id;
    private String note_description;
    private Integer note_trip_id;

    public NoteDTO() {
    }

    public NoteDTO(Integer note_id, String note_description, Integer note_trip_id) {
        this.note_id = note_id;
        this.note_description = note_description;
        this.note_trip_id = note_trip_id;
    }

    public Integer getNote_id() {
        return note_id;
    }

    public void setNote_id(Integer note_id) {
        this.note_id = note_id;
    }

    public String getNote_description() {
        return note_description;
    }

    public void setNote_description(String note_description) {
        this.note_description = note_description;
    }

    public Integer getNote_trip_id() {
        return note_trip_id;
    }

    public void setNote_trip_id(Integer note_trip_id) {
        this.note_trip_id = note_trip_id;
    }
}
