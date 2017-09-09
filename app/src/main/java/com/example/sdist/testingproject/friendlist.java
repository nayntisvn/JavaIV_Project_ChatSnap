package com.example.sdist.testingproject;

import android.net.ParseException;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

public class friendlist extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friendlist);

        new RefreshFriends().execute();
    }

    public class RefreshFriends extends AsyncTask<Void, Void, JSONArray>
    {

        @Override
        public JSONArray doInBackground(Void... params){

            JSONArray resultSet = null;
            try{

                resultSet = new JSONArray(Set_WebServices.getJsonObject(Set_Configurations.User_Friends + Set_Configurations.userId));

            }catch(Exception e)
            {
                Toast.makeText(friendlist.this, "Testing", Toast.LENGTH_SHORT).show();
            }

            return resultSet;
        }

        @Override
        public void onPostExecute(JSONArray result){

            if(result != null)
            {
                Toast.makeText(friendlist.this, "Result", Toast.LENGTH_SHORT).show();

            }

        }
    }

}
