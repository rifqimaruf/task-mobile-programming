package com.example.simpleprintout;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private EditText editTextNama;
    private EditText editTextNRP;
    private Button buttonSubmit;
    private TextView textViewResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inisialisasi komponen UI
        editTextNama = findViewById(R.id.editTextNama);
        editTextNRP = findViewById(R.id.editTextNim);
        buttonSubmit = findViewById(R.id.buttonSubmit);
        textViewResult = findViewById(R.id.textViewResult);

        // Set listener untuk tombol submit
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nama = editTextNama.getText().toString().trim();
                String nim = editTextNRP.getText().toString().trim();

                // Validasi input
                if (nama.isEmpty() || nim.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Nama dan NIM harus diisi!", Toast.LENGTH_SHORT).show();
                } else {
                    // Tampilkan hasil di TextView
                    String result = "Selamat datang " + nama + " dengan NIM " + nim;
                    textViewResult.setText(result);
                }
            }
        });
    }
}