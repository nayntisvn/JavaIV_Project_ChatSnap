package com.example.sdist.testingproject;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Sdist on 8/19/2017.
 */

public class Set_Configurations {

//    Ip Address of server
    private static String IpAddress = "192.168.1.102";

//    Address of user details file
    protected static File user_Details;

//    Set_WebServices
//
//    format for webservice :
//                      <identifier> = "http://" + IpAddress + "<insert webservice here>";
    public static String Web_Login = "http://" + IpAddress + ":8080/JavaProject4_CamShot_Server/webresources/com.camshot.user/login"; //put
    public static String Web_SignUp = "http://" + IpAddress + ":8080/JavaProject4_CamShot_Server/webresources/com.camshot.user/newuser";
    public static String Web_Logout = "http://" + IpAddress + ":8080/JavaProject4_CamShot_Server/webresources/com.camshot.user/logout";

//    Refreshing or Getting of Data from WebService
    public static String User_Friends = "http://" + IpAddress + ":8080/JavaProject4_CamShot_Server/webresources/com.camshot.friend/friends/";
    public static String User_Message = "http://" + IpAddress + ":8080/JavaProject4_CamShot_Server/webresources/com.camshot.message/message/";
    public static String User_Objects = "http://" + IpAddress + ":8080/JavaProject4_CamShot_Server/webresources/com.camshot.object1/object/";
    public static String User_Stories = "http://" + IpAddress + ":8080/JavaProject4_CamShot_Server/webresources/com.camshot.stories/";

//    Sending of JSON object or Data to WebService
    public static String User_Object_Send = "http://" + IpAddress + ":8080/JavaProject4_CamShot_Server/webresources/com.camshot.object1/new/";
    public static String User_Stories_Send = "http://" + IpAddress + ":8080/JavaProject4_CamShot_Server/webresources/com.camshot.stories/new/";
    public static String User_Message_Send = "http://" + IpAddress + ":8080/JavaProject4_CamShot_Server/webresources/com.camshot.message/send/";


    //    Public Variables
    public static int userId;
    public static String Username;
    public static int recId;
    public static String Password;
    public static String Email;
    public static String Birthday;
    public static String timeStamp;

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

    public String getTimeStamp(){
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String format = s.format(new Date());
        return format;
    }
}
