package com.example.sdist.testingproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
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
    private String username = "";
    private String password = "";
    private String resultPassword = "";
    private int ClearToGo = 0;  // Login Checker

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        ConstraintLayout constraintLayout = (ConstraintLayout) findViewById(R.id.root_layout);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(1000);
        animationDrawable.setExitFadeDuration(500);
        animationDrawable.start();

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

        Intent intent = new Intent(getApplicationContext(), homepage.class);
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

            try {
                JSONObject jsonObject = new JSONObject(Set_WebServices.getJsonObject(Set_Configurations.Web_Login + username));
                resultPassword = jsonObject.getString("password");

            } catch (Exception e) {

            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            if(password.matches(resultPassword)){

                Toast.makeText(MainActivity.this, "Login success", Toast.LENGTH_SHORT).show();

                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                // On complete call either onLoginSuccess or onLoginFailed
                                onLoginSuccess();
                                // onLoginFailed();

                            }
                        }, 0);

            }else{
                ClearToGo = 0;
                Toast.makeText(MainActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
            }
;;
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
