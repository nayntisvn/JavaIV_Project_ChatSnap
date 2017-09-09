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
    private static String IpAddress = "192.168.1.101";

//    Address of user details file
    protected static File user_Details;

//    Set_WebServices
//
//    format for webservice :
//                      <identifier> = "http://" + IpAddress + "<insert webservice here>";
    public static String Web_Login = "http://" + IpAddress + ":8080/JavaProject4_CamShot_Server/webresources/com.camshot.user/login"; //put
    public static String Web_SignUp = "http://" + IpAddress + ":8080/JavaProject4_CamShot_Server/webresources/com.camshot.user/newuser";
    public static String Web_Logout = "http://" + IpAddress + ":8080/JavaProject4_CamShot_Server/webresources/com.camshot.user/logout";

    public static String User_Friends = "http://" + IpAddress + ":8080/JavaProject4_CamShot_Server/webresources/com.camshot.friend/friends/";
    public static String User_Message = "http://" + IpAddress + ":8080/JavaProject4_CamShot_Server/webresources/com.camshot.message/message/";

    //    Public Variables
    public static int userId;
    public static String Username;
    public static int recId;
    private static String Password;
    private static String Email;
    private static String Birthday;

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
