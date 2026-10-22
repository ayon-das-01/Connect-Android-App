package com.example.chatapp;

import android.app.Application;

import com.cloudinary.android.MediaManager;

import java.util.HashMap;
import java.util.Map;


public class CloudinaryInitialization extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "din13gzpb");

        try {
            MediaManager.init(getApplicationContext(), config);
        } catch (IllegalStateException ignored) {
            // Already initialized
        }
    }

}
