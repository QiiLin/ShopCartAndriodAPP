package com.example.andrew.cscb07.code.database;
import android.content.Context;
import android.database.Cursor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


import com.example.andrew.cscb07.code.exceptions.InvalidAddressException;
import com.example.andrew.cscb07.code.exceptions.InvalidAgeException;
import com.example.andrew.cscb07.code.exceptions.InvalidIdException;
import com.example.andrew.cscb07.code.exceptions.InvalidItemException;
import com.example.andrew.cscb07.code.exceptions.InvalidUserNameException;
import com.example.andrew.cscb07.code.inventory.Item;
import com.example.andrew.cscb07.code.com.users.Roles;
import com.example.andrew.cscb07.code.com.users.User;
/**
 * Created by Neo on 2017-11-28.
 */

public class DatabaseUpdateHelpers {


    public static boolean updateRoleName(String name, int id, Context context)
            throws InvalidIdException {
        if (isInRoles(name)) {
            if (roleIdCheck(id,context)) {
                DatabaseDriverAndroid mydb = new DatabaseDriverAndroid(context);
                return mydb.updateRoleName(name, id);
            } else {
                throw new InvalidIdException();
            }
        } else {
            throw new InvalidIdException();
        }
    }

    public static boolean updateUserName(String name, int userId, Context context)
            throws InvalidUserNameException, InvalidIdException {
        if (isValidUserId(userId,context)) {
            if (userNameFormatCheck(name)) {
                DatabaseDriverAndroid mydb = new DatabaseDriverAndroid(context);
                return mydb.updateUserName(name, userId);
            } else {
                throw new InvalidUserNameException();
            }
        } else {
            throw new InvalidIdException();
        }
    }

    public static boolean updateUserAge(int age, int userId, Context context)
            throws InvalidAgeException, InvalidIdException {
        if (ageCheck(age)) {
            if (isValidUserId(userId, context)) {
                DatabaseDriverAndroid mydb = new DatabaseDriverAndroid(context);
                return mydb.updateUserAge(age, userId);
            } else {
                throw new InvalidIdException();
            }
        } else {
            throw new InvalidAgeException();
        }
    }

    public static boolean updateUserAddress(String address, int userId, Context context)
            throws InvalidIdException, InvalidAddressException {
        if (addressLimitCheck(address)) {
            if (isValidUserId(userId, context)) {
                DatabaseDriverAndroid mydb = new DatabaseDriverAndroid(context);
                return mydb.updateUserAddress(address, userId);
            } else {
                throw new InvalidIdException();
            }
        } else {
            throw new InvalidAddressException();
        }
    }

    public static boolean updateUserRole(int roleId, int userId, Context context)
            throws InvalidIdException{
        if (roleIdCheck(roleId,context)) {
            if (isValidUserId(userId, context)) {
                DatabaseDriverAndroid mydb = new DatabaseDriverAndroid(context);
                return mydb.updateUserRole(roleId, userId);
            } else {
                throw new InvalidIdException();
            }
        } else {
            throw new InvalidIdException();
        }
    }

    public static boolean updateUserPassword(String password, int id, Context context){
        DatabaseDriverAndroid mydb = new DatabaseDriverAndroid(context);
        return mydb.updateUserPassword(password, id);
    }

    public static boolean updateItemName(String name, int itemId, Context context)
            throws InvalidItemException, InvalidIdException{
        if (validItemNameCheck(name,context)) {
            if (itemIdCheck(itemId, context)) {
                DatabaseDriverAndroid mydb = new DatabaseDriverAndroid(context);
                return mydb.updateItemName(name, itemId);
            } else {
                throw new InvalidIdException();
            }
        } else {
            throw new InvalidItemException();
        }
    }

    public static boolean updateItemPrice(BigDecimal price, int itemId, Context context)
            throws InvalidIdException, InvalidItemException {
        if (vaildItemPriceCheck(price)) {
            if (itemIdCheck(itemId,context)) {
                DatabaseDriverAndroid mydb = new DatabaseDriverAndroid(context);
                return mydb.updateItemPrice(price, itemId);
            } else {
                throw new InvalidIdException();
            }
        } else {
            throw new InvalidItemException();
        }
    }

