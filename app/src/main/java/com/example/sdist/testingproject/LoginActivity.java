package com.example.sdist.testingproject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity{

    String username = "";
    String password = "";
    String resultPassword = "";

    EditText W_Username;
    EditText W_Password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public class LoginTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute(){
            try {
                JSONObject jsonObject = new JSONObject(WebServices.getJsonObject(Configurations.login + username));
                resultPassword = jsonObject.getString("password");

            } catch (Exception e) {

            }

        }

        @Override
        protected String doInBackground(Void... params) {

            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            if(password.matches(resultPassword)){
                Toast.makeText(LoginActivity.this, "Login success", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
            }

        }

    }
/*
    public void login(View v){

        W_Username = (EditText) findViewById(R.id.text_Username);
        W_Password  = (EditText) findViewById(R.id.text_Password);

        username = W_Username.getText().toString();
        password = W_Password.getText().toString();

        new LoginTask().execute();

    }*/
}
