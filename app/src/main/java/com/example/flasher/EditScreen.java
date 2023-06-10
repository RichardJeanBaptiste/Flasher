package com.example.flasher;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

public class EditScreen extends AppCompatActivity {

    Map<String, String> tempMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_screen);

        LinearLayout x =  findViewById(R.id.EditLinearLayout);

        Intent intent = getIntent();
        String folderName = intent.getStringExtra("foldername");

        Button backBtn = findViewById(R.id.EditScreenBackButton);

        backBtn.setOnClickListener(v -> {
            Intent goBackToWelcomeScreen = new Intent(EditScreen.this, WelcomeScreen.class);
            startActivity(goBackToWelcomeScreen);
        });


        File flashCardObject = new File(folderName);
        FileReader fileReader;

        // Read through flashCardObject
        try {

            fileReader = new FileReader(flashCardObject);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            StringBuilder stringBuilder = new StringBuilder();
            String line = bufferedReader.readLine();
            while (line != null){
                stringBuilder.append(line).append("\n");
                line = bufferedReader.readLine();
            }
            bufferedReader.close();

            /*
             * Turn JSON object to String
             * Then remove semicolons from string
             * */
            String response = stringBuilder.toString();
            response = response.substring(1, response.length() - 2);

            String[] parts = response.split(",");


            //iterate the parts and add them to arraylist terms/definitions
            for(String part : parts){

                //split the data by ":" to get id and name
                String[] data = part.split(":");

                String strTerm = data[0].trim().replace("\"", "");
                String strDef = data[1].trim().replace("\"", "");

                tempMap.put(strTerm, strDef);

                addFlashCard(x, strTerm, strDef, folderName);

                Log.d("EditScreen", strTerm + "\n" + strDef + "\n*********************************");
            }


        } catch (FileNotFoundException e) {
            String fileErrorMsg = "File Not Found \n" + e;
            Log.d("ShowFlashCards", fileErrorMsg);
        } catch (IOException e) {
            String errormsg= "IOExcpetion \n" + e;
            Log.d("ShowFlashCards", errormsg);
            throw new RuntimeException(e);
        }


    }

    public void addFlashCard(LinearLayout mainLayout, String term, String def, String flashCardFilePath){

        String key = term;
        String value = def;
        Button editBtn = new Button(this);
        Button deleteBtn = new Button(this);

        LinearLayout flashCardLayout = new LinearLayout(this);
        flashCardLayout.setOrientation(LinearLayout.HORIZONTAL);
        flashCardLayout.setPadding(5, 0, 0,35);


        EditText flashCardTerm = new EditText(this);
        flashCardTerm.setText(term);
        flashCardTerm.setPadding(0,0,25, 0);
        flashCardTerm.setWidth(250);
        flashCardTerm.setHeight(150);
        flashCardLayout.addView(flashCardTerm);

        EditText flashCardDef = new EditText(this);
        flashCardDef.setText(def);
        flashCardDef.setPadding(0,0,25, 0);
        flashCardDef.setWidth(250);
        flashCardDef.setHeight(150);
        flashCardLayout.addView(flashCardDef);


        editBtn.setText("Save");
        editBtn.setOnClickListener(v -> {

            try {
                saveChanges(mainLayout, flashCardTerm, flashCardDef, editBtn , deleteBtn ,key, flashCardTerm.getText().toString(), flashCardDef.getText().toString(), flashCardFilePath);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });
        flashCardLayout.addView(editBtn);


        deleteBtn.setText("Delete");
        deleteBtn.setOnClickListener(v -> {
            try {
                deleteTerm(key, flashCardFilePath);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            mainLayout.removeView(flashCardLayout);
        });
        flashCardLayout.addView(deleteBtn);


        mainLayout.addView(flashCardLayout);
    }

    public void saveChanges(LinearLayout mainLayout, EditText flashCardTerm, EditText flashCardDef, Button editBtn, Button deleteBtn ,String term, String newTerm, String newDef, String flashCardFilePath) throws JSONException, IOException {

        tempMap.remove(term);
        tempMap.put(newTerm, newDef);

        JSONObject flashCardObj = new JSONObject();

        for (Map.Entry<String,String> entry : tempMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            flashCardObj.put(key, value);
        }

        Writer output;
        File flashJson = new File(flashCardFilePath);
        output = new BufferedWriter(new FileWriter(flashJson));
        output.write(flashCardObj.toString());
        output.close();

        addFlashCard(mainLayout, newTerm, newDef, flashCardFilePath);
        deleteFlashCardView(flashCardTerm, flashCardDef, editBtn, deleteBtn);
    }

    public void deleteFlashCardView(EditText flashCardTerm, EditText flashCardDef, Button saveButton, Button deleteButton){
        ((ViewGroup) flashCardTerm.getParent()).removeView(flashCardTerm);
        ((ViewGroup) flashCardDef.getParent()).removeView(flashCardDef);
        ((ViewGroup) saveButton.getParent()).removeView(saveButton);
        ((ViewGroup) deleteButton.getParent()).removeView(deleteButton);
    }


    public void deleteTerm(String flashCardKey, String flashCardFilePath) throws JSONException, IOException {
        tempMap.remove(flashCardKey);

        if(tempMap.size() > 0){
            JSONObject flashCardObj = new JSONObject();

            for (Map.Entry<String,String> entry : tempMap.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();

                flashCardObj.put(key, value);
            }

            Writer output;
            File flashJson = new File(flashCardFilePath);
            output = new BufferedWriter(new FileWriter(flashJson));
            output.write(flashCardObj.toString());
            output.close();
        } else {
            int index = flashCardFilePath.lastIndexOf('/');
            String currentFolder = flashCardFilePath.substring(0, index);
            File fileToDelete = new File(currentFolder);
            deleteDir(fileToDelete);
        }
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