package com.example.flasher;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

public class ExistingScreen extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main1_3);

            Intent intent = getIntent();
            String folderName = intent.getStringExtra("foldername");
            LinearLayout folderLayout = findViewById(R.id.folderLayout);
            Button ExistingScreenHomeButton = findViewById(R.id.ExistingScreenHomeButton);


            Context context = this;
            String folDir = context.getFilesDir().toString() + "/folders";

            // Create File object from folder string
            File dir = new File(folDir);
            File[] directoryListing = dir.listFiles();

            assert directoryListing != null;
            String[] allFolders = new String[directoryListing.length];


            // Get all folders and Add them to Existing Screen
            for(int i = 0; i < directoryListing.length; i++) {
                allFolders[i] = directoryListing[i].toString();
            }

            for(int j = 0; j < allFolders.length; j++){

                // Split File Text to just get name of folder
                String[] str = allFolders[j].split("/");
                String newBtnText = str[str.length - 1];

                Button newBtn = new Button(context);
                newBtn.setText(newBtnText);

                // Set Button Click Dynamically // Go To Mode Screen With Folder Name
                newBtn.setOnClickListener(v -> {

                    Intent goToModeScreen = new Intent(ExistingScreen.this, ModeScreen.class);
                    goToModeScreen.putExtra("foldername", newBtnText);
                    startActivity(goToModeScreen);

                });

                folderLayout.addView(newBtn);
                Log.d("eTest", allFolders[j]);
            }

            ExistingScreenHomeButton.setOnClickListener(v -> {

                Intent intent12 = new Intent(ExistingScreen.this, WelcomeScreen.class);
                startActivity(intent12);

            });


        }

    }

