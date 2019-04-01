package com.example.andrew.cscb07;

import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.andrew.cscb07.code.database.DatabaseSelectHelpers;
import com.example.andrew.cscb07.code.store.SalesLog;

public class BooksActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books);

        Spinner dropdown = (Spinner)findViewById(R.id.spinner);
        ConstraintLayout itemConstraint = (ConstraintLayout) findViewById(R.id.breakdownScrollConstraint);
        ConstraintLayout inventoryConstraint = (ConstraintLayout) findViewById(R.id.inventoryScrollConstraint);


        String[] items = new String[]{"1", "2", "three"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);

        ImageButton home = (ImageButton) findViewById(R.id.homeButton);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeListener(view);
            }
        });





    }

    public void homeListener(View view) {
        Intent intent = new Intent(this, StoreActivity.class);
        startActivity(intent);
    }


}
