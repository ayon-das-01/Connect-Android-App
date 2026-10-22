package com.example.chatapp;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class SelectProfilePic extends AppCompatActivity {


    ImageView profilePicId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_profile_pic);

        profilePicId = findViewById(R.id.profilePicId);

        String profilePicUrl = getIntent().getStringExtra("profilePicUrl");

        if (profilePicUrl == null || profilePicUrl.isEmpty()) {

            profilePicId.setImageResource(R.drawable.name);

        } else {

            Picasso.with(SelectProfilePic.this)
                    .load(profilePicUrl)
                    .placeholder(R.drawable.name)
                    .error(R.drawable.name)
                    .into(profilePicId);
        }


    }
}