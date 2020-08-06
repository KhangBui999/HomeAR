package com.universal.homear;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

/**
 * This activity renders the account management page. Currently, a user is only able to sign out.
 */
public class AccountActivity extends AppCompatActivity {

    private Button mLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        mLogout = findViewById(R.id.btn_logout);

        //Allows a user to sign-out
        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(AccountActivity.this, MainActivity.class);
                //Prevents users from being able to access previous activity -- allows for
                //secure authentication and authorisation
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });
    }
}