package com.example.sdist.testingproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "SignUpActivity";
    private static final int REQUEST_SIGNUP = 0;

//    Controls/Swings/Views of page
    @InjectView(R.id.input_username) EditText _usernameText;
    @InjectView(R.id.input_password) EditText _passwordText;
    @InjectView(R.id.btn_login) Button _loginButton;
    @InjectView(R.id.link_signup) TextView _signupLink;

//    Local variables
    private String error = "";
    private String username = "";
    private String password = "";
    private String resultPassword = "";
    private int ClearToGo = 0;  // Login Checker
    Intent intent;
    FileOutputStream file_Write;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        Set_Configurations.user_Details = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "userdetails.txt");

        Set_DatabaseOffline data = new Set_DatabaseOffline(this);

        ConstraintLayout constraintLayout = (ConstraintLayout) findViewById(R.id.root_layout);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(2000);
        animationDrawable.start();

        if(CheckCurrentUser()) {

            intent = new Intent(getApplicationContext(),homepage.class);
            startActivity(intent);
        }

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });

    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this,
                R.style.AppTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        username = _usernameText.getText().toString();
        password = _passwordText.getText().toString();

        // TODO: Implement your own authentication logic here.

        new LoginTask().execute();

        progressDialog.dismiss();
        _loginButton.setEnabled(true);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful Web_SignUp logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {

        _loginButton.setEnabled(true);

        intent = new Intent(getApplicationContext(), homepage.class);
        startActivity(intent);
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed : Empty Fields", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _usernameText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty()) {
            _usernameText.setError("username is empty");
            valid = false;
        } else {
            _usernameText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }


        return valid;
    }

    public class LoginTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute(){

        }

        @Override
        protected String doInBackground(Void... params) {

            String stat = null;
            String toPass = "{\"username\" : \"%s\", \"password\" : \"%s\"}";

            try {
                stat = Set_WebServices.putJsonObject(Set_Configurations.Web_Login, String.format(toPass, username, password));
                ///
            } catch (Exception e) {
                e.printStackTrace();
            }

            return stat;
        }

        @Override
        protected void onPostExecute(String result) {

            switch (result)
            {
                case "0" : {
                    Toast.makeText(MainActivity.this, "Username doesn't exist!", Toast.LENGTH_SHORT).show();
                    ClearToGo = 0;

                    break;
                }
                case "10": {
                    Toast.makeText(MainActivity.this, "Incorrect Password!", Toast.LENGTH_SHORT).show();
                    ClearToGo = 0;

                    break;
                }
                case "11": {
                    Toast.makeText(MainActivity.this, "Login success", Toast.LENGTH_SHORT).show();

                    try {
                        file_Write = new FileOutputStream(Set_Configurations.user_Details);
                        file_Write.write(username.getBytes());
                        file_Write.flush();
                        file_Write.close();
                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }

                    new android.os.Handler().postDelayed(
                            new Runnable() {
                                public void run() {
                                    // On complete call either onLoginSuccess or onLoginFailed
                                    onLoginSuccess();
                                    // onLoginFailed();
                                }
                            }, 0);

                    break;
                }
                case "2": {
                    Toast.makeText(MainActivity.this, "User already logged in!", Toast.LENGTH_SHORT).show();
                    ClearToGo = 0;

                    break;
                }
            }

        }

    }

//    Insert checker of current user here.
    public boolean CheckCurrentUser(){
//
//        try {
//            file_Write = new FileOutputStream(Set_Configurations.user_Details);
//            file_Write.write(username.getBytes());
//            file_Write.flush();
//            file_Write.close();
//        }
//        catch(Exception e)
//        {
//            e.printStackTrace();
//        }

        if(Set_Configurations.user_Details.exists()){
            return true;
        }
        else{
            return false;
        }
    }

/*
    public void redirectToNext(View v)
    {
        if(v.getId() == R.id.button_login){

            Intent i = new Intent(MainActivity.this, SignUpActivity.class);
            startActivity(i);

        }
        if(v.getId() == R.id.button_signUp){

//            Intent i = new Intent(MainActivity.this, SignUp.class);
//
//            startActivity(i);
        }


    }*/

}
