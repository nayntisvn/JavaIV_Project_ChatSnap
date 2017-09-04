package com.example.sdist.testingproject;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * Created by Sdist on 8/20/2017.
 */

public class Set_WebServices {

    //    Webservice for sending a JSON object from server
    public static String postJsonObject(String urlQuery, String stringToPass) {
        try {
            URL url = new URL(urlQuery);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestProperty("Content-Type", "Application/json");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");

            OutputStream os = conn.getOutputStream();
            os.write(stringToPass.getBytes());
            os.flush();

            if (conn.getResponseCode() != HttpURLConnection.HTTP_ACCEPTED) {
                return ("Failed : HTTP error code : " + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String output;
            String concatOutput = "";
            while ((output = br.readLine()) != null) {
                concatOutput += output;
            }

            conn.disconnect();

            return "";

        } catch (Exception e) {
            return "" + e;
        }

    }

    public static String putJsonObject(String urlQuery, String stringToPass) {
        try {
            URL url = new URL(urlQuery);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestProperty("Content-Type", "Application/json");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("PUT");

            OutputStream os = conn.getOutputStream();
            os.write(stringToPass.getBytes());
            os.flush();

            if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
                return ("Failed : HTTP error code : " + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String output;
            String concatOutput = "";
            while ((output = br.readLine()) != null) {
                concatOutput += output;
            }

            conn.disconnect();

            return concatOutput;

        } catch (Exception e) {
            return "" + e;
        }

    }

    //    Webservice for getting a JSON object from server
    protected static String getJsonObject(String uri) {
        StringBuilder result = new StringBuilder();
        JSONObject jsonObject = null;

        try {
            URL url = new URL(uri);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            InputStream in = new BufferedInputStream(conn.getInputStream());

            BufferedReader reader = new BufferedReader(new InputStreamReader(in, Charset.forName("UTF-8")));

            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result.toString();
    }

}
