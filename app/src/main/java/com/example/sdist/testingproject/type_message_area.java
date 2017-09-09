package com.example.sdist.testingproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

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

}
