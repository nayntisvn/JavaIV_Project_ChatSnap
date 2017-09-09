package com.example.sdist.testingproject;

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
import org.json.JSONObject;

public class type_message_area extends AppCompatActivity {

    ArrayAdapter<ChatMessage> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_message_area);

        displayChatMessages();

    }

    private void displayChatMessages() {
        ListView listOfMessages = (ListView)findViewById(R.id.list_of_messages);


        adapter = new ArrayAdapter<ChatMessage>(type_message_area.this,
        R.layout.message/*,lagay dito yung messages*/) {
            protected void getView(int position, View v, ChatMessage model) {
                // Get references to the views of message.xml
                TextView messageText = (TextView)v.findViewById(R.id.message_text);
                TextView messageUser = (TextView)v.findViewById(R.id.message_user);
                TextView messageTime = (TextView)v.findViewById(R.id.message_time);

                // Set their text
                messageText.setText(model.getMessageText());
                messageUser.setText(model.getMessageUser());

                // Format the date before showing it
                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                        model.getMessageTime()));
            }
        };
        ChatMessage c = new ChatMessage();
        for (int i =10; i<11; i++){
            c.setMessageText("hi"+i);
            c.setMessageUser("user"+i);
            adapter.add(c);
        }

        listOfMessages.setAdapter(adapter);

    }

    public class RefreshMessages extends AsyncTask<Void, Void, JSONArray>
    {
        @Override
        public JSONArray doInBackground(Void... params)
        {
            JSONArray resultSet = null;
            try{

                resultSet = new JSONArray(Set_WebServices.getJsonObject(Set_Configurations.User_Message + Set_Configurations.userId + "/2"));

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

            }

        }

    }

}
