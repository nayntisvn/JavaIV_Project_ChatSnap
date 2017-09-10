package com.example.sdist.testingproject;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class type_message_area extends AppCompatActivity {
    int friendUserId;
    public class mAdapter extends ArrayAdapter<ChatMessage>{

        public mAdapter(@NonNull Context context, ArrayList<ChatMessage> objects) {
            super(context, 0, objects);
        }
        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            type_message_area.mAdapter.ViewHolder viewHolder;
            if (view == null) {
                view = LayoutInflater.from(getContext()).inflate(R.layout.message, viewGroup, false);

                viewHolder = new type_message_area.mAdapter.ViewHolder();

                viewHolder.textViewText = (TextView) view.findViewById(R.id.message_text);
                viewHolder.textViewUser = (TextView) view.findViewById(R.id.message_user);
                viewHolder.textViewTime = (TextView) view.findViewById(R.id.message_time);
                view.setTag(viewHolder);
            } else {
                viewHolder = (type_message_area.mAdapter.ViewHolder) view.getTag();
            }

            ChatMessage myModel = getItem(position);

            viewHolder.textViewText.setText(myModel.getMessageText());
            viewHolder.textViewUser.setText(myModel.getMessageUser());
            viewHolder.textViewTime.setText(myModel.getMessageTime());

            return view;
        }
        public class ViewHolder {
            TextView textViewText;
            TextView textViewUser;
            TextView textViewTime;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            friendUserId = Integer.valueOf(extras.getString("friendUserId"));
        }
        setContentView(R.layout.activity_type_message_area);
    }

    private void displayChatMessages(JSONArray result) {
        ListView listOfMessages = (ListView)findViewById(R.id.list_of_messages);
        ArrayList<ChatMessage> listMessages = new ArrayList<ChatMessage>();

        try {
            JSONArray ar = new JSONArray(result.toString());
            for (int i = 0; i < ar.length(); i++){
                JSONObject a = ar.getJSONObject(i);
                ChatMessage msg = new ChatMessage();
                msg.setMessageUser(a.getJSONObject("userId").getString("username"));
                msg.setMessageText(a.getString("message"));
                msg.setMessageTime(a.getString("timestamp"));
                listMessages.add(msg);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mAdapter adapter = new mAdapter(type_message_area.this, listMessages);
        listOfMessages.setAdapter(adapter);

    }

    public class RefreshMessages extends AsyncTask<Void, Void, JSONArray>
    {
        @Override
        public JSONArray doInBackground(Void... params)
        {
            JSONArray resultSet = null;
            try{

                resultSet = new JSONArray(Set_WebServices.getJsonObject(Set_Configurations.User_Message + Set_Configurations.userId + friendUserId));

            }catch(Exception e)
            {
                Toast.makeText(type_message_area.this, "Testing", Toast.LENGTH_SHORT).show();
            }

            return resultSet;
        }

        @Override
        public void onPostExecute(JSONArray result){

            if(result != null)
            {
                Toast.makeText(type_message_area.this, "Result", Toast.LENGTH_SHORT).show();
                displayChatMessages(result);

            }

        }

    }

}
