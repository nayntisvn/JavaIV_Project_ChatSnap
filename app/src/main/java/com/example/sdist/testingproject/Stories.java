package com.example.sdist.testingproject;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Stories extends AppCompatActivity {

    Intent intent;
    ListView listOfStories;
    ArrayList<Story> listStories;

    int friendUserId;
    String mode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stories);

        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            mode = extras.getString("Mode");
        }

        listStories = new ArrayList<Story>();

        new RefreshStories().execute();
    }

    public class mAdapter extends ArrayAdapter<Story> {

        public mAdapter(@NonNull Context context, ArrayList<Story> objects) {
            super(context, 0, objects);
        }
        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (view == null) {
                view = LayoutInflater.from(getContext()).inflate(R.layout.story, viewGroup, false);

                viewHolder = new Stories.mAdapter.ViewHolder();

                viewHolder.storyImageView = (ImageView) view.findViewById(R.id.story_image);
                viewHolder.storyTextView = (TextView) view.findViewById(R.id.story_time);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }

            Story myModel = getItem(position);

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

        try {
            JSONArray ar = new JSONArray(result.toString());
            for (int i = 0; i < ar.length(); i++){
                JSONObject a = ar.getJSONObject(i);
                Story story = new Story();

//                story.setStorySnap(a.getString("username"));
//                story.setStoryTime(Integer.valueOf(a.getString("userFriendId")));

                listStories.add(story);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Stories.mAdapter adapter = new Stories.mAdapter(Stories.this, listStories);

        listOfStories.setAdapter(adapter);

    }

    public class RefreshStories extends AsyncTask<Void, Void, JSONArray>
    {
        @Override
        public JSONArray doInBackground(Void... params){

            JSONArray resultSet = null;
            try{

//                resultSet = new JSONArray(Set_WebServices.getJsonObject(Set_Configurations. + Set_Configurations.userId));

            }catch(Exception e)
            {
                Toast.makeText(Stories.this, "No Network", Toast.LENGTH_SHORT).show();
            }

            return resultSet;
        }

        @Override
        public void onPostExecute(JSONArray result){

            if(result != null)
            {
                displayStories(result);
            }

        }
    }

}
