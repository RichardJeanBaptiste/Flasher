package com.example.flasher;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;


public class FolderObject extends AppCompatActivity {

    static String nameOfFolder;
    static String filepath;

    static final Map<String, String> map = new HashMap<>();

    public FolderObject(String nameOfFolder, String filepath ,Map<String, String> x){
        FolderObject.nameOfFolder = nameOfFolder;
        FolderObject.filepath = filepath;
        map.putAll(x);
    }


    public void createNewFolder(){

        try {

            String newFolderFilepath = filepath + "/" + nameOfFolder;

            File newFolder = new File(newFolderFilepath);

            if(newFolder.mkdirs()) {
              // create folder
            } else {
                Log.d("folder", "folder not created");
            }

        } catch (Exception e){
            Log.d("folder", String.valueOf(e));
        }
    }


    public void saveFlashCards() throws JSONException, IOException {

        JSONObject flashCardObj = new JSONObject();

        for (Map.Entry<String,String> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

           flashCardObj.put(key, value);
        }

        String flashCardFilePath = filepath + "/" + nameOfFolder + "/" + nameOfFolder + ".json";

        Writer output;
        File flashJson = new File(flashCardFilePath);
        output = new BufferedWriter(new FileWriter(flashJson));
        output.write(flashCardObj.toString());
        output.close();

        finish();

    }

    public void printFolder() {

        for (Map.Entry<String,String> entry : map.entrySet()) {
            String key = " key " + entry.getKey();
            String value = " value " + entry.getValue();

            Log.d("Folder Object", key);
            Log.d("Folder Object", value);
        }

    }

}


