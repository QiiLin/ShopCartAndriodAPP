package com.example.andrew.cscb07;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andrew.cscb07.code.com.users.Account;
import com.example.andrew.cscb07.code.com.users.AccountImpl;
import com.example.andrew.cscb07.code.com.users.User;
import com.example.andrew.cscb07.code.database.DatabaseInsertHelpers;
import com.example.andrew.cscb07.code.database.DatabaseSelectHelpers;
import com.example.andrew.cscb07.code.database.DatabaseUpdateHelpers;
import com.example.andrew.cscb07.code.exceptions.InvalidIdException;
import com.example.andrew.cscb07.code.exceptions.InvalidItemException;
import com.example.andrew.cscb07.code.exceptions.InvalidItemQuantityException;
import com.example.andrew.cscb07.code.inventory.Item;
import com.example.andrew.cscb07.code.store.ShoppingCart;

import java.util.HashMap;
import java.util.List;

public class ShoppingCartActivity extends AppCompatActivity {
    public HashMap<Item,Integer> shoppingCart = GlobalApplication.cart.getInformation();
    public int quantityInput;
    public TextView tvitemQuantity;
    public LinearLayout list;
    public ImageButton button;
    public TextView totalCost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);

        TextView personName = (TextView) findViewById(R.id.personNameText);

        list = (LinearLayout) findViewById(R.id.ShoppingcartLinear);
        for(Item item : shoppingCart.keySet()) {

            final Item myItem = item;
            TextView tvitemName = new TextView(this);
            tvitemName.setText(item.getName());
            tvitemName.setTextSize(25);
            tvitemName.setTextColor(Color.parseColor("#FFFFFF"));
            list.addView(tvitemName);

            tvitemQuantity = new TextView(this);
            tvitemQuantity.setText("Quantity: " + shoppingCart.get(item).toString());
            tvitemQuantity.setTextSize(25);
            tvitemQuantity.setTextColor(Color.parseColor("#FFFFFF"));
            list.addView(tvitemQuantity);

            button = new ImageButton(this);
            button.setPadding(0,0,0,0);
            button.setImageResource(R.drawable.remove);
            LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(444,203, RelativeLayout.ALIGN_PARENT_LEFT);
            button.setScaleType(ImageView.ScaleType.FIT_CENTER);
            button.setAdjustViewBounds(true);
            button.setLayoutParams(buttonParams);
            list.addView(button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final EditText quantity = new EditText(ShoppingCartActivity.this);
                    quantity.setInputType(InputType.TYPE_CLASS_NUMBER);
                    AlertDialog.Builder alert = new AlertDialog.Builder(ShoppingCartActivity.this);
                    alert.setMessage("Quantity?");
                    alert.setView(quantity);

                    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            quantityInput = Integer.parseInt(quantity.getText().toString());
                            try {
                                GlobalApplication.cart.removeItem(myItem,quantityInput);
                                tvitemQuantity.setText("Quantity: " + shoppingCart.get(myItem).toString());
                                totalCost.setText("Total Price: $" + GlobalApplication.cart.getTotal());
                                Intent intent = getIntent();
                                finish();
                                startActivity(intent);
                            } catch (InvalidIdException e) {
                                e.printStackTrace();
                            } catch (InvalidItemException e) {
                                e.printStackTrace();
                            } catch (InvalidItemQuantityException e) {
                                Toast toast = Toast.makeText(ShoppingCartActivity.this, "Invalid Quantity", Toast.LENGTH_LONG);
                                toast.show();
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




        }
        ImageButton checkout = (ImageButton) findViewById(R.id.checkoutButton);
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalApplication.cart.checkOut(GlobalApplication.cart,ShoppingCartActivity.this);
                try {
                    if (GlobalApplication.accountId > 0) {

                        List<Account> accountList = DatabaseSelectHelpers.getUserApproveAccounts(GlobalApplication.userid, ShoppingCartActivity.this);
                        for (Account account : accountList) {
                            if (account.getId() == GlobalApplication.accountId) {
                                DatabaseUpdateHelpers.updateAccountStatus(GlobalApplication.accountId, false, ShoppingCartActivity.this);
                            }
                        }
                        Toast toast = Toast.makeText(ShoppingCartActivity.this, "Account shoppingcart clear!", Toast.LENGTH_LONG);
                        toast.show();
                    }
                } catch (InvalidItemException | InvalidIdException e){
                    Toast toast = Toast.makeText(ShoppingCartActivity.this, "Account shoppingcart clear!", Toast.LENGTH_LONG);
                    toast.show();
                }
                GlobalApplication.cart = new ShoppingCart();
                Toast toast = Toast.makeText(ShoppingCartActivity.this, "Checkout Successful!", Toast.LENGTH_LONG);
                toast.show();
                Intent intent = new Intent(ShoppingCartActivity.this , StoreActivity.class);
                startActivity(intent);
            }
        });

        ImageButton home = (ImageButton) findViewById(R.id.homeButton);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeListener(view);
            }
        });

        ImageButton registerButton = findViewById(R.id.Register);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean outcome = registerListener(view);
                if (outcome) {
                    Toast.makeText(getApplicationContext(), String.valueOf(GlobalApplication.accountId) +"Finish register request, wait for approve from the store", Toast.LENGTH_LONG)
                            .show();
                } else {
                    Toast.makeText(getApplicationContext(), "Failed register request, wait for approve from the store", Toast.LENGTH_SHORT)
                            .show();
                }
        }
        });



        totalCost = (TextView) findViewById(R.id.totalCostText);
        totalCost.setText("Total Price: $" + GlobalApplication.cart.getTotal());

        ImageButton logout = (ImageButton) findViewById(R.id.logoutButton);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShoppingCartActivity.this,StoreActivity.class);
                startActivity(intent);
                finish();
            }
        });
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

    private boolean registerListener(View view){
        int customerId = GlobalApplication.userid;
        String accountId = registerAccountHelper(customerId);
        boolean result = false;
        if (accountId != null){
            GlobalApplication.accountId = Integer.parseInt(accountId);
            result = associateShoppingCartToAccountHelper(accountId,GlobalApplication.cart);
        }
        return result;
    }

    private String registerAccountHelper(int inputedCustomerId) {
        Account account = new AccountImpl();
        try {
            User user = DatabaseSelectHelpers.getUserDetails(inputedCustomerId,ShoppingCartActivity.this);
            int accountId = DatabaseInsertHelpers.insertAccount(inputedCustomerId, true, false, ShoppingCartActivity.this);
            account.setId(accountId);
            account.setUserId(inputedCustomerId);
            account.setUser(user);
            String accountIdString = String.valueOf(accountId);
            System.out.println("You new account id " + accountIdString);
            return accountIdString;
        } catch (InvalidIdException e) {
            return null;
        } catch (NumberFormatException e) {
            System.out.println("Invalid Input id");
            return null;
        }
    }

    private boolean associateShoppingCartToAccountHelper(String accountIdString,
                                                                ShoppingCart shoppingCart) {
        try {
            int accountId = Integer.parseInt(accountIdString);
            Account account = DatabaseSelectHelpers.getAccountDetails(accountId,ShoppingCartActivity.this);
            account.setShoppingCart(shoppingCart);
            List<Item> itemsInShoppingCart = shoppingCart.getItems();

            for (Item item : itemsInShoppingCart) {
                int itemId = item.getId();
                int quantity = shoppingCart.getItemQuantity(item);
                DatabaseInsertHelpers.insertAccountLine(accountId, itemId, quantity,ShoppingCartActivity.this);
            }
            return true;
        } catch (InvalidIdException | InvalidItemException e) {
            return false;
        } catch (InvalidItemQuantityException e) {
            return false;
        }
    }

}
