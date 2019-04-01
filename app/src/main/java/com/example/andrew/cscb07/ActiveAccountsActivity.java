package com.example.andrew.cscb07;

import android.content.Intent;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.andrew.cscb07.code.com.users.Account;
import com.example.andrew.cscb07.code.database.DatabaseSelectHelpers;
import com.example.andrew.cscb07.code.exceptions.InvalidIdException;
import com.example.andrew.cscb07.code.com.users.User;
import com.example.andrew.cscb07.code.exceptions.InvalidItemException;

import java.util.ArrayList;
import java.util.List;

public class ActiveAccountsActivity extends AppCompatActivity {
    TextView activeIds;
    TextView inactiveIds;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_accounts);

        Spinner userIds = (Spinner)findViewById(R.id.userSpinner);
        List<Integer> id = new ArrayList<>();

        List<User> users;
        try {
            users = DatabaseSelectHelpers.getUsersDetails(this);
            for (User user: users){
                id.add(user.getId());
            }
        } catch (InvalidIdException e){

        }


        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_dropdown_item, id);
        userIds.setAdapter(adapter);
        userIds.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        ConstraintLayout active = (ConstraintLayout) findViewById(R.id.activeConstraint);
        ConstraintLayout inactive = (ConstraintLayout) findViewById(R.id.inactiveConstraint);
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                {

                    String selectedItem = parent.getItemAtPosition(position).toString(); //this is your selected item
                    List<Account> activeAccounts = new ArrayList<>();
                    List<Account> inactiveAccounts = new ArrayList<>();
                    try {
                        activeAccounts = DatabaseSelectHelpers.getUserActiveAccounts(Integer.parseInt(selectedItem), ActiveAccountsActivity.this);
                        inactiveAccounts = DatabaseSelectHelpers.getUserInactiveAccounts(Integer.parseInt(selectedItem), ActiveAccountsActivity.this);
                    } catch (InvalidIdException | InvalidItemException e){

                    }

                    for (Account account: activeAccounts){
                        activeIds = new TextView(ActiveAccountsActivity.this);
                        activeIds.setText(account.getUserId());
                        activeIds.setTextSize(15f);
                        activeIds.setTextColor(Color.parseColor("#fff"));
                        active.addView(activeIds);
                    }
                    for (Account account: inactiveAccounts){
                        inactiveIds = new TextView(ActiveAccountsActivity.this);
                        inactiveIds.setText(account.getUserId());
                        inactiveIds.setTextSize(15f);
                        inactiveIds.setTextColor(Color.parseColor("#fff"));
                        inactive.addView(inactiveIds);
                    }
                }
            }
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

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
