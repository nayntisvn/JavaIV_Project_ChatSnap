package com.example.sdist.testingproject;

import android.graphics.Bitmap;

import java.util.Date;

/**
 * Created by Sdist on 8/25/2017.
 */

public class Class_Data_Offline {

    private String message;
    private Bitmap file;
    private int userId;
    private Date timestamp;
    private int recipient;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Bitmap getFile() {
        return file;
    }

    public void setFile(Bitmap file) {
        this.file = file;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public int getRecipient() {
        return recipient;
    }

    public void setRecipient(int recipient) {
        this.recipient = recipient;
    }
}
