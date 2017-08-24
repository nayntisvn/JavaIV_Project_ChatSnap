package com.example.sdist.testingproject;

import android.graphics.Bitmap;

import java.util.Date;

/**
 * Created by Sdist on 8/23/2017.
 */

public class Data_Stories {

//     Offline variables for Stories table
    private Bitmap file;
    private int userId;
    private Date timestamp;

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
}
