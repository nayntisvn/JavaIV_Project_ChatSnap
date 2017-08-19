package com.example.sdist.testingproject;

/**
 * Created by Sdist on 8/19/2017.
 */

public class Configurations {

//    Ip Address of server
    private static String IpAddress = "";

//    WebServices
//
//    format for webservice :
//                      <identifier> = "http://" + IpAddress + "<insert webservice here>";
    public static String login = IpAddress + "";

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
