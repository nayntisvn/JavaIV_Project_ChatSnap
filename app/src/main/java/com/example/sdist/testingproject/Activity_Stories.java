package com.example.sdist.testingproject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sdist.testingproject.FaceTracker.CameraActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class Activity_Stories extends AppCompatActivity {

    Intent intent;
    ListView listOfStories;
    ArrayList<Class_Story> listStories;
    Bitmap currPicture;
    String prePicture;

    ImageView Send;

    int userFriendId = 0;
    String mode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stories);

        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            mode = extras.getString("Mode");
            userFriendId = Integer.parseInt(extras.getString("userFriendId"));
        }

        Send = (ImageView) findViewById(R.id.imageView8);

        if(mode.equals("FriendStory"))
        {
            Send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    intent = new Intent(getApplicationContext(), CameraActivity.class);
                    intent.putExtra("userFriendId", userFriendId + "");
                    startActivity(intent);
                }
            });
        }
        else if(mode.equals("MyStory"))
        {
            Send.setEnabled(false);
        }

        listStories = new ArrayList<Class_Story>();


//        Thread thread = new Thread() {
//            @Override
//            public void run() {
//                try {
//                    while(true) {
//                        sleep(1000);
//
//                    }
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        };

//        thread.start();

        new RefreshStories().execute();

    }

    public class mAdapter extends ArrayAdapter<Class_Story> {

        public mAdapter(@NonNull Context context, ArrayList<Class_Story> objects) {
            super(context, 0, objects);
        }
        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;

            if (view == null) {
                view = LayoutInflater.from(getContext()).inflate(R.layout.story, viewGroup, false);

                viewHolder = new Activity_Stories.mAdapter.ViewHolder();

                viewHolder.storyImageView = (ImageView) view.findViewById(R.id.message_user);
                viewHolder.storyTextView = (TextView) view.findViewById(R.id.story_time);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }

            Class_Story myModel = getItem(position);

            viewHolder.storyImageView.setImageBitmap(myModel.getStorySnap());
            viewHolder.storyTextView.setText(myModel.getStoryTime());

            return view;
        }

        public class ViewHolder {
            ImageView storyImageView;
            TextView storyTextView;
        }
    }

    private void displayStories(JSONArray result) {

        ListView listOfStories = (ListView)findViewById(R.id.list_of_snaps);
        ArrayList<Class_Story> listStories = new ArrayList<Class_Story>();

        if(result.isNull(0)){

        }else {
            try {
                JSONArray ar = new JSONArray(result.toString());

                for (int i = 0; i < ar.length(); i++) {
                    JSONObject a = ar.getJSONObject(i);
                    Class_Story story = new Class_Story();

                    prePicture = a.getString("file");
                    prePicture = prePicture.replace("NEWLINE", System.getProperty("line.separator"));

                    byte[] imgByte = Base64.decode(prePicture, Base64.DEFAULT);

                    currPicture = BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);

                    story.setStorySnap(currPicture);
                    story.setStoryTime(a.getString("timestamp"));


                    listStories.add(story);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            mAdapter adapter = new mAdapter(Activity_Stories.this, listStories);

            listOfStories.setAdapter(adapter);
        }
    }

    public class RefreshStories extends AsyncTask<Void, Void, JSONArray> {
        @Override
        public JSONArray doInBackground(Void... params) {

            JSONArray resultSet = null;
            try {
                if (userFriendId == 0 && mode.equals("MyStory")) {

                    resultSet = new JSONArray(Set_WebServices.getJsonObject(Set_Configurations.User_Stories + Set_Configurations.userId));
                } else if (userFriendId != 0 && mode.equals("FriendStory")) {

                    resultSet = new JSONArray(Set_WebServices.getJsonObject(Set_Configurations.User_Objects + Set_Configurations.userId + "/" + userFriendId));

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return resultSet;
        }

        @Override
        public void onPostExecute(JSONArray result) {

            if (result != null) {
                displayStories(result);
            }

        }
    }

}
