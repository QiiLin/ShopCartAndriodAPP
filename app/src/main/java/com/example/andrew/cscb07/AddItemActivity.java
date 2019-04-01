package com.example.andrew.cscb07;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.andrew.cscb07.code.database.DatabaseInsertHelpers;
import com.example.andrew.cscb07.code.exceptions.InvalidIdException;
import com.example.andrew.cscb07.code.exceptions.InvalidItemException;
import com.example.andrew.cscb07.code.exceptions.InvalidItemNameException;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;

public class AddItemActivity extends AppCompatActivity {
    public static final int REQUEST_CODE_GET_PICTURE = 001;
    private ImageButton homeBtn;
    private ImageButton selectPictureBtn;
    private ImageButton acceptBtn;
    private EditText itemNameEditText;
    private EditText itemPriceEditText;
    private ImageView itemImageView;
    private Bitmap bitmap = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        homeBtn = (ImageButton) findViewById(R.id.homeButton);
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeBtn.setEnabled(false);
                homeListener(view);
            }

        });
        selectPictureBtn = (ImageButton) findViewById(R.id.imageButton2);
        selectPictureBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                selectPictureBtn.setEnabled(false);
                selectPictureBtnListener(view);
            }
        });

        acceptBtn = (ImageButton) findViewById(R.id.acceptButton);
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
                        itemImageView = (ImageView) findViewById(R.id.imageView);
                        itemImageView.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
        }
        selectPictureBtn.setEnabled(true);
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

    public void selectPictureBtnListener(View view){
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE_GET_PICTURE);
    }

    public void acceptBtnListener(View view){
        itemNameEditText = (EditText) findViewById(R.id.itemNameText);
        itemPriceEditText = (EditText) findViewById(R.id.itemPriceText);
        String itemName = itemNameEditText.getText().toString();
        BigDecimal itemPrice = new BigDecimal(itemPriceEditText.getText().toString());
        try {
            int itemId = DatabaseInsertHelpers.insertItem(itemName, itemPrice, this);
            byte[] image = bitMapToByte(bitmap);
            DatabaseInsertHelpers.insertItemImage(itemId, image, this);
        } catch (InvalidItemException | InvalidIdException e){
            itemPriceEditText.setError("You have entered an invalid item price, it should be greater than 0");
            acceptBtn.setEnabled(true);
        } catch (InvalidItemNameException e){
            itemNameEditText.setError("You have entered an item name that is already within the store, please set a new item or go back to previous page");
            acceptBtn.setEnabled(true);
        }
        Intent intent = new Intent(this, AdminActivity.class);
        startActivity(intent);
        finish();
    }

    private byte[] bitMapToByte(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }
}
