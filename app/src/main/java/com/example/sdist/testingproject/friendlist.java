package com.example.sdist.testingproject;

import android.content.Context;
import android.net.ParseException;
import android.os.AsyncTask;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class friendlist extends AppCompatActivity {
    public class mAdapter extends ArrayAdapter<Friend>{

        public mAdapter(@NonNull Context context, ArrayList<Friend> objects) {
            super(context, 0, objects);
        }
        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (view == null) {
                view = LayoutInflater.from(getContext()).inflate(R.layout.friend, viewGroup, false);

                viewHolder = new ViewHolder();

                viewHolder.textView = (TextView) view.findViewById(R.id.friend);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }

            Friend myModel = getItem(position);

            viewHolder.textView.setText(myModel.getFriendName());

            return view;
        }
        public class ViewHolder {
            TextView textView;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friendlist);

        new RefreshFriends().execute();
    }

    private void displayFriends(JSONArray result) {
        ListView listOfFriends = (ListView)findViewById(R.id.list_of_friends);
        ArrayList<Friend> listFriends = new ArrayList<Friend>();

        try {
            JSONArray ar = new JSONArray(result.toString());
            for (int i = 0; i < ar.length(); i++){
                JSONObject a = ar.getJSONObject(i);
                Friend friend = new Friend();
                friend.setFriendName(a.getJSONObject("userId").getString("username"));
                listFriends.add(friend);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        adapter = new ArrayAdapter<Friend>(friendlist.this,
//                R.layout.friend, listFriends) {
//
//            protected void getView(int position, View v, Friend model) {
//                // Get references to the views of message.xml
//                TextView friendText = (TextView)v.findViewById(R.id.friend);
//
//                // Set their text
//                friendText.setText(model.getFriendName());
//            }
//        };

        mAdapter adapter = new mAdapter(friendlist.this, listFriends);
        listOfFriends.setAdapter(adapter);

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
