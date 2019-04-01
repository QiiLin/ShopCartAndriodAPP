package com.example.andrew.cscb07;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.andrew.cscb07.code.com.users.Account;
import com.example.andrew.cscb07.code.com.users.Employee;
import com.example.andrew.cscb07.code.database.DatabaseSelectHelpers;
import com.example.andrew.cscb07.code.database.DatabaseUpdateHelpers;
import com.example.andrew.cscb07.code.exceptions.InvalidIdException;
import com.example.andrew.cscb07.code.exceptions.InvalidItemException;

import java.util.List;

public class EmployeeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);


        ImageButton approveButton = findViewById(R.id.ApproveAllAccount);
        approveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (approveListener(view)){
                    Toast.makeText(getApplicationContext(), "Approve all requested accounts!", Toast.LENGTH_LONG)
                            .show();
                } else {
                    Toast.makeText(getApplicationContext(), "Failed to approve all requested accounts!", Toast.LENGTH_LONG)
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
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutListener(view);
            }
        });

        ImageButton createCustomer = (ImageButton) findViewById(R.id.createCustomerButton);
        createCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createCustomerListener(view);
            }
        });

        ImageButton createEmployee = (ImageButton) findViewById(R.id.createEmployeeButton);
        createEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createEmployeeListener(view);
            }
        });

        ImageButton restockItems = (ImageButton) findViewById(R.id.restockItemsButton);
        createEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restockItemsListener(view);
            }
        });
    }

    public boolean approveListener(View view){
        List<Account> userAccounts;
        boolean result = false;
        try {
            userAccounts = DatabaseSelectHelpers.getAllaccounts(EmployeeActivity.this);
            System.out.println("Here is a list of your approve active account id, which one you want to restore?");
            for (Account currentAccount : userAccounts) {
                if (currentAccount.getActive()) {
                    if (!currentAccount.getApprove()) {
                        int currentaccountId = currentAccount.getId();
                        result = result && DatabaseUpdateHelpers.updateAccountApprove(currentaccountId, true, EmployeeActivity.this);
                    }}
            }
            return result;
        } catch (InvalidItemException | InvalidIdException e) {
            // TODO Auto-generated catch block
            return false;
        }
    }

    public void homeListener(View view) {
        Intent intent = new Intent(this, StoreActivity.class);
        if (GlobalApplication.roleid == 1){
            intent = new Intent(this, StoreActivity.class);
        } else if (GlobalApplication.roleid == 2){
            intent = new Intent(this, EmployeeActivity.class);
        } else if (GlobalApplication.roleid == 3){
            intent = new Intent(this, AdminActivity.class);
        }
        startActivity(intent);
    }

    public void logoutListener(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void createCustomerListener(View view) {
        GlobalApplication.roleid = 3;
        GlobalApplication.fromEmployee = true;
        Intent intent = new Intent(this, CreateAccountActivity.class);
        startActivity(intent);
        finish();
    }

    public void createEmployeeListener(View view) {
        GlobalApplication.roleid = 2;
        GlobalApplication.fromEmployee = true;
        Intent intent = new Intent(this, CreateAccountActivity.class);
        startActivity(intent);
        finish();
    }

    public void restockItemsListener(View view) {
        Intent intent = new Intent(this, InventoryActivity.class);
        startActivity(intent);
        finish();
    }
}
