package com.example.andrew.cscb07;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.andrew.cscb07.code.database.DatabaseInsertHelpers;
import com.example.andrew.cscb07.code.exceptions.InvalidIdException;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class PhotoActivity extends AppCompatActivity {
    private ImageButton homeBtn;
    private ImageButton selectPictureBtn;
    private ImageButton acceptBtn;
    public static final int REQUEST_CODE_GET_PICTURE = 12;
    private Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        homeBtn = (ImageButton) findViewById(R.id.homeButton);
        homeBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                homeBtn.setEnabled(false);
                homeListener(view);
            }
        });
        selectPictureBtn = (ImageButton) findViewById(R.id.selectPhotoButton);
        selectPictureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectPictureBtn.setEnabled(false);
                selectPictureBtnListener(view);
            }
        });
        acceptBtn = (ImageButton) findViewById(R.id.imageButton4);
        acceptBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                acceptBtn.setEnabled(false);
                acceptBtnListener(view);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_GET_PICTURE:
                if (resultCode == Activity.RESULT_OK) {
                    Uri selectedImage = data.getData();
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                        ImageView photoImageView = (ImageView) findViewById(R.id.photoImage);
                        photoImageView.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } else {
                    Intent intent = new Intent();
                    setResult(Activity.RESULT_CANCELED, intent);
                }
        }
        selectPictureBtn.setEnabled(true);
    }

    public void selectPictureBtnListener(View view){
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE_GET_PICTURE);
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
        homeBtn.setEnabled(true);
        startActivity(intent);
    }

    private byte[] bitMapToByte(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    public void acceptBtnListener(View view){
        byte[] image = bitMapToByte(bitmap);
        try {
            DatabaseInsertHelpers.insertUserImage(GlobalApplication.userid, image, this);
        } catch (InvalidIdException e) {
            e.printStackTrace();
        }
    }

}


