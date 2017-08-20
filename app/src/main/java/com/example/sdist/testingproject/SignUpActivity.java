package com.example.sdist.testingproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SignUpActivity extends AppCompatActivity{

    private String stringToPass;

    private static final String TAG = "SignupActivity";

    @InjectView(R.id.input_username) EditText _usernameText;
    @InjectView(R.id.input_email) EditText _emailText;
    @InjectView(R.id.input_password) EditText _passwordText;
    @InjectView(R.id.input_confirm_password) EditText _conPasswordText;
    @InjectView(R.id.input_birthday) EditText _birthday;
    @InjectView(R.id.btn_signup) Button _signupButton;
    @InjectView(R.id.link_login) TextView _loginLink;

    private String username;
    private String email;
    private String password;
    private String conPassword;
    private String birthday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.inject(this);

        _signupButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
            }
        });

    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);


        username = _usernameText.getText().toString();
        email = _emailText.getText().toString();
        password = _passwordText.getText().toString();
        conPassword = _conPasswordText.getText().toString();
        birthday = _birthday.getText().toString();

        // TODO: Implement your own signup logic here.

        if(password.equals(conPassword)) {
            final ProgressDialog progressDialog = new ProgressDialog(SignUpActivity.this,
                    R.style.AppTheme);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Creating Account...");
            progressDialog.show();

            stringToPass = "{ \"username\":\"" + username + "\","
                    + "\"email\":\"" + email + "\","
                    + "\"password\":\"" + password + "\","
                    + "\"birthday\":\"" + birthday + "\"}";

            new SignUpTask().execute();

            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            // On complete call either onSignupSuccess or onSignupFailed
                            // depending on success
                            onSignupSuccess();
                            // onSignupFailed();
                            progressDialog.dismiss();
                        }
                    }, 3000);
        }
        else
        {
            onSignupFailed();
        }
    }

    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Sign Up failed : Mismatched Password", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _usernameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _usernameText.setError("at least 3 characters");
            valid = false;
        } else {
            _usernameText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }

    public class SignUpTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute(){

        }

        @Override
        protected String doInBackground(Void... params) {

            try {

                    WebServices.sendToJsonObject(Configurations.signup, stringToPass);

            } catch (Exception e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            if(password.matches(conPassword)){
                Toast.makeText(SignUpActivity.this, "Sign Up Success", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(SignUpActivity.this, "Mismatched Password", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
