package com.example.andrew.cscb07;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.Image;
import android.provider.Settings;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andrew.cscb07.code.database.DatabaseInsertHelpers;
import com.example.andrew.cscb07.code.database.DatabaseSelectHelpers;
import com.example.andrew.cscb07.code.exceptions.InvalidItemException;
import com.example.andrew.cscb07.code.inventory.Item;
import com.example.andrew.cscb07.code.exceptions.InvalidIdException;
import com.example.andrew.cscb07.code.store.ShoppingCart;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class StoreActivity extends AppCompatActivity {
    public List<Item> itemList;
    private Boolean confirm = false;
    private int quantityInput;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        LinearLayout list = (LinearLayout) findViewById(R.id.itemsList);
        try {
            itemList = DatabaseSelectHelpers.getAllItems(this);
        } catch (InvalidIdException e) {
            e.printStackTrace();
        }

        for(Item item : itemList){

            final Item myItem = item;
            ImageView itemPic = new ImageView(this);
            TextView itemName = new TextView(this);
            Bitmap itemBit;

            try{
                itemBit = DatabaseSelectHelpers.getItemImage(item.getId(),this);
                itemPic.setImageBitmap(itemBit);
                itemPic.setLayoutParams(new ConstraintLayout
                        .LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,800));
            } catch (InvalidIdException e){

            }

            itemName.setText(item.getName());
            itemName.setTextSize(25);
            itemName.setTextColor(Color.parseColor("#FFFFFF"));
            list.addView(itemPic);
            itemName.setGravity(Gravity.CENTER_HORIZONTAL);
            list.addView(itemName);

            TextView itemPrice = new TextView(this);
            String price = item.getPrice().toString();
            String result = "$"+price;
            itemPrice.setText(result);
            itemPrice.setTextSize(25);
            itemPrice.setGravity(Gravity.CENTER_HORIZONTAL);
            itemPrice.setTextColor(Color.parseColor("#FFFFFF"));
            list.addView(itemPrice);

            TextView itemQuantity = new TextView(this);
            String quantity = item.getPrice().toString();
            try {
                itemQuantity.setText("Quantity: " + DatabaseSelectHelpers.getInventoryQuantity(item.getId(),StoreActivity.this));
            } catch (InvalidIdException e) {
                e.printStackTrace();
            }
            itemQuantity.setTextSize(25);
            itemQuantity.setTextColor(Color.parseColor("#FFFFFF"));
            itemQuantity.setGravity(Gravity.CENTER_HORIZONTAL);
            list.addView(itemQuantity);

            ImageButton button = new ImageButton(this);
            button.setPadding(0,0,0,0);
            button.setImageResource(R.drawable.addtocart);
            LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(444,203, RelativeLayout.ALIGN_PARENT_LEFT);
            button.setScaleType(ImageView.ScaleType.FIT_CENTER);
            button.setAdjustViewBounds(true);
            button.setLayoutParams(buttonParams);
            button.setForegroundGravity(Gravity.CENTER_HORIZONTAL);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final EditText quantity = new EditText(StoreActivity.this);
                    quantity.setInputType(InputType.TYPE_CLASS_NUMBER);
                    AlertDialog.Builder alert = new AlertDialog.Builder(StoreActivity.this);
                    alert.setMessage("Quantity?");
                    alert.setView(quantity);

                    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            quantityInput = Integer.parseInt(quantity.getText().toString());
                            try {
                                GlobalApplication.cart.addItem(myItem,quantityInput);
                            } catch (InvalidIdException e) {
                                e.printStackTrace();
                            } catch (InvalidItemException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    alert.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });


                    AlertDialog alertDialog = alert.create();
                    alertDialog.show();

                }
            });

            list.addView(button);
        }

        ImageButton home = (ImageButton) findViewById(R.id.homeButton);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeListener(view);
            }
        });

        ImageButton userSettings = (ImageButton) findViewById(R.id.userSettingsButton);
        userSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userSettingsListener(view);
            }
        });

        ImageButton viewCart = (ImageButton) findViewById(R.id.viewCartButton);
        viewCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewCartListener(view);
            }
        });

        }


    public void confirmOk(){
        this.confirm = true;
    }

    public void confirmCancel(){
        this.confirm = false;
    }


    public void homeListener(View view) {
        Intent intent = new Intent(this, StoreActivity.class);
        startActivity(intent);
    }
    public void userSettingsListener(View view){
        Intent intent = new Intent(this, UserActivity.class);
        startActivity(intent);
    }

    public void viewCartListener(View view){
        Intent intent = new Intent(this, ShoppingCartActivity.class);
        startActivity(intent);
        finish();
    }

    public Integer checkifiteminCart(String itemid,ArrayList<String> itemids){
        int counter = 0;
        for(String item : itemids){
            if (itemid.equals(item)){
                return counter;
            }
            counter++;
        }
        return null;
    }


}

