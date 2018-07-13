package com.example.anti2.instagramproject.Profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.anti2.instagramproject.R;
import com.example.anti2.instagramproject.Utils.BottomNavigationViewHelper;
import com.example.anti2.instagramproject.Utils.GridImageAdapter;
import com.example.anti2.instagramproject.Utils.UniversalImageLoader;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";
    private static final int ACTIVITY_NUM = 4;
    private static final int NUM_GRID_COLUMNS = 3;

    private Context mContext = ProfileActivity.this;

    private ProgressBar mProgressBar;
    private ImageView profilePhoto;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Log.d(TAG, "onCreate: started.");

        setupActivityWidgets();
        setupBottomNavigationView();
        setupToolbar();
        setProfileImage();

        tempGridSetup();
    }

    private void tempGridSetup() {

        List<String> imgURLs = new ArrayList<>();
        imgURLs.add("https://cdn.pixabay.com/photo/2018/05/30/15/31/rustic-3441673__340.jpg");
        imgURLs.add("https://cdn.pixabay.com/photo/2017/11/10/08/10/son-2935723__340.jpg");
        imgURLs.add("https://cdn.pixabay.com/photo/2018/05/07/22/08/sydney-opera-house-3381786__340.jpg");
        imgURLs.add("https://cdn.pixabay.com/photo/2018/04/12/11/44/apple-3313225__340.jpg");
        imgURLs.add("https://cdn.pixabay.com/photo/2017/06/05/14/55/glass-2374311__340.jpg");
        imgURLs.add("https://cdn.pixabay.com/photo/2016/06/20/03/15/pier-1467984__340.jpg");
        imgURLs.add("https://cdn.pixabay.com/photo/2018/06/28/15/23/soft-fruits-3504149__340.jpg");
        imgURLs.add("https://cdn.pixabay.com/photo/2018/07/01/20/01/mercedes-3510327__340.jpg");
        imgURLs.add("https://cdn.pixabay.com/photo/2018/07/06/00/33/person-3519503__340.jpg");
        imgURLs.add("https://cdn.pixabay.com/photo/2018/06/10/22/48/pawns-3467512__340.jpg");
        imgURLs.add("https://cdn.pixabay.com/photo/2018/06/10/17/40/olives-3466908__340.jpg");
        imgURLs.add("https://cdn.pixabay.com/photo/2018/07/03/10/47/berries-3513547__340.jpg");
        imgURLs.add("https://cdn.pixabay.com/photo/2018/07/04/00/19/champagne-3515140__340.jpg");

        setupImageGrid(imgURLs);
    }

    private void setupImageGrid(List<String> imgURLs) {
        GridView gridView = (GridView) findViewById(R.id.gridView);

        int gridWidth = getResources().getDisplayMetrics().widthPixels;
        int imageWidth = gridWidth/NUM_GRID_COLUMNS;
        gridView.setColumnWidth(imageWidth);

        GridImageAdapter adapter =
                new GridImageAdapter(mContext, R.layout.layout_grid_imageview, "", imgURLs);

        gridView.setAdapter(adapter);
    }

    private void setProfileImage() {
        Log.d(TAG, "setProfileImage: setting profile photo");
        String imgURL = "encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTztvvYG3N7n_wCj5ZCGyxMOyuhIuDZc32FAVK7rmCY7KrNqG0uDA";
        UniversalImageLoader.setImage(imgURL, profilePhoto, null, "https://");
    }

    private void setupActivityWidgets() {
        mProgressBar = (ProgressBar) findViewById(R.id.profileProgressBar);
        mProgressBar.setVisibility(View.INVISIBLE);
        profilePhoto = (ImageView) findViewById(R.id.profile_photo);
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.profileToolBar);
        setSupportActionBar(toolbar);

        ImageView profileMenu = (ImageView) findViewById(R.id.profileMenu);
        profileMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: navigating to account settings");
                Intent intent = new Intent(mContext, AccountSettingsActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     *BottomNavigationView setup
     */
    private void setupBottomNavigationView() {
        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContext, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

}
