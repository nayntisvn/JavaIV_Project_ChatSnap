package com.example.sdist.testingproject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.sdist.testingproject.FaceTracker.CameraActivity;

public class Activity_Homepage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                intent = new Intent(getApplicationContext(), CameraActivity.class);
                intent.putExtra("userFriendId", "0");

                startActivity(intent);

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        TextView user = (TextView) findViewById(R.id.user_profile_name);
        TextView userwelcome = (TextView) findViewById(R.id.user_profile_short_bio);
        TextView email = (TextView) findViewById(R.id.txt_Email);

        user.setText(Set_Configurations.Username);
        userwelcome.setText("Welcome back " + Set_Configurations.Username);
//        email.setText(Set_Configurations.Email);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.homepage, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_myStory) {
            // Handle the camera action
            intent = new Intent(getApplicationContext(), Activity_Stories.class);
            intent.putExtra("Mode", "MyStory");
            intent.putExtra("userFriendId", "0");
            startActivity(intent);

        } else if (id == R.id.nav_friends) {

            intent = new Intent(getApplicationContext(), Activity_Friendlist.class);
            intent.putExtra("Mode", "Messages");
            startActivity(intent);

        } else if (id == R.id.nav_stories) {

            intent = new Intent(getApplicationContext(), Activity_Friendlist.class);
            intent.putExtra("Mode", "Activity_Stories");
            startActivity(intent);

        } else if (id == R.id.nav_send) {
            Set_Configurations.user_Details.delete();

            new Logout().execute();

            super.onBackPressed();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public class Logout extends AsyncTask<Void, Void, Void> {

        @Override
        public Void doInBackground(Void... params)
        {
            String toPass = "{ \"username\":\""+ Set_Configurations.Username + "\"}";

            try {
                Set_WebServices.putJsonObject(Set_Configurations.Web_Logout, toPass);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

    }
}
