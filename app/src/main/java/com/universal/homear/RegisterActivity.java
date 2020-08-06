package com.universal.homear;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private EditText email, name, password, confirmPassword;
    private TextView login;
    private ImageView backbtn;
    private Button signup;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        backbtn = findViewById(R.id.register_backBtn);
        login = findViewById(R.id.register_login);
        email = findViewById(R.id.register_email);
        name = findViewById(R.id.register_name);
        password = findViewById(R.id.register_ps);
        confirmPassword = findViewById(R.id.register_confirm);
        signup = findViewById(R.id.btn_register);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchLoginActivity();
            }
        });

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchLoginActivity();
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(RegisterActivity.this, "Account Created!", Toast.LENGTH_SHORT).show();
                launchLoginActivity();
            }
        });
    }

    private void createNewAccount() {
        String fullName = name.getText().toString();
        String emailValue = email.getText().toString();
        String passwordValue = password.getText().toString();
        String confirmPass = confirmPassword.getText().toString();
        boolean valid = false;

        if(fullName.isEmpty()) {
            name.setError("Please enter a name!");
            valid = false;
        }
        if(emailValue.isEmpty()) {
            email.setError("Please enter an e-mail!");
            valid = false;
        }
        if(passwordValue.isEmpty()) {
            password.setError("Please enter a password!");
            valid = false;
        }
        if(confirmPass.isEmpty()) {
            confirmPassword.setError("Please confirm your password!");
            valid = false;
        }

        if(valid) {
            //Length check for name
            if(fullName.length() < 2) {
                name.setError("Display name must be greater than 2 characters!");
                valid = false;
            }
            //Length check for password fields (must be 6 or more characters)
            if(passwordValue.length() < 6) {
                password.setError("Passwords must be at least 6 characters!");
                valid = false;
            }
            if(confirmPass.length() < 6) {
                confirmPassword.setError("Passwords must be at least 6 characters!");
                valid = false;
            }
        }

        if(valid) {
            if(!password.equals(confirmPass)) {
                password.setError("Passwords do not match!");
                confirmPassword.setError("Passwords do not match!");
                valid = false;
            }
            //If the final check if passed it will activate the else statement
            //The block of code will communicate with Firebase to create an account
            else {
                registerNewUser(emailValue, passwordValue, fullName);
            }
        }

        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null) {
            //signs current user out to prevent any account conflicts
            FirebaseAuth.getInstance().signOut();
        }
    }

    protected void registerNewUser(String email, String password, String name) {
        //do this after break
    }


    private void launchLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

}