    public static boolean updateInventoryQuantity(int quantity, int itemId, Context context)
            throws InvalidIdException, InvalidItemException{
        if (itemQuantityCheck(quantity)) {
            if (itemIdCheck(itemId, context)) {
                DatabaseDriverAndroid mydb = new DatabaseDriverAndroid(context);
                return mydb.updateInventoryQuantity(quantity, itemId);
            } else {
                throw new InvalidIdException();
            }
        } else {
            throw new InvalidItemException();
        }
    }


    public static boolean updateAccountStatus(int accountId, boolean active, Context context)
            throws InvalidIdException, InvalidItemException {
        if (accountIdCheck(accountId, context)) {
            DatabaseDriverAndroid mydb = new DatabaseDriverAndroid(context);
            return mydb.updateAccountStatus(accountId, active);
        } else {
            throw new InvalidIdException();
        }
    }

    public static boolean updateAccountApprove(int accountId, boolean approve, Context context)
            throws InvalidIdException, InvalidItemException {
        if (accountIdCheck(accountId, context)) {
            DatabaseDriverAndroid mydb = new DatabaseDriverAndroid(context);
            return mydb.updateAccountApprove(accountId, approve);
        } else {
            throw new InvalidIdException();
        }
    }

    public static boolean updateUserImage(byte[] image, int userId, Context context)
            throws InvalidIdException {
        if (isValidUserId(userId, context)){
            DatabaseDriverAndroid mydb = new DatabaseDriverAndroid(context);
            return mydb.updateUserImage(image, userId);
        } else {
            throw new InvalidIdException();
        }
    }

    public static boolean updateItemImage(byte[] image, int itemId, Context context)
            throws InvalidIdException {
        if (itemIdCheck(itemId, context)){
            DatabaseDriverAndroid mydb = new DatabaseDriverAndroid(context);
            return mydb.updateItemImage(image, itemId);
        } else {
            throw new InvalidIdException();
        }
    }

    private static boolean vaildItemPriceCheck(BigDecimal price) {
        boolean itemNameCheck = false;
        if (price != null) {
            BigDecimal bottomValue = new BigDecimal(0);
            if (price.compareTo(bottomValue) == 1) {
                itemNameCheck = true;
            }
        }
        return itemNameCheck;
    }

