package com.universal.homear;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private ImageView mBackBtn;
    private EditText mEmail, mPassword;
    private Button mLogin, mGoogleLogin;
    private TextView mSignUp, mOrText, mForgot, mStatus, mLoading;
    private ProgressBar mProgress;
    private String email, password;

    private String TAG = "com.universal.homear.login";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        mBackBtn = findViewById(R.id.iv_backBtn);
        mEmail = findViewById(R.id.et_email);
        mPassword = findViewById(R.id.et_password);
        mLogin = findViewById(R.id.btn_login);
        mGoogleLogin = findViewById(R.id.btn_google);
        mSignUp = findViewById(R.id.tv_signup);
        mOrText = findViewById(R.id.tv_or);
        mForgot = findViewById(R.id.tv_forgot);
        mStatus = findViewById(R.id.tv_error);
        mLoading = findViewById(R.id.tv_loading);
        mProgress = findViewById(R.id.pb_loading);

        mSignUp.setPaintFlags(mSignUp.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchMainActivity();
            }
        });

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authenticateUser();
            }
        });

        mGoogleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Do nothing for now
            }
        });

        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchRegisterActivity();
            }
        });
    }

    private void authenticateUser() {
        boolean userError = clientUserErrorCheck();
        if(!userError) {
            renderLoading(true);
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                try {
                                    Log.d(TAG, "signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    launchHomeActivity();
                                }
                                catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Exception e = task.getException();
                                Log.w(TAG, "signInWithEmail:failure", e);
                                if(e instanceof FirebaseAuthInvalidUserException || e instanceof FirebaseAuthInvalidCredentialsException){
                                    mStatus.setText("Login details does not match or has been disabled.");

                                }
                                else if (e instanceof FirebaseAuthException){
                                    String errorCode = ((FirebaseAuthException) e).getErrorCode();
                                    mStatus.setText("Error occurred. ERROR CODE:"+errorCode);
                                }
                                else {
                                    mStatus.setText("Unknown error occurred. Please contact support.");
                                }
                                Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                renderLoading(false);
                            }
                        }
                    });
        }
        else {
            mStatus.setText("Please fill out highlighted fields.");
        }
    }

    private void renderLoading(boolean b) {
        if(b) {
            mProgress.setIndeterminate(true);
            mProgress.setVisibility(View.VISIBLE);
            mLoading.setVisibility(View.VISIBLE);
            mBackBtn.setVisibility(View.INVISIBLE);
            mStatus.setVisibility(View.INVISIBLE);
            mEmail.setVisibility(View.INVISIBLE);
            mPassword.setVisibility(View.INVISIBLE);
            mLogin.setVisibility(View.INVISIBLE);
            mOrText.setVisibility(View.INVISIBLE);
            mGoogleLogin.setVisibility(View.INVISIBLE);
            mSignUp.setVisibility(View.INVISIBLE);
            mForgot.setVisibility(View.INVISIBLE);
        }
        else {
            mProgress.setIndeterminate(false);
            mProgress.setVisibility(View.INVISIBLE);
            mLoading.setVisibility(View.INVISIBLE);
            mBackBtn.setVisibility(View.VISIBLE);
            mStatus.setVisibility(View.VISIBLE);
            mEmail.setVisibility(View.VISIBLE);
            mPassword.setVisibility(View.VISIBLE);
            mLogin.setVisibility(View.VISIBLE);
            mOrText.setVisibility(View.VISIBLE);
            mGoogleLogin.setVisibility(View.VISIBLE);
            mSignUp.setVisibility(View.VISIBLE);
            mForgot.setVisibility(View.VISIBLE);
        }
    }

    private boolean clientUserErrorCheck() {
        email = mEmail.getText().toString();
        password = mPassword.getText().toString();

        if(email.isEmpty() && password.isEmpty()) {
            mEmail.setError("Please enter an e-mail!");
            mPassword.setError("Please enter a password!");
            return true;
        }
        else if(email.isEmpty()) {
            mEmail.setError("Please enter an e-mail!");
            return true;
        }
        else if(password.isEmpty()) {
            mPassword.setError("Please enter a password!");
            return true;
        }
        else {
            return false;
        }
    }

    private void launchRegisterActivity() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }

    private void launchHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void launchMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}