package com.example.sdist.testingproject;

import android.net.ParseException;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class friendlist extends AppCompatActivity {

    ArrayAdapter<Friend> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friendlist);

        new RefreshFriends().execute();
    }

    private void displayFriends(JSONArray result) {
        ListView listOfMessages = (ListView)findViewById(R.id.list_of_messages);

        adapter = new ArrayAdapter<Friend>(friendlist.this,
                R.layout.friend/*,lagay dito yung messages*/) {
            protected void getView(int position, View v, Friend model) {
                // Get references to the views of message.xml
                TextView friendText = (TextView)v.findViewById(R.id.friend);

                // Set their text
                friendText.setText(model.getFriendName());
            }
        };
        try {
            JSONArray ar = new JSONArray(result.toString());
            for (int i = 0; i < ar.length(); i++){
                JSONObject a = ar.getJSONObject(i);
                Friend friend = new Friend();
                friend.setFriendName(a.getJSONObject("UserId").getString("username"));
                adapter.add(friend);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        listOfMessages.setAdapter(adapter);
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
                displayFriends(result);

            }

        }
    }

}