    private static boolean isInRoles(String name) {
        // if name is not null
        if (name != null) {
            // loop through a Roles values once
            for (Roles currentRole : Roles.values()) {
                // if the name equals to the currentRole.
                if (currentRole.name().equals(name)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean roleIdCheck(int roleId, Context context){
        List<Integer> ids = DatabaseSelectHelpers.getRoleIds(context);
        return ids.contains(roleId);
    }

    private static boolean userIdCheck(int userId, Context context) throws InvalidIdException {
        List<Integer> ids = DatabaseSelectHelpers.getRoleIds(context);
        List<Integer> userIds = new ArrayList<>();
        for (Integer id : ids) {
            userIds.addAll(DatabaseSelectHelpers.getUserIdsByRole(id,context));
        }
        for (Integer id : userIds) {
            System.out.println(id);
            if (id == userId) {
                return true;
            }
        }
        return false;
    }
    private static boolean isValidUserId(int userId, Context context) {
        DatabaseDriverAndroid mydb = new DatabaseDriverAndroid(context);
        Cursor cursor = mydb.getUsersDetails();
        List<Integer> users = new ArrayList<>();
        while (cursor.moveToNext()) {
            int currUserId = cursor.getInt(cursor.getColumnIndex("ID"));
            users.add(currUserId);
        }
        cursor.close();
        for (int id : users) {
            if (id == userId) {
                return true;
            }
        }
        return false;
    }
    private static boolean userNameFormatCheck(String userName) {
        if (userName == null) {
            return false;
        }
        // splite userName into Array of String that contains all the word in it
        String[] splitedName = userName.split("\\s+");
        // By add up the first index and second index with a space between it, we form a good formatName
        String goodFormatNameCopy = splitedName[0] + " " + splitedName[1];
        // if there are only 2 word in userName and goodCopy version is the same as userName
        if (splitedName.length == 2 && goodFormatNameCopy.equals(userName)) {
            // loop through each char in each word of userName
            for (String currentWord : splitedName) {
                char firstCharacter = currentWord.charAt(0);
                // if the firstCharacter is upper case and is alphabetic
                if (Character.isUpperCase(firstCharacter) && Character.isAlphabetic(firstCharacter)) {
                    // we get sub string of it from index 1
                    char[] charList = currentWord.substring(1).toCharArray();
                    // loop through all elelemnt in the charlIst
                    for (char currentChar : charList) {
                        // if it is not lowcase or it is not alphabetic
                        if (Character.isUpperCase(currentChar) || !(Character.isAlphabetic(currentChar))) {
                            return false;
                        }
                    }
                } else {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private static boolean addressLimitCheck(String address) {
        boolean withInLimit = false;
        // if address is not null
        if (address != null) {
            // if address is less than 101
            if (address.length() <= 100) {
                withInLimit = true;
            }
        }
        return withInLimit;
    }

    private static boolean ageCheck(int age) {
        boolean valid = false;
        if (age > 0) {
            valid = true;
        }
        return valid;
    }

    private static boolean validItemNameCheck(String itemName, Context context){
        try {
            List<Item> itemList = DatabaseSelectHelpers.getAllItems(context);
            for (Item item : itemList) {
                String currItemName = item.getName();
                if (currItemName.equals(itemName)) {
                    return true;
                }
            }
            return false;
        } catch (InvalidIdException e){
            return false;
        }
    }

    private static boolean itemIdCheck(int itemId, Context context) throws InvalidIdException {
        DatabaseDriverAndroid mydb = new DatabaseDriverAndroid(context);
        Cursor cursor = mydb.getUsersDetails();
        List<Integer> items = new ArrayList<>();
        while (cursor.moveToNext()) {
            int currUserId = cursor.getInt(cursor.getColumnIndex("ID"));
            items.add(currUserId);
        }
        cursor.close();
        for (Integer item : items) {
            if (item == itemId) {
                return true;
            }
        }
        return false;
    }

    private static boolean itemQuantityCheck(int quantity) {
        boolean valid = false;
        if (quantity >= 0) {
            valid = true;
        }
        return valid;
    }

    private static List<Integer> getUserAccountIds(int userId, Context context) throws InvalidIdException{
        boolean judge = isValidUserId(userId,context);
        List<Integer> userAccounts = new ArrayList<>();
        if (judge) {
            DatabaseDriverAndroid mydb = new DatabaseDriverAndroid(context);
            Cursor cursor = mydb.getUsersDetails();
            while (cursor.moveToNext()) {
                int currUserId = cursor.getInt(cursor.getColumnIndex("ID"));
                userAccounts.add(currUserId);
            }
            cursor.close();
            return userAccounts;
        } else {
            throw new InvalidIdException();
        }
    }

    public static List<Integer> getAllaccountId(Context context) throws  InvalidIdException, InvalidItemException {
        List<Integer> accountList = new ArrayList<Integer>();
        DatabaseDriverAndroid mydb = new DatabaseDriverAndroid(context);
        Cursor cursor = mydb.getAllAccount();
        while (cursor.moveToNext()) {
            int accountId = cursor.getInt(cursor.getColumnIndex("ID"));
            accountList.add(accountId);
        }
        cursor.close();
        return accountList;
    }
    private static boolean accountIdCheck(int accountId, Context context) throws InvalidItemException {
        try {
            List<Integer> currAccounts;
            currAccounts = DatabaseSelectHelpers.getAllaccountId(context);
            if (currAccounts.contains(accountId)){
                return true;
            }
        } catch(InvalidIdException e){
            return false;
        }
        return false;
    }


}
