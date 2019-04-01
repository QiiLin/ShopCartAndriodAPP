package com.example.andrew.cscb07;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.andrew.cscb07.code.database.DatabaseDeserializeHelper;
import com.example.andrew.cscb07.code.database.DatabaseSerializeHelper;

public class AdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        ImageButton saveData = (ImageButton) findViewById(R.id.saveDatabase);
        saveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean result = DatabaseSerializeHelper.serializeHelper(AdminActivity.this);
                if (result){
                    Toast.makeText(getApplicationContext(), "Complete serli request", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    Toast.makeText(getApplicationContext(), "Failed serli request", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });



        ImageButton restoreData = findViewById(R.id.restoredata);
        restoreData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean result = DatabaseDeserializeHelper.deserializeHelper(AdminActivity.this);
                if (result){
                    Toast.makeText(getApplicationContext(), "Complete deserli request", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    Toast.makeText(getApplicationContext(), "Failed deserli request", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });

        ImageButton home = (ImageButton) findViewById(R.id.homeButton);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeListener(view);
            }
        });

        ImageButton logout = (ImageButton) findViewById(R.id.logoutButton);
        logout.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                logoutListener(view);
            }
        });

        ImageButton viewItems = (ImageButton) findViewById(R.id.restockItemsButton);
        viewItems.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                viewInventoryListener(view);
            }
        });

        ImageButton viewBooks = (ImageButton) findViewById(R.id.viewBooksButton);
        viewBooks.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                viewBooksListener(view);
            }
        });

        ImageButton promoteEmployee = (ImageButton) findViewById(R.id.promoteEmployeeButton);
        promoteEmployee.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                promoteEmployeeListener(view);
            }
        });

        ImageButton addItem = (ImageButton) findViewById(R.id.addItemButton);
        addItem.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                addItemListener(view);
            }
        });

        ImageButton activeAccounts = (ImageButton) findViewById(R.id.activeAccountsButton);
        activeAccounts.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                activeAccountsListener(view);
            }
        });
    }

    public void homeListener(View view) {
        Intent intent = new Intent(this, StoreActivity.class);
        startActivity(intent);
    }


    public void logoutListener(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void viewBooksListener(View view) {
        Intent intent = new Intent(this, BooksActivity.class);
        startActivity(intent);
    }

    public void promoteEmployeeListener(View view) {
        Intent intent = new Intent(this, PromoteActivity.class);
        startActivity(intent);
    }

    public void activeAccountsListener(View view) {
        Intent intent = new Intent(this, ActiveAccountsActivity.class);
        startActivity(intent);
    }

    public void addItemListener(View view) {
        Intent intent = new Intent(this, AddItemActivity.class);
        startActivity(intent);
    }

    public void viewInventoryListener(View view) {
        Intent intent = new Intent(this, InventoryActivity.class);
        startActivity(intent);
    }
}
