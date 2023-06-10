package com.example.flasher;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class ShowFlashcards extends AppCompatActivity{
    private TextView term_view;
    private TextView def_view;

    private int counter = 0;

    Boolean show_answer = false;

    // Terms And Definitions
    private final ArrayList<String> terms = new ArrayList<>();
    private final ArrayList<String> definitions = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_flashcards);

        term_view = findViewById(R.id.term_view);
        def_view = findViewById(R.id.def_view);
        Button next_view = findViewById(R.id.next_view);
        Button back_view = findViewById(R.id.back_view);
        Button home_view = findViewById(R.id.home_view);
        Button show_answer_button = findViewById(R.id.show_answer_button);

        // Get Flash Card Location String From ModeScreen
        Intent intent = getIntent();
        String tempString = intent.getStringExtra("foldername");


        File flashCardObject = new File(tempString);
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
            String responce = stringBuilder.toString();
            responce = responce.substring(1, responce.length() - 2);

            String[] parts = responce.split(",");


            //iterate the parts and add them to arraylist terms/definitions
            for(String part : parts){

                //split the data by ":" to get id and name
                String[] data = part.split(":");

                String strTerm = data[0].trim().replace("\"", "");
                String strDef = data[1].trim().replace("\"", "");

                terms.add(strTerm);
                definitions.add(strDef);

            }

            // Set initial flash card
            term_view.setText(terms.get(counter).toString());
            def_view.setText("");

        } catch (FileNotFoundException e) {
            String fileErrorMsg = "File Not Found \n" + e;
            Log.d("ShowFlashCards", fileErrorMsg);
        } catch (IOException e) {
            String errormsg= "IOExcpetion \n" + e;
            Log.d("ShowFlashCards", errormsg);
            throw new RuntimeException(e);
        }

        show_answer_button.setOnClickListener(view -> {

            if(!show_answer){
                show_answer = true;
                def_view.setText(definitions.get(counter).toString());
            } else {
                show_answer = false;
                def_view.setText("");
            }
        });
        next_view.setOnClickListener(view -> {
            counter += 1;
            show_answer = false;

            if(counter >= terms.size()){
                counter = 0;
            }

            term_view.setText(terms.get(counter).toString());
            def_view.setText("");
        });
        back_view.setOnClickListener(view -> {
            counter -= 1;
            show_answer = false;

            if(counter < 0){
                counter = terms.size() - 1;
            }

            term_view.setText(terms.get(counter).toString());
            def_view.setText("");
        });
         home_view.setOnClickListener(view -> {
              Intent intent1 = new Intent(ShowFlashcards.this, ExistingScreen.class);
              startActivity (intent1);
         });
       

    }
}

