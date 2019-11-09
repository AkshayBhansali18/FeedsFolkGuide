package com.example.feeds_folkguide;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

public class WebViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        WebView webView=findViewById(R.id.webView);
        Intent intent=getIntent();
        webView.loadUrl(intent.getStringExtra("link"));

    }
}
