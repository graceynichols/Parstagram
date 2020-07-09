package com.example.parstagram.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.parstagram.R;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class LoginActivity extends AppCompatActivity {

    public static final String TAG = "LoginActivity";
    private static final int USERNAME_ERROR_CODE = 202;
    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin;
    private Button btnSignup;
    private ProgressBar pb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Put instagram photo on the actionbar
        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        this.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white)));
        //this.getSupportActionBar().set
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.actionbar);

        if (ParseUser.getCurrentUser() != null) {
            goMainActivity();
        }
        pb = findViewById(R.id.pbLoading);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignup = findViewById(R.id.btnSignup);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "onClick login button");
                pb.setVisibility(ProgressBar.VISIBLE);
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                loginUser(username, password);
            }
        });
        // On click listener for sign up button
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pb.setVisibility(ProgressBar.VISIBLE);
                Log.i(TAG, "onClick signup button");
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                signupUser(username, password);
            }
        });
    }

    private void loginUser(String username, String password) {
        Log.i(TAG, "Attempting to login user " + username);

        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with login", e);
                    Toast.makeText(LoginActivity.this, "Issue with login", Toast.LENGTH_SHORT).show();
                    return;
                }
                pb.setVisibility(ProgressBar.INVISIBLE);
                goMainActivity();
            }
        });
    }

    private void signupUser(String username, String password) {
        Log.i(TAG, "Attempting to signup user " + username);
        if (username.equals("")) {
            Toast.makeText(getApplicationContext(), "Username missing!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.equals("")) {
            Toast.makeText(getApplicationContext(), "Password missing!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length() < 6) {
            Toast.makeText(getApplicationContext(), "Password must be at least 6 characters!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.equals(username)) {
            Toast.makeText(getApplicationContext(), "Password cannot be the same as username", Toast.LENGTH_SHORT).show();
            return;
        }
        ParseUser user = new ParseUser();
        // Set core properties
        user.setUsername(username);
        user.setPassword(password);
        // Invoke signUpInBackground
        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    // Hooray! Let them use the app now.
                    pb.setVisibility(ProgressBar.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "Successful sign up!", Toast.LENGTH_SHORT).show();
                    goMainActivity();
                } else {
                    // Sign up didn't succeed. Look at the ParseException
                    // to figure out what went wrong
                    if (e.getCode() == ParseException.USERNAME_TAKEN) {
                        Toast.makeText(getApplicationContext(), "Username already taken", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Error while signing up", Toast.LENGTH_SHORT).show();
                    }
                    Log.i(TAG, e.toString());
                }
            }
        });
    }

    private void goMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}