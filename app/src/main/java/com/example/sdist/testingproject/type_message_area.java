package com.example.sdist.testingproject;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class type_message_area extends AppCompatActivity {
    int friendUserId = 1;
    String messageToBeSend= "";
    EditText Message;
    ImageView Send;

    public class mAdapter extends ArrayAdapter<ChatMessage>{

        public mAdapter(@NonNull Context context, ArrayList<ChatMessage> objects) {
            super(context, 0, objects);
        }
        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (view == null) {

                view = LayoutInflater.from(getContext()).inflate(R.layout.message, viewGroup, false);

                viewHolder = new type_message_area.mAdapter.ViewHolder();

                viewHolder.textViewText = (TextView) view.findViewById(R.id.message_text);
                viewHolder.textViewUser = (TextView) view.findViewById(R.id.story_image);
                viewHolder.textViewTime = (TextView) view.findViewById(R.id.message_time);
                view.setTag(viewHolder);

            } else {
                viewHolder = (ViewHolder) view.getTag();
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

        Send = (ImageView) findViewById(R.id.imageView8);
        Message = (EditText) findViewById(R.id.editText);

        Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                messageToBeSend = "{ \n" +
                        "\t\"message\" : \"%s\",\n" +
                        "\t\"timestamp\" : \"2009-09-17T00:00:00+08:00\",\n" +
                        "\t\"recipient\" : %s\n" +
                        "}";

                messageToBeSend = String.format(messageToBeSend, Message.getText().toString(), friendUserId);

                new SendMessages().execute();

            }
        });

        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    while(true) {
                        sleep(500);
                        new RefreshMessages().execute();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        thread.start();

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

                resultSet = new JSONArray(Set_WebServices.getJsonObject(Set_Configurations.User_Message + Set_Configurations.userId + "/" + friendUserId));

            }catch(Exception e)
            {
                Toast.makeText(type_message_area.this, "No Network", Toast.LENGTH_SHORT).show();
            }

            return resultSet;
        }

        @Override
        public void onPostExecute(JSONArray result){

            if(result != null)
            {
                displayChatMessages(result);

            }

        }

    }

    public class SendMessages extends AsyncTask<Void, Void, Void>
    {
        @Override
        public Void doInBackground(Void... params)
        {
            try{

                Set_WebServices.postJsonObject(Set_Configurations.User_Message_Send + Set_Configurations.userId, messageToBeSend);

            }catch(Exception e)
            {
                Toast.makeText(type_message_area.this, "Send Failed", Toast.LENGTH_SHORT).show();
            }

            return null;
        }

        @Override
        public void onPostExecute(Void result){

            Toast.makeText(type_message_area.this, "MessageSent", Toast.LENGTH_SHORT).show();

            new RefreshMessages().execute();
        }

    }

}
