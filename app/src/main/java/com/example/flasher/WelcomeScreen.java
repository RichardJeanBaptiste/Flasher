package com.example.flasher;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import androidx.appcompat.app.AppCompatActivity;

import java.io.File;


public class WelcomeScreen extends AppCompatActivity {


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1_1);

        Button create = findViewById(R.id.test);
        Button existing = findViewById(R.id.exisitingButton);

        CreateSaveFile();

        create.setOnClickListener(view -> {
            Intent intent = new Intent(WelcomeScreen.this, CreateScreen.class);
            startActivity(intent);
        });

        existing.setOnClickListener(view -> {
            Intent intent = new Intent(WelcomeScreen.this, ExistingScreen.class);
            startActivity(intent);

        });

    }

    // Check if save file exist/ Create new save file
    // String folDir = context.getFilesDir().toString() + "/folders";
    public void CreateSaveFile() {

        Context context = this;

        File directory = context.getFilesDir();

        File file = new File(directory, "folders");

        if(file.exists()){
            // do nothing
        } else {
            //create file
            file.mkdir();
        }

    }

}
