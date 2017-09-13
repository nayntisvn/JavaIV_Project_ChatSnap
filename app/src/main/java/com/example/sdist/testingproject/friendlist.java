package com.example.sdist.testingproject;

import android.content.Context;
import android.content.Intent;
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
import android.widget.AdapterView;
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
    Intent intent;
    ListView listOfFriends;
    ArrayList<Friend> listFriends;
    int friendUserId;
    String mode = "";

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
        listOfFriends = (ListView)findViewById(R.id.list_of_friends);

        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            mode = extras.getString("friendUserId");
        }

        if(mode.equals("Stories"))
        {
            listOfFriends.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                   intent = new Intent(getApplicationContext(), Stories.class);

                    for(int z = 0; z < listFriends.size(); z++){

                        Toast.makeText(friendlist.this, listFriends.get(z).getFriendName(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(friendlist.this, listOfFriends.getItemAtPosition(i).toString(), Toast.LENGTH_SHORT).show();
                        if((listOfFriends.getItemAtPosition(i)).equals(listFriends.get(z).getFriendName())){
                            friendUserId = listFriends.get(z).getFriendUserId();
                            Toast.makeText(friendlist.this, "No Network", Toast.LENGTH_SHORT).show();
                        }
                    }

                    intent.putExtra("Mode", "FriendStory");
                    intent.putExtra("friendUserId", friendUserId + "");

                    startActivity(intent);
                }
            });
        }
        else if(mode.equals("Messages"))
        {
            listOfFriends.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    intent = new Intent(getApplicationContext(), type_message_area.class);

                    for(int z = 0; z < listFriends.size(); z++){

                        Toast.makeText(friendlist.this, listFriends.get(z).getFriendName(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(friendlist.this, listOfFriends.getItemAtPosition(i).toString(), Toast.LENGTH_SHORT).show();
                        if((listOfFriends.getItemAtPosition(i)).equals(listFriends.get(z).getFriendName())){
                            friendUserId = listFriends.get(z).getFriendUserId();
                            Toast.makeText(friendlist.this, "No Network", Toast.LENGTH_SHORT).show();
                        }
                    }

                    intent.putExtra("friendUserId", friendUserId + "");

                    startActivity(intent);

                }
            });
        }

        listFriends = new ArrayList<Friend>();
        new RefreshFriends().execute();

    }

    private void displayFriends(JSONArray result) {

        try {
            JSONArray ar = new JSONArray(result.toString());
            for (int i = 0; i < ar.length(); i++){
                JSONObject a = ar.getJSONObject(i);
                Friend friend = new Friend();
                friend.setFriendName(a.getString("username"));
                friend.setFriendUserId(Integer.valueOf(a.getString("userFriendId")));
                listFriends.add(friend);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

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
                Toast.makeText(friendlist.this, "No Network", Toast.LENGTH_SHORT).show();
            }

            return resultSet;
        }

        @Override
        public void onPostExecute(JSONArray result){

            if(result != null)
            {
                displayFriends(result);
            }

        }
    }

}
