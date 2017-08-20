package com.example.sdist.testingproject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

import butterknife.InjectView;

public class SignUpActivity extends AppCompatActivity{

    private String stringToPass;

    @InjectView(R.id.input_username) EditText _usernameText;
    @InjectView(R.id.input_email) EditText _emailText;
    @InjectView(R.id.input_password) EditText _passwordText;
    @InjectView(R.id.input_confirm_password) EditText _conPasswordText;
    @InjectView(R.id.input_birthday) EditText _birthday;
    @InjectView(R.id.btn_signup) Button _signupButton;
    @InjectView(R.id.btn_login) TextView _loginbutton;

    private String username;
    private String email;
    private String password;
    private String conPassword;
    private String birthday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


    }

    public class SignUpTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute(){

            try {
                WebServices.sendToJsonObject(Configurations.signup, stringToPass);

            } catch (Exception e) {

            }

        }

        @Override
        protected String doInBackground(Void... params) {

            return null;
        }

        @Override
        protected void onPostExecute(String result) {


        }

    }

    public void onClick(View v){

        username = _usernameText.getText().toString();
        email = _emailText.getText().toString();
        password = _passwordText.getText().toString();
        conPassword = _conPasswordText.getText().toString();
        birthday = "0000-00-00";

        if(password.equals(conPassword))
        {
            stringToPass = "{ \"username\":\"" + username + "\","
                    + "\"email\":\"" + email + "\","
                    + "\"password\":\"" + password + "\","
                    + "\"birthday\":\"" + birthday + "\"";

            new SignUpTask().execute();
        }
        else
        {
            Toast.makeText(getBaseContext(), "Mismatch passwords", Toast.LENGTH_LONG).show();
        }

    }
}
