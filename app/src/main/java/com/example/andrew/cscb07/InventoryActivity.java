package com.example.andrew.cscb07;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.andrew.cscb07.code.database.DatabaseSelectHelpers;
import com.example.andrew.cscb07.code.database.DatabaseUpdateHelpers;
import com.example.andrew.cscb07.code.exceptions.InvalidIdException;
import com.example.andrew.cscb07.code.exceptions.InvalidItemException;
import com.example.andrew.cscb07.code.inventory.Inventory;
import com.example.andrew.cscb07.code.inventory.InventoryImpl;
import com.example.andrew.cscb07.code.inventory.Item;

import java.util.HashMap;

public class InventoryActivity extends AppCompatActivity {
    private int quantityInput;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);
        ImageButton home = (ImageButton) findViewById(R.id.homeButton);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeListener(view);
            }
        });
        Inventory inv = new InventoryImpl();
        try {
            inv = DatabaseSelectHelpers.getInventory(this);
        } catch (InvalidIdException e) {

        }
        HashMap<Item, Integer> items = inv.getItemMap();
        Button itemInfo = new Button(this);
        itemInfo.setTextColor(Color.parseColor("#fff"));
        boolean alternate = false;
        ConstraintLayout inventoryConstraint = findViewById(R.id.inventoryScrollConstraint);
        for (final Item item : items.keySet()) {
            String name = item.getName();
            final Item myItem = item;
            String quantity = String.valueOf(items.get(item));
            String result = name + " x" + quantity;
            itemInfo.setText(result);
            if (alternate) {
                itemInfo.setBackgroundColor(Color.parseColor("#4b4b4b"));
                alternate = false;
            } else if (!alternate) {
                itemInfo.setBackgroundColor(Color.parseColor("#3d3d3d"));
                alternate = true;
            }

            itemInfo.setGravity(Gravity.CENTER);

            itemInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final EditText quantity = new EditText(InventoryActivity.this);
                    quantity.setInputType(InputType.TYPE_CLASS_NUMBER);
                    AlertDialog.Builder alert = new AlertDialog.Builder(InventoryActivity.this);
                    alert.setMessage("Quantity?");
                    alert.setView(quantity);

                    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            quantityInput = Integer.parseInt(quantity.getText().toString());
                            try {
                                DatabaseUpdateHelpers.updateInventoryQuantity(quantityInput, myItem.getId(), InventoryActivity.this);
                            } catch (InvalidIdException | InvalidItemException e) {

                            }
                        }
                    });
                    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });


                    AlertDialog alertDialog = alert.create();
                    alertDialog.show();
                }
            });

            inventoryConstraint.addView(itemInfo);
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
}
