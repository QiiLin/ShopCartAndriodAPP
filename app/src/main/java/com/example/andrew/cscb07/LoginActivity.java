package com.example.andrew.cscb07;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.andrew.cscb07.code.com.users.Account;
import com.example.andrew.cscb07.code.com.users.User;
import com.example.andrew.cscb07.code.database.DatabaseInsertHelpers;
import com.example.andrew.cscb07.code.database.DatabaseSelectHelpers;
import com.example.andrew.cscb07.code.exceptions.InvalidIdException;
import com.example.andrew.cscb07.code.exceptions.InvalidItemException;

import org.w3c.dom.Text;

import java.util.List;

public class LoginActivity extends AppCompatActivity {
    TextView invalidPassword;
    ImageButton login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = (ImageButton) findViewById(R.id.LoginButton);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login.setEnabled(false);
                EditText etuser = (EditText) findViewById(R.id.UsernameText);
                String username = etuser.getText().toString();
                EditText etpassword = (EditText) findViewById(R.id.PasswordText);
                String password = etpassword.getText().toString();
                TextView invalidPassword = (TextView) findViewById(R.id.InvalidPasswordText);
                if (TextUtils.isEmpty(username)) {
                    etuser.setError("Empty field");
                    login.setEnabled(true);
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    etpassword.setError("Empty field");
                    login.setEnabled(true);
                    return;
                }

                int inputedId = Integer.parseInt(username);
                int roleOfId;
                try {
                    roleOfId = DatabaseSelectHelpers.getUserRoleId(inputedId,LoginActivity.this);
                    User user = DatabaseSelectHelpers.getUserDetails(inputedId,LoginActivity.this);
                    if (user.authenticate(password,LoginActivity.this)) {
                        GlobalApplication.userid = inputedId;
                        if(roleOfId == 1){
                            GlobalApplication.roleid = roleOfId;
                            Intent intent = new Intent(LoginActivity.this,AdminActivity.class);
                            startActivity(intent);
                            finish();
                        } else if (roleOfId == 2){
                            GlobalApplication.roleid = roleOfId;
                            Intent intent = new Intent(LoginActivity.this,EmployeeActivity.class);
                            startActivity(intent);
                            finish();
                        } else if(roleOfId == 3){
                            GlobalApplication.roleid = roleOfId;
                            /*
                            try {
                                List<Account> accounts = DatabaseSelectHelpers.getUserAccounts(inputedId,LoginActivity.this);
                                if (accounts.size() == 1){
                                    List<Account> activeAccounts = DatabaseSelectHelpers.getUserActiveAccounts(inputedId,LoginActivity.this);
                                    if (accounts.size() == 1){
                                        GlobalApplication.account = activeAccounts.get(0);
                                    } else {
                                        GlobalApplication.account = accounts.get(0);
                                    }
                                } else {
                                    DatabaseInsertHelpers.insertAccount(inputedId,false, LoginActivity.this);
                                    accounts = DatabaseSelectHelpers.getUserAccounts(inputedId,LoginActivity.this);
                                    GlobalApplication.account = accounts.get(0);
                                }
                            } catch (InvalidItemException e) {
                                e.printStackTrace();
                            }*/
                            Intent intent = new Intent(LoginActivity.this,StoreActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        etpassword.setError("Incorrect Password!");
                        login.setEnabled(true);
                    }
                } catch (InvalidIdException e1) {
                    etuser.setError("Invalid userId!");
                    login.setEnabled(true);
                }



            }


        });

        ImageButton continueButton = (ImageButton) findViewById(R.id.newAccountButton);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalApplication.roleid = 3;
                Intent intent = new Intent(LoginActivity.this,CreateAccountActivity.class);
                startActivity(intent);
            }
        });
    }
}







