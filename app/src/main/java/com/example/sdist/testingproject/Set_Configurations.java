package com.example.sdist.testingproject;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Sdist on 8/19/2017.
 */

public class Set_Configurations {

//    Ip Address of server
    private static String IpAddress = "192.168.254.107";

//    Address of user details file
    protected static File user_Details;

//    Set_WebServices
//
//    format for webservice :
//                      <identifier> = "http://" + IpAddress + "<insert webservice here>";
    public static String Web_Login = "http://" + IpAddress + ":8080/JavaProject4_CamShot_Server/webresources/com.camshot.user/Web_Login?username=";
    public static String Web_SignUp = "http://" + IpAddress + ":8080/JavaProject4_CamShot_Server/webresources/com.camshot.user/newuser";

//    Public Variables
    private int userId;
    private String Username;
    private String Password;
    private String Email;
    private String Birthday;

//    Encapsulation
    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getBirthday() {
        return Birthday;
    }

    public void setBirthday(String birthday) {
        Birthday = birthday;
    }
}
