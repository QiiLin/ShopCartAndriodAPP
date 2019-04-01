package com.example.andrew.cscb07;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.andrew.cscb07.code.com.users.User;
import com.example.andrew.cscb07.code.database.DatabaseSelectHelpers;
import com.example.andrew.cscb07.code.database.DatabaseUpdateHelpers;
import com.example.andrew.cscb07.code.exceptions.InvalidAddressException;
import com.example.andrew.cscb07.code.exceptions.InvalidAgeException;
import com.example.andrew.cscb07.code.exceptions.InvalidIdException;
import com.example.andrew.cscb07.code.exceptions.InvalidUserNameException;


public class UserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        ImageButton home = (ImageButton) findViewById(R.id.homeButton);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeListener(view);
            }
        });

        ImageButton changePicture = (ImageButton) findViewById(R.id.changePhotoButton);
        changePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePictureListener(view);
            }
        });

        ImageView userPhoto = (ImageView) findViewById(R.id.photoImage);
        final int userId = GlobalApplication.userid;
        ImageView userImage = findViewById(R.id.photoImage);
        Bitmap pic;
        try {
            pic = DatabaseSelectHelpers.getUserImage(userId, this);
            userImage.setImageBitmap(pic);
        } catch (InvalidIdException e) {

        }
        TextView userName = findViewById(R.id.customerNameText);
        TextView currentName = findViewById(R.id.currentNameText);
        TextView currentAge = findViewById(R.id.currentAgeText);
        TextView currentRole = findViewById(R.id.currentRoleText);
        try{
            User user = DatabaseSelectHelpers.getUserDetails(userId, this);
            userName.setText(user.getName().split(" ")[0]);
            currentName.setText(user.getName());
            currentAge.setText(String.valueOf(user.getAge()));
            currentRole.setText(DatabaseSelectHelpers.getRoleName(user.getRoleId(), UserActivity.this));
        } catch (InvalidIdException e){

        }

        ImageButton accept = findViewById(R.id.acceptButton);
        accept.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                EditText changeName = findViewById(R.id.ChangeNameText);
                EditText changeAge = findViewById(R.id.ChangeAgeText);
                EditText changePassword = findViewById(R.id.ChangePasswordText);
                EditText changeAddress = findViewById(R.id.ChangeAddressText);
                String newName = changeName.getText().toString();
                String newAge = changeAge.getText().toString();
                String newAddress = changeAddress.getText().toString();
                String newPassword = changePassword.getText().toString();

                try{
                    if (!newName.equals("")){
                        DatabaseUpdateHelpers.updateUserName(newName, userId, UserActivity.this);
                    }
                    if (!newAge.equals("")){
                        DatabaseUpdateHelpers.updateUserAge(Integer.parseInt(newAge), userId, UserActivity.this);
                    }
                    if (!newAddress.equals("")){
                        DatabaseUpdateHelpers.updateUserAddress(newAddress, userId, UserActivity.this);
                    }
                    if (!newPassword.equals("")){
                        DatabaseUpdateHelpers.updateUserPassword(newPassword, userId, UserActivity.this);
                    }
                } catch (InvalidIdException| InvalidUserNameException | InvalidAgeException |InvalidAddressException e){
                }
            }
        });
    }

    public void changePictureListener(View view) {
        Intent intent = new Intent(this, PhotoActivity.class);
        startActivity(intent);
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

    public void Logout(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

}
