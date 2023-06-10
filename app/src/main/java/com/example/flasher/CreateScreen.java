package com.example.flasher;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

public class CreateScreen extends AppCompatActivity {

    private EditText enterNameOfFolder;
    private EditText enterTerm;
    private EditText enterDef;

    private String folderName;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1_2);
        enterNameOfFolder = findViewById(R.id.enterNameOfFolder);
        enterTerm = findViewById(R.id.enterTerm);
        enterDef = findViewById(R.id.enterDef);
        Button addMoreFlash = findViewById(R.id.math);
        Button finishedAddDef = findViewById(R.id.science);

        Map<String, String> tempMap = new HashMap<>();

        // Folder Reference
        Context context = this;
        folderName = context.getFilesDir().toString() + "/folders";

        addMoreFlash.setOnClickListener(view -> {
            String term = enterTerm.getText().toString();
            String definiton = enterDef.getText().toString();

            tempMap.put(term, definiton);

            enterTerm.setText("");
            enterDef.setText("");

        });

        finishedAddDef.setOnClickListener(view -> {
            String fName = enterNameOfFolder.getText().toString();

            try {

                FolderObject folderObject = new FolderObject(fName, folderName, tempMap);
                folderObject.createNewFolder();
                folderObject.saveFlashCards();

            } catch (Exception e){
                Log.d("flashcards", "Json Error");
            }

            Intent intent = new Intent(CreateScreen.this, ExistingScreen.class);
            intent.putExtra("foldername", folderName);

            startActivity(intent);
        });


    }
}
