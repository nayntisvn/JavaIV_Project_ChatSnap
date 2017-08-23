package com.example.sdist.testingproject;

import java.util.Date;

/**
 * Created by Sdist on 8/23/2017.
 */

public class Data_Message {

//    Offline variables for Message table

    private String message;
    private int userId;
    private Date timestamp;
    private int recipient;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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
