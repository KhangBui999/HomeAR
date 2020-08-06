package com.universal.homear;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText email, name, password, confirmPassword;
    private TextView login, mStatus;
    private ImageView backBtn;
    private Button signUp;
    private ProgressBar mProgress;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        backBtn = findViewById(R.id.register_backBtn);
        login = findViewById(R.id.register_login);
        email = findViewById(R.id.register_email);
        name = findViewById(R.id.register_name);
        password = findViewById(R.id.register_ps);
        confirmPassword = findViewById(R.id.register_confirm);
        signUp = findViewById(R.id.btn_register);
        mStatus = findViewById(R.id.tv_status);
        mProgress = findViewById(R.id.progressBar);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchLoginActivity();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchLoginActivity();
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewAccount();
                //Toast.makeText(RegisterActivity.this, "Account Created!", Toast.LENGTH_SHORT).show();
                //launchLoginActivity();
            }
        });
    }

    private void createNewAccount() {
        mStatus.setVisibility(View.INVISIBLE);
        String fullName = name.getText().toString();
        String emailValue = email.getText().toString();
        String passwordValue = password.getText().toString();
        String confirmPass = confirmPassword.getText().toString();
        boolean valid = true;

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
            if(!passwordValue.equals(confirmPass)) {
                System.out.println("Check 1");
                password.setError("Passwords do not match!");
                confirmPassword.setError("Passwords do not match!");
            }
            //If the final check if passed it will activate the else statement
            //The block of code will communicate with Firebase to create an account
            else {
                System.out.println("Check 2");
                registerNewUser(emailValue, passwordValue, fullName);
                renderUI(true);
            }
        }

        if(mAuth.getCurrentUser() != null) {
            //signs current user out to prevent any account conflicts
            mAuth.signOut();
        }
    }

    protected void registerNewUser(String email, String password, String name) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(name)
                                    .build();
                            task.getResult().getUser().updateProfile(profileUpdates);
                            setUpDatabaseFiles(user.getUid());
                        }
                        else {
                            Exception e = task.getException();
                            if(e instanceof FirebaseAuthUserCollisionException){
                                mStatus.setText("Login details does not match or has been disabled.");

                            }
                            else if (e instanceof FirebaseAuthException){
                                String errorCode = ((FirebaseAuthException) e).getErrorCode();
                                mStatus.setText("Error occurred. ERROR CODE:"+errorCode);
                            }
                            else {
                                mStatus.setText("Unknown error occurred. Please contact support (e-mail: support@homear.com).");
                            }
                            Toast.makeText(RegisterActivity.this, "Registration failed.", Toast.LENGTH_SHORT).show();
                        }
                        renderUI(false);
                    }
                });
    }

    private void setUpDatabaseFiles(String uid) {
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("cartItems", new ArrayList<>());
        userInfo.put("viewed", new ArrayList<>());
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("ShoppingCart").document(uid)
                .set(userInfo)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        launchLoginActivity();
                        Toast.makeText(RegisterActivity.this, "Account created!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mAuth.getCurrentUser().delete();
                        Toast.makeText(RegisterActivity.this, "Account creation failed - network error",
                                Toast.LENGTH_SHORT).show();
                        renderUI(false);
                    }
                });
    }


    private void launchLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private void renderUI(boolean invisible) {
        if(invisible) {
            mProgress.setIndeterminate(true);
            mProgress.setVisibility(View.VISIBLE);
            mStatus.setVisibility(View.INVISIBLE);
            backBtn.setVisibility(View.INVISIBLE);
            email.setVisibility(View.INVISIBLE);
            name.setVisibility(View.INVISIBLE);
            password.setVisibility(View.INVISIBLE);
            confirmPassword.setVisibility(View.INVISIBLE);
            signUp.setVisibility(View.INVISIBLE);
            login.setVisibility(View.INVISIBLE);
        }
        else {
            mProgress.setIndeterminate(false);
            mProgress.setVisibility(View.INVISIBLE);
            backBtn.setVisibility(View.VISIBLE);
            email.setVisibility(View.VISIBLE);
            name.setVisibility(View.VISIBLE);
            password.setVisibility(View.VISIBLE);
            confirmPassword.setVisibility(View.VISIBLE);
            signUp.setVisibility(View.VISIBLE);
            login.setVisibility(View.VISIBLE);
        }
    }

}