package com.example.andrew.cscb07.code.database;

/**
 * Created by Qi on 2017-12-03.
 */

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.example.andrew.cscb07.code.exceptions.InvalidAddressExcetpion;
import com.example.andrew.cscb07.code.exceptions.InvalidAgeException;
import com.example.andrew.cscb07.code.exceptions.InvalidIdException;
import com.example.andrew.cscb07.code.exceptions.InvalidItemException;
import com.example.andrew.cscb07.code.exceptions.InvalidItemNameException;
import com.example.andrew.cscb07.code.exceptions.InvalidItemQuantityException;
import com.example.andrew.cscb07.code.exceptions.InvalidTotalPriceException;
import com.example.andrew.cscb07.code.exceptions.InvalidUserNameException;
import com.example.andrew.cscb07.code.inventory.Inventory;
import com.example.andrew.cscb07.code.inventory.Item;
import com.example.andrew.cscb07.code.store.Sale;
import com.example.andrew.cscb07.code.com.users.Account;
import com.example.andrew.cscb07.code.com.users.User;

public class DatabaseDeserializeHelper {

    public static boolean deserializeHelper(Context context) {
        boolean restored = false;
        try {
            File file = new File(context.getFilesDir(), "database_copy.ser");
            FileInputStream fileIn = new FileInputStream(file);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            List<Account> userAccount = new ArrayList<Account>();
            List<User> userList = new ArrayList<User>();
            int size = in.readInt();
            for (int i = 0; i < size; i++) {
                User user = (User) in.readObject();
                userList.add(user);
            }
            int accountSize = in.readInt();
            for (int i = 0; i < accountSize; i++) {
                Account account = (Account) in.readObject();
                userAccount.add(account);
            }
            int itemSize = in.readInt();
            List<Item> items = new ArrayList<Item>();
            for (int i = 0; i < itemSize; i++) {
                Item item = (Item) in.readObject();
                items.add(item);
            }
            HashMap<Integer, String> roleNames = new HashMap<Integer, String>();
            int roleSize = in.readInt();
            for (int i = 0; i < roleSize; i++) {
                Integer roleId = (Integer) in.readObject();
                String roleName = (String) in.readObject();
                roleNames.put(roleId, roleName);
            }
            HashMap<User, String> userPassword = new HashMap<User, String>();
            int userSize = in.readInt();
            for (int i = 0; i < userSize; i++) {
                User user = (User) in.readObject();
                String password = (String) in.readObject();
                userPassword.put(user, password);
            }
            List<Sale> saleList = new ArrayList<Sale>();
            int saleListSize = in.readInt();
            for (int i = 0; i < saleListSize; i++) {
                Sale sale = (Sale) in.readObject();
                saleList.add(sale);
            }
            Inventory inventory = (Inventory) in.readObject();
            DatabaseDriverAndroid mydb = new DatabaseDriverAndroid(context);
            mydb.onUpgrade(mydb.getWritableDatabase(),0,1);
            boolean restoreUser = restoreUser(userPassword, roleNames, userList, context);
            boolean restoreInventoryAndItem = restoreInventoryAndItem(inventory, items, context);
            boolean restoreSales = restoreSale(saleList, context);
            boolean restoreAccount = restoreAccounts(userAccount, context);
            in.close();
            fileIn.close();
            restored = restoreUser && restoreInventoryAndItem && restoreSales && restoreAccount;
        } catch (IOException i) {
            return restored;
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            return restored;
        }
        return restored;
    }

    private static boolean restoreAccounts(List<Account> userAccounts, Context context) {
        // TODO Auto-generated method stub

        try {
            for (Account account : userAccounts) {
                int accountId =
                        DatabaseInsertHelpers.insertAccount(account.getUserId(), account.getActive(), account.getApprove(), context);
                HashMap<Item, Integer> itemInCart = account.getShoppingCart().getInformation();
                for (Item item : itemInCart.keySet()) {
                    DatabaseInsertHelpers.insertAccountLine(accountId, item.getId(), itemInCart.get(item), context);
                }
            }
            return true;
        } catch ( InvalidIdException | InvalidItemException | InvalidItemQuantityException e) {
            return false;
        }

    }

    private static boolean restoreSale(List<Sale> saleList, Context context) {
        // TODO Auto-generated method stub
        try {
            DatabaseInsertHelpers insertHelper = new DatabaseInsertHelpers();
            for (Sale sale : saleList) {
                int saleId = insertHelper.insertSale(sale.getUser().getId(), sale.getTotalPrice(),context);
                HashMap<Item, Integer> saleItems = sale.getItemMap();
                for (Item item : saleItems.keySet()) {
                    insertHelper.insertItemizedSale(saleId, item.getId(), saleItems.get(item),context);
                }
            }
            return true;
        } catch (InvalidIdException
                | InvalidTotalPriceException | InvalidItemException e) {
            return false;
        }
    }

    private static boolean restoreInventoryAndItem(Inventory currentInventory, List<Item> items ,Context context) {
        DatabaseInsertHelpers insertHelper = new DatabaseInsertHelpers();
        try {
            for (Item item : items) {
                insertHelper.insertItem(item.getName(), item.getPrice(), context);
            }
            HashMap<Item, Integer> inventory = currentInventory.getItemMap();
            for (Item item : inventory.keySet()) {
                insertHelper.insertInventory(item.getId(), inventory.get(item), context);
            }
            return true;
        } catch (InvalidIdException | InvalidItemException | InvalidItemNameException e) {
            return false;
        }
    }

    private static boolean restoreUser(HashMap<User, String> userPassword,
                                       HashMap<Integer, String> roleNames, List<User> userList ,Context context) {
        try {
            for (User user : userList) {
                String password = userPassword.get(user);
                int userId;
                userId = DatabaseInsertHelpers.insertNewUser(user.getName(), user.getAge(),
                        user.getAddress(), "0", context);
                String roleName = roleNames.get(user.getRoleId());
                int roleId = DatabaseInsertHelpers.insertRole(roleName,context);
                DatabaseInsertHelpers.insertUserRole(userId, roleId, context);
                DatabaseUpdateHelpers.updateUserPassword(password, user.getId(), context);
            }
            return true;
        } catch ( InvalidAddressExcetpion | InvalidAgeException
                | InvalidUserNameException | InvalidIdException e) {
            // TODO Auto-generated catch block
            return false;
        }
    }
}
