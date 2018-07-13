package com.example.anti2.instagramproject.Login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anti2.instagramproject.Home.HomeActivity;
import com.example.anti2.instagramproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity{

    private static final String TAG = "RegisterActivity";

    private Context mContext;
    private String email, password, username;
    private EditText mEmail, mPassword, mUsername;
    private ProgressBar mProgressBar;
    private TextView mPleaseWait;

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Log.d(TAG, "onCreate: started");

        initWidgets();
        setupFirebaseAuth();

    }

    private void initWidgets() {
        Log.d(TAG, "initWidgets: initializing widgets");
        mEmail = (EditText) findViewById(R.id.input_email);
        mPassword = (EditText) findViewById(R.id.input_password);
        mUsername = (EditText) findViewById(R.id.input_username);
        mContext = RegisterActivity.this;

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mPleaseWait = (TextView) findViewById(R.id.pleasewait);

        mProgressBar.setVisibility(View.INVISIBLE);
        mPleaseWait.setVisibility(View.INVISIBLE);

    }

    private boolean isStringNull(String string) {
        Log.d(TAG, "isStringNull: checking string if nulll");
        if(string.equals("")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * ------------------------------------ firebase ----------------------------------------
     */

    /**
     * setup the firebase auth object
     */
    private void setupFirebaseAuth() {
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth");
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if(user != null) {
                    //user is signed in
                    Log.d(TAG, "onAuthStateChanged: signed_in : " +user.getUid());
                } else {
                    Log.d(TAG, "onAuthStateChanged: signed_out");
                }

            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
