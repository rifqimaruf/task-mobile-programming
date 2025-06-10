package com.example.camera;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity
{
//    deklarasi variabel
    private Button openCamera;
    private ImageView clickedImage;

//    create launcher
    private ActivityResultLauncher<Intent> takePictureLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // inisiasi button dan imageview
        openCamera = findViewById(R.id.camera_open);
        clickedImage = findViewById(R.id.click_image);
        // inisiasi launcher
        takePictureLauncher = registerForActivityResult
                (new ActivityResultContracts.StartActivityForResult(),
                        result ->
                    {
                        if(result.getData() != null && result.getData().getExtras() != null)
                        {
                            Bitmap photo = (Bitmap) result.getData().getExtras().get("data");
                            if(photo != null)
                            {
                                clickedImage.setImageBitmap(photo);
                            }
                        }
                    }
                );

        // set a click listener
        openCamera.setOnClickListener(v -> {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            takePictureLauncher.launch(cameraIntent);
        });
    }


}