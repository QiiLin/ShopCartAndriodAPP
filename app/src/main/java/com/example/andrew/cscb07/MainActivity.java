package com.example.andrew.cscb07;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.andrew.cscb07.code.database.DatabaseInsertHelpers;
import com.example.andrew.cscb07.code.database.DatabaseSelectHelpers;
import com.example.andrew.cscb07.code.exceptions.InvalidAddressExcetpion;
import com.example.andrew.cscb07.code.exceptions.InvalidAgeException;
import com.example.andrew.cscb07.code.exceptions.InvalidIdException;
import com.example.andrew.cscb07.code.exceptions.InvalidItemException;
import com.example.andrew.cscb07.code.exceptions.InvalidItemNameException;
import com.example.andrew.cscb07.code.exceptions.InvalidUserNameException;
import com.example.andrew.cscb07.code.inventory.Item;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button continueButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println(intializedDatabase());
        if(intializedDatabase()){
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }



        continueButton = (Button) findViewById(R.id.continueButton);
        continueButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){
                //disable the button after clicked
                continueButton.setEnabled(false);

                try {
                    DatabaseInsertHelpers.insertRole("ADMIN", MainActivity.this);
                    DatabaseInsertHelpers.insertRole("EMPLOYEE", MainActivity.this);
                    DatabaseInsertHelpers.insertRole("CUSTOMER", MainActivity.this);
                    int fishingRodId = DatabaseInsertHelpers.insertItem("FISHING_ROD", BigDecimal.valueOf(100.99),MainActivity.this);
                    Bitmap fishingRodBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.fishingrod);
                    byte[] fishingRodImage = bitMapToByte(fishingRodBitmap);
                    DatabaseInsertHelpers.insertItemImage(fishingRodId, fishingRodImage, MainActivity.this);
                    int hockeyStickId = DatabaseInsertHelpers.insertItem("HOCKEY_STICK", BigDecimal.valueOf(75.99),MainActivity.this);
                    Bitmap hockeyStickBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.hockeystick);
                    byte[] hockeyStickImage = bitMapToByte(hockeyStickBitmap);
                    DatabaseInsertHelpers.insertItemImage(hockeyStickId, hockeyStickImage, MainActivity.this);
                    int skatesId = DatabaseInsertHelpers.insertItem("SKATES", BigDecimal.valueOf(125.99),MainActivity.this);
                    Bitmap skatesBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.skates);
                    byte[] skatesImage = bitMapToByte(skatesBitmap);
                    DatabaseInsertHelpers.insertItemImage(skatesId, skatesImage, MainActivity.this);
                    int runningShoesId = DatabaseInsertHelpers.insertItem("RUNNING_SHOES", BigDecimal.valueOf(55.99),MainActivity.this);
                    Bitmap runningShoesBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.runningshoes);
                    byte[] runningShoesImage = bitMapToByte(runningShoesBitmap);
                    DatabaseInsertHelpers.insertItemImage(runningShoesId, runningShoesImage, MainActivity.this);
                    int proteinBarId = DatabaseInsertHelpers.insertItem("PROTEIN_BAR", BigDecimal.valueOf(0.99),MainActivity.this);
                    Bitmap proteinBarBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.proteinbar);
                    byte[] proteinBarImage = bitMapToByte(proteinBarBitmap);
                    DatabaseInsertHelpers.insertItemImage(proteinBarId, proteinBarImage, MainActivity.this);
                    DatabaseInsertHelpers.insertInventory(1,100,MainActivity.this);
                    DatabaseInsertHelpers.insertInventory(2,100,MainActivity.this);
                    DatabaseInsertHelpers.insertInventory(3,100,MainActivity.this);
                    DatabaseInsertHelpers.insertInventory(4,100,MainActivity.this);
                    DatabaseInsertHelpers.insertInventory(5,100,MainActivity.this);

                    //DatabaseInsertHelpers.insertItemImage(1,,this);
                } catch (InvalidIdException e) {
                    e.printStackTrace();
                } catch (InvalidItemException e) {
                    e.printStackTrace();
                } catch (InvalidItemNameException e) {
                    e.printStackTrace();
                }

                //get the ids of all EditTexts

                EditText adminName = (EditText) findViewById(R.id.adminNameText);
                EditText adminAddress = (EditText) findViewById(R.id.adminAddressText);
                EditText adminAge = (EditText) findViewById(R.id.adminAgeText);
                EditText adminPassword = (EditText) findViewById(R.id.adminPasswordText);
                EditText employeeName = (EditText) findViewById(R.id.employeeNameText);
                EditText employeeAddress = (EditText) findViewById(R.id.employeeAddressText);
                EditText employeeAge = (EditText) findViewById(R.id.employeeAgeText);
                EditText employeePassword = (EditText) findViewById(R.id.employeePasswordText);
                //convert all the edittexts into strings
                String stradminName = adminName.getText().toString();
                String stradminAddress = adminAddress.getText().toString();
                String stradminAge = adminAge.getText().toString();

                String stradminPassword = adminPassword.getText().toString();
                String stremployeeName = employeeName.getText().toString();
                String stremployeeAddress = employeeAddress.getText().toString();
                String stremployeeAge = employeeAge.getText().toString();

                String stremployeePassword = employeePassword.getText().toString();
                //set stringEmpty to false
                boolean stringEmpty = false;

                //create arrays to loop through
                EditText[] etArray = new EditText[]{adminName,adminAge,adminAddress,adminPassword,employeeName,employeeAddress,employeeAge,employeePassword};
                String[] strArray = new String[]{stradminName,stradminAge,stradminAddress,stradminPassword,stremployeeName,stremployeeAddress,stremployeeAge,stremployeePassword};

                //countered while loop to check if the given EditText is non-empty
                int counter = 0;
                while(counter < 8) {
                    //if string is empty set stringEmpty to true and re-enable the button
                    //also create the error message
                    if (TextUtils.isEmpty(strArray[counter])) {
                        stringEmpty = true;
                        continueButton.setEnabled(true);
                        etArray[counter].setError("Empty field");
                        return;
                    }
                    counter++;
                }



                // if all strings are non-empty move to LoginActivity
                if(!stringEmpty) {
                    try {
                        int intadminAge = Integer.parseInt(stradminAge);
                        DatabaseInsertHelpers.insertNewUser(stradminName,intadminAge,stradminAddress,stradminPassword,MainActivity.this);
                        DatabaseInsertHelpers.insertUserRole((int) 1,(int) 1,MainActivity.this);

                    } catch (InvalidUserNameException e) {
                        continueButton.setEnabled(true);
                        adminName.setError("Should be in the format \"Firstname Lastname\" with first letter capitalized");
                        return;
                    } catch (InvalidAddressExcetpion invalidAddressExcetpion) {
                        adminAddress.setError("Address length must be <= 100");
                    } catch (InvalidAgeException e) {
                        continueButton.setEnabled(true);
                        adminAge.setError("Must be >0");
                        return;
                    } catch (InvalidIdException e) {
                        e.printStackTrace();
                    }


                    try {
                        int intempolyeeAge = Integer.parseInt(stremployeeAge);
                        DatabaseInsertHelpers.insertNewUser(stremployeeName,intempolyeeAge,stremployeeAddress,stremployeePassword,MainActivity.this);
                        DatabaseInsertHelpers.insertUserRole((int) 2,(int) 2,MainActivity.this);
                    } catch (InvalidUserNameException e) {
                        continueButton.setEnabled(true);
                        employeeName.setError("Should be in the format \"Firstname Lastname\" with first letter capitalized");
                        return;
                    } catch (InvalidAddressExcetpion invalidAddressExcetpion) {
                        employeeAddress.setError("Address length must be <= 100");
                    } catch (InvalidAgeException e) {
                        continueButton.setEnabled(true);
                        employeeAge.setError("Must be >0");
                        return;
                    } catch (InvalidIdException e) {
                        e.printStackTrace();
                    }


                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
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

public boolean intializedDatabase(){
    List<Integer> roleidList = DatabaseSelectHelpers.getRoleIds(this);
    try {
        List<Item> itemsList = DatabaseSelectHelpers.getAllItems(this);
        if (itemsList.size() < 5){
            return false;
        }
    } catch (InvalidIdException e) {
        return false;
    }
    if (roleidList.size() != 3){
        return false;
    }
    try {
        DatabaseSelectHelpers.getUserDetails(1,this);
        DatabaseSelectHelpers.getUserDetails(2,this);
    } catch (InvalidIdException e) {
        return false;
    }
    return true;



}


}