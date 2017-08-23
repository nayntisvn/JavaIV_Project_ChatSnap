package com.example.sdist.testingproject;

import java.io.BufferedReader;
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

//    Set_WebServices
//
//    format for webservice :
//                      <identifier> = "http://" + IpAddress + "<insert webservice here>";
    public static String login = "http://" + IpAddress + ":8080/JavaProject4_CamShot_Server/webresources/com.camshot.user/login?username=";
    public static String signup = "http://" + IpAddress + ":8080/JavaProject4_CamShot_Server/webresources/com.camshot.user/newuser";

//    Public Variables
    private String Username;
    private String Password;

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


}
