package com.example.anti2.instagramproject.Utils;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.example.anti2.instagramproject.Login.LoginActivity;
import com.example.anti2.instagramproject.Models.User;
import com.example.anti2.instagramproject.Models.UserAccountSettings;
import com.example.anti2.instagramproject.Models.UserSettings;
import com.example.anti2.instagramproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseMethods {

    private static final String TAG = "FirebaseMethods";

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;

    private String userID;

    private Context mContext;

    public FirebaseMethods(Context context) {
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        mContext = context;

        if(mAuth.getCurrentUser() != null) {
            userID = mAuth.getCurrentUser().getUid();
        }

    }

    public void updateUsername(String username) {

        Log.d(TAG, "updateUsername: updating username to : "+username);
        myRef.child(mContext.getString(R.string.dbname_users))
                .child(userID)
                .child(mContext.getString(R.string.field_username))
                .setValue(username);
        myRef.child(mContext.getString(R.string.dbname_user_account_settings))
                .child(userID)
                .child(mContext.getString(R.string.field_username))
                .setValue(username);
        Toast.makeText(mContext, "updateUsername", Toast.LENGTH_SHORT).show();
    }

//    public boolean checkIfUsernameExists(String username, DataSnapshot dataSnapshot) {
//        Log.d(TAG, "checkIfUsernameExists: checking if " + username + " already exists");
//
//        User user = new User();
//
//        for(DataSnapshot ds : dataSnapshot.child(userID).getChildren()) {
//            Log.d(TAG, "checkIfUsernameExists: datasnapshot: " + ds);
//
//            user.setUsername(ds.getValue(User.class).getUsername());
//            Log.d(TAG, "checkIfUsernameExists: username :" + user.getUsername());
//
//            if(StringManipulation.expandUsername(user.getUsername()).equals(username)) {
//                Log.d(TAG, "checkIfUsernameExists: Found a match! " + user.getUsername());
//                return true;
//            }
//        }
//        return false;
//    }

    public void registerNewEmail(final String email, String password, final String username) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //send verification email
                            sendVerificatonEmail();

                            userID = mAuth.getCurrentUser().getUid();
                            Log.d(TAG, "onComplete: Authstate changed: "+userID);

                            Toast.makeText(mContext, R.string.auth_succeed, Toast.LENGTH_SHORT).show();

                            Intent loginIntent = new Intent(mContext, LoginActivity.class);
                            mContext.startActivity(loginIntent);
                        } else {
                            Toast.makeText(mContext, R.string.auth_failed, Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    public void sendVerificatonEmail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user != null) {
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()) {

                    } else {

                    }
                }
            });
        }
    }


    /**
     * add information to the users nodes
     * add information to the user_account_settings node
     * @param email
     * @param username
     * @param description
     * @param website
     * @param profile_photo
     */
    public void addNewUser(String email, String username, String description, String website, String profile_photo) {

        User user = new User(userID, 1, email, StringManipulation.condenseUsername(username));

        myRef.child(mContext.getString(R.string.dbname_users))
                .child(userID)
                .setValue(user);

        UserAccountSettings settings = new UserAccountSettings(
                description,
                username,
                0,
                0,
                0,
                profile_photo,
                StringManipulation.condenseUsername(username),
                website
        );

        myRef.child(mContext.getString(R.string.dbname_user_account_settings))
                .child(userID)
                .setValue(settings);
    }

    /**
     * Retrieves the account settings for the user currently logged in
     * Database: user_account_settings node
     * @param dataSnapshot
     * @return
     */
    public UserSettings getUserSettings(DataSnapshot dataSnapshot) {
        Log.d(TAG, "getUserAccountSettings: retrieving user account from firebase");

        UserAccountSettings settings = new UserAccountSettings();

        User user = new User();

        for(DataSnapshot ds : dataSnapshot.getChildren()) {
            //user_account_settings node
            if(ds.getKey().equals(mContext.getString(R.string.dbname_user_account_settings))) {
                Log.d(TAG, "getUserAccountSettings: datasnapshot: " + ds);
                try {
                    settings.setDisplay_name(ds.child(userID)
                            .getValue(UserAccountSettings.class)
                            .getDisplay_name()
                    );
                    settings.setUsername(ds.child(userID)
                            .getValue(UserAccountSettings.class)
                            .getUsername()
                    );
                    settings.setWebsite(ds.child(userID)
                            .getValue(UserAccountSettings.class)
                            .getWebsite()
                    );
                    settings.setDescription(ds.child(userID)
                            .getValue(UserAccountSettings.class)
                            .getDescription()
                    );
                    settings.setProfile_photo(ds.child(userID)
                            .getValue(UserAccountSettings.class)
                            .getProfile_photo()
                    );
                    settings.setPosts(ds.child(userID)
                            .getValue(UserAccountSettings.class)
                            .getPosts()
                    );
                    settings.setFollowing(ds.child(userID)
                            .getValue(UserAccountSettings.class)
                            .getFollowing()
                    );
                    settings.setFollowers(ds.child(userID)
                            .getValue(UserAccountSettings.class)
                            .getFollowers()
                    );
                    Log.d(TAG, "getUserAccountSettings: retrieved user_account_settings information: "+settings.toString());

                } catch (NullPointerException e) {
                    Log.d(TAG, "getUserAccountSettings: NullPointerException "+e.getMessage());
                }

                //users node
                if(ds.getKey().equals(mContext.getString(R.string.dbname_user_account_settings))) {
                    Log.d(TAG, "getUserAccountSettings: datasnapshot: " + ds);

                    user.setUsername(ds.child(userID)
                            .getValue(User.class)
                            .getUsername()
                    );
                    user.setEmail(ds.child(userID)
                            .getValue(User.class)
                            .getEmail()
                    );
                    user.setPhone_number(ds.child(userID)
                            .getValue(User.class)
                            .getPhone_number()
                    );
                    user.setUser_id(ds.child(userID)
                            .getValue(User.class)
                            .getUser_id()
                    );

                    Log.d(TAG, "getUserAccountSettings: retrieved users: "+user.toString());
                }
            }
        }
        return new UserSettings(user, settings);
    }

}
