package com.universal.homear;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private TextView mLogin;
    private TextView mRegister;
    private FirebaseAuth mAuth;
    private String TAG = "com.universal.homear.main";
    ImageView imageViews;
    AnimationDrawable imagesAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageViews = findViewById(R.id.start_screen);
        /*imageViews.setBackgroundResource(R.drawable.start_up_slideshow);
        imagesAnimation = (AnimationDrawable) imageViews.getBackground();
        imagesAnimation.start();*/
        final int[] imageArray = { R.drawable.start_up1, R.drawable.start_up2};
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            int i = 0;

            public void run() {
                imageViews.setImageResource(imageArray[i]);
                i++;
                if (i > imageArray.length - 1) {
                    i = 0;
                }
                handler.postDelayed(this, 5000);
            }
        };
        handler.postDelayed(runnable, 2000);
        mLogin = findViewById(R.id.tv_login);
        mRegister = findViewById(R.id.tv_signup);
        mRegister.setPaintFlags(mRegister.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchLoginActivity();
            }
        });

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchSignUpActivity();
            }
        });
    }

    /**
     * Executed after the onCreate method
     * DEVELOPMENT NOTE:
     * bypassLogin(currentUser) should be commented out when working on authentication or MainActivity
     */
    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //bypassLogin(currentUser);
    }

    /**
     * Allows user to log in instantly if they have are still logged in on their phones.
     * @param user is the current Firebase user account.
     */
    protected void bypassLogin(FirebaseUser user) {
        if(user != null) {
            Log.d(TAG, "Logged in user:"+user.getUid()+". Bypassing login.");
            launchHomeActivity();
        }
        else {
            Log.d(TAG, "No current user detected. Authentication form must be filled.");
        }
    }

    private void launchLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private void launchSignUpActivity() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private void launchHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

}
