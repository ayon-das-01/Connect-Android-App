package com.example.chatapp;

import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

public class VideoActivity extends AppCompatActivity {

    private WebView videoWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_video);

        videoWebView = findViewById(R.id.videoWebView);

        String videoUrl =
                getIntent().getStringExtra("videoUrl");

        WebSettings settings = videoWebView.getSettings();

        settings.setJavaScriptEnabled(true);
        settings.setMediaPlaybackRequiresUserGesture(false);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);

        videoWebView.setWebChromeClient(new WebChromeClient());
        videoWebView.setWebViewClient(new WebViewClient());

        videoWebView.loadUrl(videoUrl);
    }

    @Override
    protected void onDestroy() {
        if (videoWebView != null) {
            videoWebView.destroy();
        }

        super.onDestroy();
    }
}