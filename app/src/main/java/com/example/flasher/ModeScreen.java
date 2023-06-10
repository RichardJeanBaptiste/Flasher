package com.example.flasher;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;


public class ModeScreen extends AppCompatActivity{

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1_4);
        Button study = findViewById(R.id.studyButton);
        Button edit = findViewById(R.id.editButton);
        Button delete = findViewById(R.id.deleteButton);
        Button backButton = findViewById(R.id.backButton);

        // Get Folder Name From ExistingScreen.class
        Intent intent = getIntent();
        String folderName = intent.getStringExtra("foldername");
        String flashCardName = folderName;
        folderName = folderName.substring(0, 1).toUpperCase() + folderName.substring(1);
        TextView nameOfFolder1 = findViewById(R.id.nameOfFolder1);
        nameOfFolder1.setText(folderName);


        Context context = this;
        final String flashCardDir = context.getFilesDir().toString() + "/folders/" + flashCardName + "/" + flashCardName + ".json";

        study.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick (View view) {
                Intent goToFlashCardScreen = new Intent (ModeScreen.this, ShowFlashcards.class);
                goToFlashCardScreen.putExtra("foldername", flashCardDir);
                startActivity(goToFlashCardScreen);
            }
        });

        edit.setOnClickListener(view -> {

            Intent goToEditScreen = new Intent (ModeScreen.this, EditScreen.class);
            goToEditScreen.putExtra("foldername", flashCardDir);
            startActivity(goToEditScreen);
        });

        delete.setOnClickListener(view -> {

            String currentFolderName = intent.getStringExtra("foldername");

            String currentFolder = context.getFilesDir().toString() + "/folders/" + currentFolderName;

            File fileToDelete = new File(currentFolder);

            deleteDir(fileToDelete);

            Intent returnToExistingScreen = new Intent (ModeScreen.this, ExistingScreen.class);
            startActivity(returnToExistingScreen);
        });

        backButton.setOnClickListener(view -> {
            Intent goToWelcomeScreen = new Intent (ModeScreen.this, WelcomeScreen.class);
            startActivity(goToWelcomeScreen);

        });

    }

    public void deleteDir(File file){
        File[] contents = file.listFiles();
        if(contents != null) {
            for (File f: contents) {
                deleteDir(f);
            }
        }

        file.delete();
    }

}
