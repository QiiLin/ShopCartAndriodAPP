package com.example.andrew.cscb07;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.andrew.cscb07.code.com.users.Account;
import com.example.andrew.cscb07.code.com.users.AccountImpl;
import com.example.andrew.cscb07.code.com.users.Customer;
import com.example.andrew.cscb07.code.database.DatabaseInsertHelpers;
import com.example.andrew.cscb07.code.exceptions.InvalidAddressExcetpion;
import com.example.andrew.cscb07.code.exceptions.InvalidAgeException;
import com.example.andrew.cscb07.code.exceptions.InvalidIdException;
import com.example.andrew.cscb07.code.exceptions.InvalidUserNameException;

import java.io.ByteArrayOutputStream;

public class CreateAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        ImageButton backButton = (ImageButton) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateAccountActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        final ImageButton acceptButton = (ImageButton) findViewById(R.id.acceptButton);
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                acceptButton.setEnabled(false);
                EditText etname = (EditText) findViewById(R.id.NameText);
                EditText etage = (EditText) findViewById(R.id.AgeInteger);
                EditText etaddress = (EditText) findViewById(R.id.AddressText);
                EditText etpassword = (EditText) findViewById(R.id.PasswordText);
                EditText etrepassword = (EditText) findViewById(R.id.RePasswordText);

                String strname = etname.getText().toString();
                String strage = etage.getText().toString();

                String straddress = etaddress.getText().toString();
                String strpassword = etpassword.getText().toString();
                String strrepassword = etrepassword.getText().toString();


                EditText[] etArray = new EditText[]{etname, etage, etaddress, etpassword, etrepassword};
                String[] strArray = new String[]{strname, strage, straddress, strpassword, strrepassword};

                int counter = 0;
                while (counter < 4) {
                    //if string is empty set stringEmpty to true and re-enable the button
                    //also create the error message
                    if (TextUtils.isEmpty(strArray[counter])) {
                        acceptButton.setEnabled(true);
                        etArray[counter].setError("Empty field");
                        return;
                    }
                    counter++;
                }

                if (!strpassword.equals(strrepassword)) {
                    acceptButton.setEnabled(true);
                    etArray[counter].setError("Passwords do not match!");
                    return;
                }

                try {
                    Integer intage = Integer.parseInt(strage);
                    int userId = DatabaseInsertHelpers.insertNewUser(strname, intage, straddress, strpassword, CreateAccountActivity.this);
                    DatabaseInsertHelpers.insertUserRole(userId, GlobalApplication.roleid, CreateAccountActivity.this);
                    Bitmap userBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.noimage);
                    byte[] UserImage = bitMapToByte(userBitmap);
                    DatabaseInsertHelpers.insertItemImage(userId, UserImage, CreateAccountActivity.this);

                    Context context = getApplicationContext();
                    CharSequence text = "Your userId is: " + Integer.toString(userId);
                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();

                } catch (InvalidUserNameException e) {
                    acceptButton.setEnabled(true);
                    etname.setError("Should be in the format \"Firstname Lastname\" with first letter capitalized");
                    return;
                } catch (InvalidAddressExcetpion invalidAddressExcetpion) {
                    acceptButton.setEnabled(true);
                    etaddress.setError("Address length must be <= 100");
                    return;
                } catch (InvalidAgeException e) {
                    acceptButton.setEnabled(true);
                    etage.setError("Age must be >0");
                    return;
                } catch (InvalidIdException e) {
                    e.printStackTrace();
                }

                if(GlobalApplication.roleid == 3 && !GlobalApplication.fromEmployee) {
                    Intent intent = new Intent(CreateAccountActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else if(GlobalApplication.roleid == 2){
                    Intent intent = new Intent(CreateAccountActivity.this, EmployeeActivity.class);
                    startActivity(intent);
                    finish();
                } else if (GlobalApplication.roleid == 3 && GlobalApplication.fromEmployee) {
                    Intent intent = new Intent(CreateAccountActivity.this, EmployeeActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        ImageButton back = (ImageButton) findViewById(R.id.backButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(GlobalApplication.roleid == 3 && !GlobalApplication.fromEmployee) {
                    Intent intent = new Intent(CreateAccountActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else if(GlobalApplication.roleid == 2){
                    Intent intent = new Intent(CreateAccountActivity.this, EmployeeActivity.class);
                    startActivity(intent);
                    finish();
                } else if (GlobalApplication.roleid == 3 && GlobalApplication.fromEmployee) {
                    Intent intent = new Intent(CreateAccountActivity.this, EmployeeActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private byte[] bitMapToByte(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }
}



