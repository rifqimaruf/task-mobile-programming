package com.example.colorchanger;

import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {
    private LinearLayout rootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rootLayout = findViewById(R.id.rootLayout);
        // Set default color ke hitam
        rootLayout.setBackgroundColor(Color.BLACK);

        findViewById(R.id.buttonRed).setOnClickListener(v -> rootLayout.setBackgroundColor(Color.RED));
        findViewById(R.id.buttonBlue).setOnClickListener(v -> rootLayout.setBackgroundColor(Color.BLUE));
        findViewById(R.id.buttonGreen).setOnClickListener(v -> rootLayout.setBackgroundColor(Color.GREEN));
        findViewById(R.id.buttonYellow).setOnClickListener(v -> rootLayout.setBackgroundColor(Color.YELLOW));
        findViewById(R.id.buttonPurple).setOnClickListener(v -> rootLayout.setBackgroundColor(Color.parseColor("#800080")));
        findViewById(R.id.buttonOrange).setOnClickListener(v -> rootLayout.setBackgroundColor(Color.parseColor("#FFA500")));
        findViewById(R.id.buttonReset).setOnClickListener(v -> rootLayout.setBackgroundColor(Color.BLACK));
    }
}