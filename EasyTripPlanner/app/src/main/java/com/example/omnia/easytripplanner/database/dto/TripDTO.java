package com.example.omnia.easytripplanner.database.dto;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by EslamWaheed on 3/5/2018.
 */

public class TripDTO implements Serializable{
    private Integer trip_id;
    private String trip_name;
    private String trip_start_point;
    private String trip_end_point;
    private Double trip_start_point_longitude;
    private Double trip_end_point_longitude;
    private Double trip_start_point_latitude;
    private Double trip_end_point_latitude;
    private String trip_date;
    private String trip_time;
    private Integer trip_rounded;
    private byte[] trip_picture;
    private Double trip_millisecond;
    private Integer profile_id;
    private String trip_status;

    public TripDTO() {
    }

    public TripDTO(String trip_name, String trip_start_point, String trip_end_point, Double trip_start_point_longitude, Double trip_end_point_longitude, Double trip_start_point_latitude, Double trip_end_point_latitude, String trip_date, String trip_time, Integer trip_rounded, byte[] trip_picture, Double trip_millisecond, Integer profile_id, String trip_status) {
        this.trip_name = trip_name;
        this.trip_start_point = trip_start_point;
        this.trip_end_point = trip_end_point;
        this.trip_start_point_longitude = trip_start_point_longitude;
        this.trip_end_point_longitude = trip_end_point_longitude;
        this.trip_start_point_latitude = trip_start_point_latitude;
        this.trip_end_point_latitude = trip_end_point_latitude;
        this.trip_date = trip_date;
        this.trip_time = trip_time;
        this.trip_rounded = trip_rounded;
        this.trip_picture = trip_picture;
        this.trip_millisecond = trip_millisecond;
        this.profile_id = profile_id;
        this.trip_status = trip_status;
    }

    public Integer getTrip_id() {
        return trip_id;
    }

    public void setTrip_id(Integer trip_id) {
        this.trip_id = trip_id;
    }

    public String getTrip_name() {
        return trip_name;
    }

    public void setTrip_name(String trip_name) {
        this.trip_name = trip_name;
    }

    public String getTrip_start_point() {
        return trip_start_point;
    }

    public void setTrip_start_point(String trip_start_point) {
        this.trip_start_point = trip_start_point;
    }

    public String getTrip_end_point() {
        return trip_end_point;
    }

    public void setTrip_end_point(String trip_end_point) {
        this.trip_end_point = trip_end_point;
    }

    public Double getTrip_start_point_longitude() {
        return trip_start_point_longitude;
    }

    public void setTrip_start_point_longitude(Double trip_start_point_longitude) {
        this.trip_start_point_longitude = trip_start_point_longitude;
    }

    public Double getTrip_end_point_longitude() {
        return trip_end_point_longitude;
    }

    public void setTrip_end_point_longitude(Double trip_end_point_longitude) {
        this.trip_end_point_longitude = trip_end_point_longitude;
    }

    public Double getTrip_start_point_latitude() {
        return trip_start_point_latitude;
    }

    public void setTrip_start_point_latitude(Double trip_start_point_latitude) {
        this.trip_start_point_latitude = trip_start_point_latitude;
    }

    public Double getTrip_end_point_latitude() {
        return trip_end_point_latitude;
    }

    public void setTrip_end_point_latitude(Double trip_end_point_latitude) {
        this.trip_end_point_latitude = trip_end_point_latitude;
    }

    public String getTrip_date() {
        return trip_date;
    }

    public void setTrip_date(String trip_date) {
        this.trip_date = trip_date;
    }

    public String getTrip_time() {
        return trip_time;
    }

    public void setTrip_time(String trip_time) {
        this.trip_time = trip_time;
    }

    public Integer getTrip_rounded() {
        return trip_rounded;
    }

    public void setTrip_rounded(Integer trip_rounded) {
        this.trip_rounded = trip_rounded;
    }

    public byte[] getTrip_picture() {
        return trip_picture;
    }

    public void setTrip_picture(byte[] trip_picture) {
        this.trip_picture = trip_picture;
    }

    public Double getTrip_millisecond() {
        return trip_millisecond;
    }

    public void setTrip_millisecond(Double trip_millisecond) {
        this.trip_millisecond = trip_millisecond;
    }

    public Integer getProfile_id() {
        return profile_id;
    }

    public void setProfile_id(Integer profile_id) {
        this.profile_id = profile_id;
    }

    public String getTrip_status() {
        return trip_status;
    }

    public void setTrip_status(String trip_status) {
        this.trip_status = trip_status;
    }
}
