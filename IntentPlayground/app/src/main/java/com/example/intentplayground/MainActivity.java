package com.example.intentplayground;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private Button btnGoogle, btnYoutube, btnCustomUrl;
    private EditText editTextUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnGoogle = findViewById(R.id.btn_google);
        btnYoutube = findViewById(R.id.btn_youtube);
        btnCustomUrl = findViewById(R.id.btn_custom_url);
        editTextUrl = findViewById(R.id.edit_text_url);

        btnGoogle.setOnClickListener(v -> openWebPage("https://www.google.com"));
        btnYoutube.setOnClickListener(v -> openWebPage("https://www.youtube.com"));
        btnCustomUrl.setOnClickListener(v -> openCustomUrl());
    }

    private void openCustomUrl() {
        String url = editTextUrl.getText().toString().trim();
        if (url.isEmpty()) {
            Toast.makeText(this, "Masukkan URL terlebih dahulu", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "https://" + url;
        }
        openWebPage(url);
    }

    private void openWebPage(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

        try {

            startActivity(intent);
        } catch (Exception e) {

            Intent webViewIntent = new Intent(this, WebViewActivity.class);
            webViewIntent.putExtra("url", url);
            startActivity(webViewIntent);
        }
    }
}