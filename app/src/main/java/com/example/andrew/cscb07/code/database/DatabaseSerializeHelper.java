package com.example.andrew.cscb07.code.database;

/**
 * Created by Qi on 2017-12-03.
 */
import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.example.andrew.cscb07.code.exceptions.InvalidIdException;
import com.example.andrew.cscb07.code.exceptions.InvalidItemException;
import com.example.andrew.cscb07.code.inventory.Inventory;
import com.example.andrew.cscb07.code.inventory.Item;
import com.example.andrew.cscb07.code.store.Sale;
import com.example.andrew.cscb07.code.com.users.Account;
import com.example.andrew.cscb07.code.com.users.User;

public class DatabaseSerializeHelper {

    public static boolean serializeHelper(Context context) {
        try {
            File file = new File(context.getFilesDir(), "database_copy.ser");
            FileOutputStream fileOut = new FileOutputStream(file);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            List<User> userList = DatabaseSelectHelpers.getUsersDetails(context);
            getUserRoles(userList, context);
            List<Account> accountList = DatabaseSelectHelpers.getAllaccounts(context);
            int size = userList.size();
            out.writeInt(size);
            for (User user : userList) {
                out.writeObject(user);
            }
            out.writeInt(accountList.size());
            for (Account account : accountList) {
                out.writeObject(account);
            }
            List<Item> allItem = DatabaseSelectHelpers.getAllItems(context);
            out.writeInt(allItem.size());
            for (Item item : allItem) {
                out.writeObject(item);
            }
            HashMap<Integer, String> roleNames = getRoleNames(context);
            out.writeInt(roleNames.size());
            for (Integer roleId : roleNames.keySet()) {
                out.writeObject(roleId);
                out.writeObject(roleNames.get(roleId));
            }
            HashMap<User, String> userPassword = getUserPassword(userList, context);
            out.writeInt(userPassword.size());
            for (User user : userList) {
                out.writeObject(user);
                out.writeObject(userPassword.get(user));
            }
            List<Sale> saleList = getsaleList(userList, context);
            out.writeInt(saleList.size());
            for (Sale sale : saleList) {
                out.writeObject(sale);
            }
            Inventory currentInventory = DatabaseSelectHelpers.getInventory(context);
            out.writeObject(currentInventory);
            out.close();
            fileOut.close();
            return true;
        } catch (IOException | SQLException | InvalidIdException | InvalidItemException e) {
            e.printStackTrace();
            return false;
        }
    }


    private static void getUserRoles(List<User> userList, Context context) throws SQLException, InvalidIdException {
        // TODO Auto-generated method stub
        for (User user : userList) {
            int roleId = DatabaseSelectHelpers.getUserRoleId(user.getId(), context);
            user.setRoleId(roleId);
        }
    }

    private static HashMap<User, String> getUserPassword(List<User> userList, Context context) {
        HashMap<User, String> userPassword = new HashMap<User, String>();
        try {
            for (User currentUser : userList) {
                String password = null;
                password = DatabaseSelectHelpers.getPassword(currentUser.getId(), context);
                userPassword.put(currentUser, password);
            }
        } catch ( InvalidIdException e) {
            // TODO Auto-generated catch block
            return userPassword;
        }
        return userPassword;
    }

    private static HashMap<Integer, String> getRoleNames(Context context) {
        List<Integer> roleIds;
        HashMap<Integer, String> roleNames = new HashMap<Integer, String>();
        try {
            roleIds = DatabaseSelectHelpers.getRoleIds(context);
            for (Integer roleId : roleIds) {
                String roleName = DatabaseSelectHelpers.getRoleName(roleId, context);
                roleNames.put(roleId, roleName);
            }
            return roleNames;
        } catch (InvalidIdException e) {
            // TODO Auto-generated catch block
            return roleNames;
        }
    }

    private static List<Sale> getsaleList(List<User> userList, Context context) {
        List<Sale> saleIds = new ArrayList<Sale>();
        try {
            for (User user : userList) {
                saleIds.addAll(DatabaseSelectHelpers.getSalesToUser(user.getId(), context));
            }
        } catch (InvalidIdException e) {
            // TODO Auto-generated catch block
            return saleIds;
        }
        return saleIds;
    }
}

