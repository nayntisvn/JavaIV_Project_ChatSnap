package com.example.sdist.testingproject;

import java.util.Date;

/**
 * Created by ezeki on 09/09/2017.
 */

public class Class_ChatMessage {

    private String messageText;
    private String messageUser;
    private String messageTime;

    public Class_ChatMessage(String messageText, String messageUser, String messageTime) {
        this.messageText = messageText;
        this.messageUser = messageUser;
        this.messageTime = messageTime;
        // Initialize to current time
    }

    public Class_ChatMessage(){

    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageUser() {
        return messageUser;
    }

    public void setMessageUser(String messageUser) {
        this.messageUser = messageUser;
    }

    public String getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(String messageTime) {
        this.messageTime = messageTime;
    }
}
