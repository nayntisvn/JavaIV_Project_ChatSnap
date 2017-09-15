package com.example.sdist.testingproject;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Activity_MessageArea extends AppCompatActivity implements SensorEventListener{
    int friendUserId = 1;
    String messageToBeSend= "";
    EditText Message;
    ImageView Send;
    private static final int SHAKE_THRESHOLD = 1500;
    long lastUpdate;
    float x,y,z, last_x=0, last_y=0, last_z=0;

    private Sensor mAccelerometer;
    SensorManager sensorMgr;

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float g[] = sensorEvent.values.clone();
            long curTime = System.currentTimeMillis();
            // only allow one update every 100ms.
            if ((curTime - lastUpdate) > 100) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;

                x = g[0];
                y = g[1];
                z = g[2];

                float speed = Math.abs(x + y + z - last_x - last_y - last_z) / diffTime * 10000;

                if (speed > SHAKE_THRESHOLD) {
                    Send.performClick();
                }
                last_x = x;
                last_y = y;
                last_z = z;
            }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public class mAdapter extends ArrayAdapter<Class_ChatMessage>{

        public mAdapter(@NonNull Context context, ArrayList<Class_ChatMessage> objects) {
            super(context, 0, objects);
        }
        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (view == null) {

                view = LayoutInflater.from(getContext()).inflate(R.layout.message, viewGroup, false);

                viewHolder = new Activity_MessageArea.mAdapter.ViewHolder();

                viewHolder.textViewText = (TextView) view.findViewById(R.id.message_text);
                viewHolder.textViewUser = (TextView) view.findViewById(R.id.message_user);
                viewHolder.textViewTime = (TextView) view.findViewById(R.id.message_time);
                view.setTag(viewHolder);

            } else {
                viewHolder = (ViewHolder) view.getTag();
            }

            Class_ChatMessage myModel = getItem(position);

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

        sensorMgr = (SensorManager) getSystemService(SENSOR_SERVICE);

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
                        "\t\"timestamp\" : \"%s\",\n" +
                        "\t\"recipient\" : %s\n" +
                        "}";

                messageToBeSend = String.format(messageToBeSend, Message.getText().toString(), Set_Configurations.getTimeStamp(), friendUserId);

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

        mAccelerometer = sensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorMgr.registerListener(Activity_MessageArea.this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);

        thread.start();

    }

    private void displayChatMessages(JSONArray result) {
        ListView listOfMessages = (ListView)findViewById(R.id.list_of_messages);
        ArrayList<Class_ChatMessage> listMessages = new ArrayList<Class_ChatMessage>();

        try {
            JSONArray ar = new JSONArray(result.toString());
            for (int i = 0; i < ar.length(); i++){
                JSONObject a = ar.getJSONObject(i);
                Class_ChatMessage msg = new Class_ChatMessage();
                msg.setMessageUser(a.getJSONObject("userId").getString("username"));
                msg.setMessageText(a.getString("message"));
                msg.setMessageTime(a.getString("timestamp"));
                listMessages.add(msg);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mAdapter adapter = new mAdapter(Activity_MessageArea.this, listMessages);
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
                Toast.makeText(Activity_MessageArea.this, "No Network", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(Activity_MessageArea.this, "Send Failed", Toast.LENGTH_SHORT).show();
            }

            return null;
        }

        @Override
        public void onPostExecute(Void result){

            Toast.makeText(Activity_MessageArea.this, "MessageSent", Toast.LENGTH_SHORT).show();

            new RefreshMessages().execute();
        }

    }

    public String getTimeStamp(){
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-ddThh:mm:ss");
        String format = s.format(new Date());
        return format;
    }

}
