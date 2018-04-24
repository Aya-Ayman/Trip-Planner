package com.example.omnia.easytripplanner.database.dto;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Serializable;

/**
 * Created by EslamWaheed on 3/5/2018.
 */

public class ProfileDTO implements Serializable {
    private Integer profile_id;
    private String profile_name;
    private String profile_email;
    private byte[] profile_picture;
    private String profile_password;

    public ProfileDTO() {
    }

    public ProfileDTO(Integer profile_id, String profile_name, String profile_email, byte[] profile_picture, String profile_password) {
        this.profile_id = profile_id;
        this.profile_name = profile_name;
        this.profile_email = profile_email;
        this.profile_picture = profile_picture;
        this.profile_password = profile_password;
    }

    public Integer getProfile_id() {
        return profile_id;
    }

    public void setProfile_id(Integer profile_id) {
        this.profile_id = profile_id;
    }

    public String getProfile_name() {
        return profile_name;
    }

    public void setProfile_name(String profile_name) {
        this.profile_name = profile_name;
    }

    public String getProfile_email() {
        return profile_email;
    }

    public void setProfile_email(String profile_email) {
        this.profile_email = profile_email;
    }

    public byte[] getProfile_picture() {
        return profile_picture;
    }

    public void setProfile_picture(byte[] profile_picture) {
        this.profile_picture = profile_picture;
    }

    public String getProfile_password() {
        return profile_password;
    }

    public void setProfile_password(String profile_password) {
        this.profile_password = profile_password;
    }
}
